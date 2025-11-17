package org.pwc.todo.todo.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.pwc.todo.todo.domain.Todo;
import org.pwc.todo.user.domain.User;
import org.pwc.todo.user.domain.UserInfo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoMapper {
	public static TodoResponse toTodoResponse(Todo todo){
		int dDay = (int)ChronoUnit.DAYS.between(LocalDate.now(), todo.getDueDate());
		return new TodoResponse(
			todo.getId(),
			todo.getContent(),
			todo.getCategory(),
			todo.isCompleted(),
			todo.getDueDate(),
			todo.getOrderIndex(),
			dDay,
			new UserInfo(todo.getUser().getId(),todo.getUser().getNickname())
		);
	}
}
