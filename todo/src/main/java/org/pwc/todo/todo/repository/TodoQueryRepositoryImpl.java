package org.pwc.todo.todo.repository;

import static org.pwc.todo.todo.domain.QTodo.*;

import java.util.List;

import org.pwc.todo.todo.domain.Category;
import org.pwc.todo.todo.domain.Todo;
import org.pwc.todo.todo.dto.TodoMapper;
import org.pwc.todo.todo.dto.TodoResponse;
import org.pwc.todo.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoQueryRepositoryImpl implements TodoQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<TodoResponse> searchTodos(User user, SearchCondition condition, Pageable pageable) {

		List<Todo> results = queryFactory
			.selectFrom(todo)
			.where(
				todo.user.eq(user),
				categoryEq(condition.category()),
				completedEq(condition.isCompleted()),
				todo.isDeleted.isFalse()
			)
			.orderBy(todo.orderIndex.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L)
			.fetch();

		boolean hasNext = hasNext(pageable.getPageSize(), results);

		List<TodoResponse> content = results.stream()
			.limit(pageable.getPageSize())
			.map(TodoMapper::toTodoResponse)
			.toList();

		return new SliceImpl<>(content, pageable, hasNext);
	}

	private BooleanExpression categoryEq(Category category) {
		return category != null ? todo.category.eq(category) : null;
	}

	private BooleanExpression completedEq(Boolean completed) {
		return completed != null ? todo.isCompleted.eq(completed) : null;
	}

	private <T> boolean hasNext(int pageSize, List<T> items) {
		return items.size() > pageSize;
	}
}
