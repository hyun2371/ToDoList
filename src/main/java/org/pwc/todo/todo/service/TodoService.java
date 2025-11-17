package org.pwc.todo.todo.service;

import java.util.Objects;

import org.pwc.todo.common.dto.PageMapper;
import org.pwc.todo.common.dto.PageResponse;
import org.pwc.todo.common.exception.NotFoundException;
import org.pwc.todo.common.exception.ValidationException;
import org.pwc.todo.share.repository.ShareRepository;
import org.pwc.todo.todo.dto.CreateTodoResponse;
import org.pwc.todo.todo.exception.TodoErrorCode;
import org.pwc.todo.todo.domain.Todo;
import org.pwc.todo.todo.dto.CreateTodoRequest;
import org.pwc.todo.todo.dto.TodoMapper;
import org.pwc.todo.todo.dto.TodoResponse;
import org.pwc.todo.todo.dto.UpdateTodoRequest;
import org.pwc.todo.todo.repository.SearchCondition;
import org.pwc.todo.todo.repository.TodoRepository;
import org.pwc.todo.user.UserErrorCode;
import org.pwc.todo.user.UserRepository;
import org.pwc.todo.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
	private final ShareRepository shareRepository;

	@Transactional
	public CreateTodoResponse createTodo(Long userId, CreateTodoRequest request) {
		User user = getUserOrThrow(userId);
		int orderIndex = (int)todoRepository.countByUser(user) + 1;
		Todo todo = new Todo(
			orderIndex,
			request.category(),
			user,
			request.content(),
			request.dueDate()
		);
		Todo savedTodo = todoRepository.save(todo);
		return new CreateTodoResponse(savedTodo.getId());
	}

	@Transactional(readOnly = true)
	public PageResponse<TodoResponse> searchTodos(
		Long userId,
		SearchCondition condition,
		Pageable pageable
	) {
		User user = getUserOrThrow(userId);
		Slice<TodoResponse> todoResponsePage =
			todoRepository.searchTodos(user, condition, pageable);
		return PageMapper.toPageResponse(todoResponsePage);
	}

	@Transactional(readOnly = true)
	public PageResponse<TodoResponse> searchSharedTodos(Long viewerId, Long ownerId, SearchCondition condition, Pageable pageable) {
		User owner = getUserOrThrow(ownerId);

		boolean isShared = shareRepository.existsByOwnerIdAndViewerId(ownerId, viewerId);
		if (!isShared) {
			throw new ValidationException(TodoErrorCode.PERMISSION_DENIED);
		}

		Slice<TodoResponse> page = todoRepository.searchTodos(owner, condition, pageable);
		return PageMapper.toPageResponse(page);
	}

	@Transactional
	public TodoResponse updateTodo(Long userId, Long todoId, UpdateTodoRequest request) {
		Todo todo = getTodoOrThrow(todoId);
		User user = getUserOrThrow(userId);
		if (isUnAuthorized(user, todo)) {
			throw new ValidationException(TodoErrorCode.PERMISSION_DENIED);
		}
		todo.update(request.content(), request.category(), request.dueDate());

		return TodoMapper.toTodoResponse(todo);
	}

	@Transactional
	public TodoResponse completeTodo(Long userId, Long todoId) {
		Todo todo = getTodoOrThrow(todoId);
		User user = getUserOrThrow(userId);

		if (isUnAuthorized(user, todo)) {
			throw new ValidationException(TodoErrorCode.PERMISSION_DENIED);
		}

		todo.markCompleted();
		return TodoMapper.toTodoResponse(todo);
	}

	@Transactional
	public TodoResponse uncompleteTodo(Long userId, Long todoId) {
		Todo todo = getTodoOrThrow(todoId);
		User user = getUserOrThrow(userId);

		if (isUnAuthorized(user, todo)) {
			throw new ValidationException(TodoErrorCode.PERMISSION_DENIED);
		}

		return TodoMapper.toTodoResponse(todo);
	}

	@Transactional
	public void deleteTodo(Long userId, Long todoId) {
		Todo todo = getTodoOrThrow(todoId);
		User user = getUserOrThrow(userId);
		if (isUnAuthorized(user, todo)) {
			throw new ValidationException(TodoErrorCode.PERMISSION_DENIED);
		}
		todo.markDeleted();
	}

	@Transactional
	public void updateOrder(Long userId, Long todoId, int newOrderIndex) {
		Todo todo = getTodoOrThrow(todoId);
		User user = getUserOrThrow(userId);

		if (isUnAuthorized(user, todo)) {
			throw new ValidationException(TodoErrorCode.PERMISSION_DENIED);
		}
		int oldIndex = todo.getOrderIndex();

		if (oldIndex == newOrderIndex)
			return;

		if (newOrderIndex < oldIndex) {
			// 위로 이동: 중간 요소들을 한 칸씩 내림
			todoRepository.incrementOrderBetween(user, newOrderIndex, oldIndex - 1);
		} else {
			// 아래로 이동: 중간 요소들을 한 칸씩 올림
			todoRepository.decrementOrderBetween(user, oldIndex + 1, newOrderIndex);
		}
		todo = getTodoOrThrow(todoId);
		todo.updateOrderIndex(newOrderIndex);
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_USER));
	}

	private Todo getTodoOrThrow(Long todoId) {
		return todoRepository.findById(todoId)
			.orElseThrow(() -> new NotFoundException(TodoErrorCode.NOT_FOUND_TODO));
	}

	private boolean isUnAuthorized(User user, Todo todo) {
		boolean isOwner = Objects.equals(user.getId(), todo.getUser().getId());
		return !isOwner;
		//&& user.getRole() != Role.ADMIN; //관리자도 권한 부여하도록 정책 변경시 주석 해제
	}
}
