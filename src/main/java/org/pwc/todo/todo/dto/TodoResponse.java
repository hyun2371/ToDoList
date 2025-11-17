package org.pwc.todo.todo.dto;

import java.time.LocalDate;

import org.pwc.todo.todo.domain.Category;
import org.pwc.todo.user.domain.UserInfo;

public record TodoResponse (
	long id,
	String content,
	Category category,
	boolean isCompleted,
	LocalDate dueDate,
	int orderIndex,
	int dDay,
	UserInfo userInfo
){}
