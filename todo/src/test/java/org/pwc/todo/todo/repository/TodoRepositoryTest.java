package org.pwc.todo.todo.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.pwc.todo.support.DataJpaTestSupport;
import org.pwc.todo.todo.domain.Category;
import org.pwc.todo.todo.domain.Todo;
import org.pwc.todo.todo.dto.TodoResponse;
import org.pwc.todo.user.UserRepository;
import org.pwc.todo.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

class TodoRepositoryTest extends DataJpaTestSupport {
	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private UserRepository userRepository;

	@Nested
	@DisplayName("OrderIndex 변경")
	class OrderIndexTest {
		private User user;
		@BeforeEach
		void setUp() {
			user = userRepository.save(new User("user1@gmail.com", "1234", "와와와"));
			todoRepository.saveAll(List.of(
				new Todo(1, Category.ETC, user, "와", LocalDate.now()),
				new Todo(2, Category.ETC, user, "와", LocalDate.now()),
				new Todo(3, Category.ETC, user, "와", LocalDate.now()),
				new Todo(4, Category.ETC, user, "와", LocalDate.now())
			));
		}

		@Test
		@DisplayName("1~3 구간 orderIndex 1 증가")
		void incrementOrderBetween() {
			assertThat(getAllOrders()).containsExactly(1, 2, 3, 4);

			todoRepository.incrementOrderBetween(user, 1, 3);

			assertThat(getAllOrders()).containsExactly(2, 3, 4, 4);
		}

		@Test
		@DisplayName("2~4 구간 orderIndex 1 감소")
		void decrementOrderBetween() {
			assertThat(getAllOrders()).containsExactly(1, 2, 3, 4);

			todoRepository.decrementOrderBetween(user, 2, 4);

			assertThat(getAllOrders()).containsExactly(1, 1, 2, 3);
		}

		private List<Integer> getAllOrders() {
			return todoRepository.findByUserOrderByOrderIndexAsc(user)
				.stream()
				.map(Todo::getOrderIndex)
				.toList();
		}
	}

	@Nested
	@DisplayName("todo 필터링")
	class searchFilter{
		private User user1;
		@BeforeEach
		void setUp() {
			user1 = userRepository.save(new User("user1@gmail.com", "1234", "와와와"));
		}

		@Test
		@DisplayName("다른 유저가 생성한 todo는 볼 수 없다")
		void searchTodos_filter_user() {
			User user2 = userRepository.save(new User("user2@gmail.com", "1234", "와와와"));
			Todo todo1 = new Todo(1, Category.ETC, user1, "와", LocalDate.now());
			Todo todo2 = new Todo(2, Category.ETC, user2, "와", LocalDate.now());
			todoRepository.saveAll(List.of(todo1, todo2));

			List<TodoResponse> response = todoRepository.
				searchTodos(user2, new SearchCondition(null, null),
					pageRequest).getContent();
			assertAll(
				() -> assertThat(response).hasSize(1),
				() -> assertThat(response.get(0).id())
					.isEqualTo(todo2.getId())
			);
		}

		@Test
		@DisplayName("특정 사용자의 카테고리 필터로 볼 수 있다.")
		void searchTodos_filter_category() {
			Todo todo1 = new Todo(1, Category.EXERCISE, user1, "와", LocalDate.now());
			Todo todo2 = new Todo(2, Category.EXERCISE, user1, "와", LocalDate.now());
			Todo todo3 = new Todo(3, Category.HOME, user1, "와", LocalDate.now());
			todoRepository.saveAll(List.of(todo1, todo2, todo3));

			List<TodoResponse> response = todoRepository.
				searchTodos(user1, new SearchCondition(Category.EXERCISE, null),
					pageRequest).getContent();

			assertAll(
				() -> assertThat(response).hasSize(2),
				() -> assertThat(response.get(0).id())
					.isEqualTo(todo1.getId()),
				() -> assertThat(response.get(1).id())
					.isEqualTo(todo2.getId())
			);
		}

		@Test
		@DisplayName("특정 사용자의 완료여부 필터로 볼 수 있다.")
		void searchTodos_filter_iscompleted() {
			Todo todo1 = new Todo(1, Category.EXERCISE, user1, "와", LocalDate.now());
			Todo todo2 = new Todo(2, Category.EXERCISE, user1, "와", LocalDate.now());
			Todo todo3 = new Todo(3, Category.EXERCISE, user1, "와", LocalDate.now());
			todo1.markCompleted();
			todo2.markCompleted();

			todoRepository.saveAll(List.of(todo1, todo2, todo3));
			List<TodoResponse> response = todoRepository.
				searchTodos(user1, new SearchCondition(null, true),
					pageRequest).getContent();
			assertAll(
				() -> assertThat(response).hasSize(2),
				() -> assertThat(response.get(0).id())
					.isEqualTo(todo1.getId()),
				() -> assertThat(response.get(1).id())
					.isEqualTo(todo2.getId())
			);
		}

		@Test
		@DisplayName("특정 사용자의 복합 필터로 볼 수 있다.")
		void searchTodos_filter_category_iscompleted() {
			User user2 = userRepository.save(new User("user2@gmail.com", "1234", "와와와"));
			Todo todo1 = new Todo(1, Category.EXERCISE, user1, "와", LocalDate.now());
			Todo todo2 = new Todo(2, Category.EXERCISE, user2, "와", LocalDate.now());
			Todo todo3 = new Todo(3, Category.HOME, user1, "와", LocalDate.now());
			todo2.markCompleted();
			todoRepository.saveAll(List.of(todo1, todo2, todo3));

			List<TodoResponse> response = todoRepository.
				searchTodos(user1, new SearchCondition(Category.EXERCISE, false),
					pageRequest).getContent();
			assertAll(
				() -> assertThat(response).hasSize(1),
				() -> assertThat(response.get(0).id())
					.isEqualTo(todo1.getId())
			);
		}
	}

}