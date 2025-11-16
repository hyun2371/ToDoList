package org.pwc.todo.todo.repository;

import org.pwc.todo.todo.dto.TodoResponse;
import org.pwc.todo.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TodoQueryRepository {
	Slice<TodoResponse> searchTodos(
		User user,
		SearchCondition condition,
		Pageable pageable
	);
}
