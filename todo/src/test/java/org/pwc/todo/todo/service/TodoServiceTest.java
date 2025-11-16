package org.pwc.todo.todo.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.pwc.todo.common.exception.NotFoundException;
import org.pwc.todo.common.exception.ValidationException;
import org.pwc.todo.support.IntegrationTestSupport;
import org.pwc.todo.todo.domain.Category;
import org.pwc.todo.todo.domain.Todo;
import org.pwc.todo.todo.dto.CreateTodoRequest;
import org.pwc.todo.todo.dto.CreateTodoResponse;
import org.pwc.todo.todo.dto.UpdateTodoRequest;
import org.pwc.todo.todo.exception.TodoErrorCode;
import org.pwc.todo.todo.repository.TodoRepository;
import org.pwc.todo.user.UserRepository;
import org.pwc.todo.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

class TodoServiceTest extends IntegrationTestSupport {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TodoRepository todoRepository;

	@Autowired
	TodoService todoService;

	private User user1;

	@BeforeEach
	void setUp() {
		user1 = userRepository.save(new User("user1@gmail.com", "1234", "와와와"));
	}

	@AfterEach
	void clean(){
		todoRepository.deleteAll();
	}

	@Test
	@DisplayName("Todo를 생성할 수 있다")
	void createTodo() {
		CreateTodoRequest request = new CreateTodoRequest("할일", Category.ETC, LocalDate.now());
		CreateTodoResponse response = todoService.createTodo(user1.getId(), request);
		Todo todo = todoRepository.findById(response.todoId()).orElseThrow();
		assertAll(
			() -> assertThat(todo.getId()).isEqualTo(response.todoId()),
			() -> assertThat(todo.getCategory()).isEqualTo(request.category()),
			() -> assertThat(todo.getContent()).isEqualTo(request.content()),
			() -> assertThat(todo.isCompleted()).isFalse(),
			() -> assertThat(todo.getDueDate()).isEqualTo(request.dueDate())
		);
	}

	@Nested
	@DisplayName("Todo 업데이트")
	class UpdateTodoTest {
		Todo prevTodo;
		@BeforeEach
		void setUp(){
			prevTodo = todoRepository.save(new Todo(1, Category.ETC, user1, "할일1", LocalDate.now()));
		}

		@Test
		@DisplayName("카테고리 업데이트")
		void updateCategory(){
			UpdateTodoRequest request = new UpdateTodoRequest(Category.EXERCISE,null,null);
			todoService.updateTodo(user1.getId(), prevTodo.getId(), request);
			Todo updatedTodo = todoRepository.findById(prevTodo.getId()).orElseThrow();
			assertAll(
				() -> assertThat(updatedTodo.getCategory()).isEqualTo(request.category()),
				() -> assertThat(updatedTodo.getContent()).isEqualTo(prevTodo.getContent()),
				() -> assertThat(updatedTodo.getId()).isEqualTo(prevTodo.getId()),
				() -> assertThat(updatedTodo.isCompleted()).isEqualTo(prevTodo.isCompleted()),
				() -> assertThat(updatedTodo.getDueDate()).isEqualTo(prevTodo.getDueDate())
			);
		}

		@Test
		@DisplayName("내용 업데이트")
		void updateContent(){
			UpdateTodoRequest request = new UpdateTodoRequest(null,"와",null);
			todoService.updateTodo(user1.getId(), prevTodo.getId(), request);
			Todo updatedTodo = todoRepository.findById(prevTodo.getId()).orElseThrow();
			assertAll(
				() -> assertThat(updatedTodo.getContent()).isEqualTo(request.content()),
				() -> assertThat(updatedTodo.getId()).isEqualTo(prevTodo.getId()),
				() -> assertThat(updatedTodo.getCategory()).isEqualTo(prevTodo.getCategory()),
				() -> assertThat(updatedTodo.isCompleted()).isEqualTo(prevTodo.isCompleted()),
				() -> assertThat(updatedTodo.getDueDate()).isEqualTo(prevTodo.getDueDate())
			);
		}

		@Test
		@DisplayName("마감일 업데이트")
		void updateDueDate(){
			UpdateTodoRequest request = new UpdateTodoRequest(null,null,LocalDate.now().plusDays(2));
			todoService.updateTodo(user1.getId(), prevTodo.getId(), request);
			Todo updatedTodo = todoRepository.findById(prevTodo.getId()).orElseThrow();
			assertAll(
				() -> assertThat(updatedTodo.getDueDate()).isEqualTo(request.dueDate()),
				() -> assertThat(updatedTodo.getContent()).isEqualTo(prevTodo.getContent()),
				() -> assertThat(updatedTodo.getId()).isEqualTo(prevTodo.getId()),
				() -> assertThat(updatedTodo.getCategory()).isEqualTo(prevTodo.getCategory()),
				() -> assertThat(updatedTodo.isCompleted()).isEqualTo(prevTodo.isCompleted())
			);
			System.out.println(updatedTodo);
		}

		@Test
		@DisplayName("다른 사용자는 Todo를 수정할 수 없다")
		void updateTodoFail_permissionDenied() {
			// given
			User otherUser = userRepository.save(new User("other@mail.com", "pw", "다른유저"));
			UpdateTodoRequest request = new UpdateTodoRequest(Category.EXERCISE, null, null);

			// when
			ValidationException exception = assertThrows(
				ValidationException.class,
				() -> todoService.updateTodo(otherUser.getId(), prevTodo.getId(), request)
			);

			// then
			assertThat(exception.getMessage()).isEqualTo(TodoErrorCode.PERMISSION_DENIED.getMessage());
		}

	}

	@Nested
	@DisplayName("Todo soft delete")
	class DeleteTodoTest{

		@Test
		@DisplayName("Todo를 soft delete 할 수 있다")
		void deleteTodo() {
			Todo todo = todoRepository.save(new Todo(1, Category.ETC, user1, "할일1", LocalDate.now()));
			todoService.deleteTodo(user1.getId(), todo.getId());
			Todo updatedTodo = todoRepository.findById(todo.getId()).orElseThrow();
			Assertions.assertThat(updatedTodo.isDeleted()).isTrue();
		}

		@Test
		@DisplayName("존재하지 않는 투두 삭제 불가")
		void deleteTodoFail_deleted() {
			NotFoundException exception = assertThrows(
				NotFoundException.class,
				() -> todoService.deleteTodo(user1.getId(),1L)
			);

			assertThat(exception.getMessage())
				.isEqualTo(TodoErrorCode.NOT_FOUND_TODO.getMessage());
		}
	}

	@Nested
	@DisplayName("Todo 순서 변경")
	class UpdateOrderTest {
		Todo todo1;
		Todo todo2;
		Todo todo3;
		Todo todo4;

		@BeforeEach
		void setup() {
			todo1 = new Todo(1, Category.ETC, user1, "할일1", LocalDate.now());
			todo2 = new Todo(2, Category.ETC, user1, "할일2", LocalDate.now());
			todo3 = new Todo(3, Category.ETC, user1, "할일3", LocalDate.now());
			todo4 = new Todo(4, Category.ETC, user1, "할일4", LocalDate.now());

			todoRepository.saveAll(List.of(todo1, todo2, todo3, todo4));
		}

		@Test
		@DisplayName("4번을 2번으로 이동한다 (위로 이동)")
		void updateOrderUp() {
			List<Todo> before = todoRepository.findByUserOrderByOrderIndexAsc(user1);
			assertThat(before)
				.extracting(Todo::getId)
				.containsExactly(
					todo1.getId(),
					todo2.getId(),
					todo3.getId(),
					todo4.getId()
				);

			todoService.updateOrder(user1.getId(), todo4.getId(), 2);

			List<Todo> after = todoRepository.findByUserOrderByOrderIndexAsc(user1);
			assertThat(after)
				.extracting(Todo::getId)
				.containsExactly(
					todo1.getId(),
					todo4.getId(),
					todo2.getId(),
					todo3.getId()
				);
		}

		@Test
		@DisplayName("2번을 4번으로 이동한다 (아래로 이동)")
		void updateOrderDown() {
			List<Todo> before = todoRepository.findByUserOrderByOrderIndexAsc(user1);
			assertThat(before)
				.extracting(Todo::getId)
				.containsExactly(
					todo1.getId(),
					todo2.getId(),
					todo3.getId(),
					todo4.getId()
				);

			todoService.updateOrder(user1.getId(), todo2.getId(), 4);

			List<Todo> after = todoRepository.findByUserOrderByOrderIndexAsc(user1);
			assertThat(after)
				.extracting(Todo::getId)
				.containsExactly(
					todo1.getId(),
					todo3.getId(),
					todo4.getId(),
					todo2.getId()
				);
		}

		@Test
		@DisplayName("같은 순서로 변경하면 변화가 없다")
		void updateOrderSame() {
			List<Todo> before = todoRepository.findByUserOrderByOrderIndexAsc(user1);
			assertThat(before)
				.extracting(Todo::getId)
				.containsExactly(
					todo1.getId(),
					todo2.getId(),
					todo3.getId(),
					todo4.getId()
				);

			todoService.updateOrder(user1.getId(), todo2.getId(), 2);

			List<Todo> after = todoRepository.findByUserOrderByOrderIndexAsc(user1);
			assertThat(after)
				.extracting(Todo::getId)
				.containsExactly(
					todo1.getId(),
					todo2.getId(),
					todo3.getId(),
					todo4.getId()
				);
		}
	}
}