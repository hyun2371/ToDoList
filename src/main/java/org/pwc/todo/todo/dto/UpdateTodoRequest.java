package org.pwc.todo.todo.dto;

import java.time.LocalDate;

import org.pwc.todo.todo.domain.Category;

import jakarta.validation.constraints.FutureOrPresent;

public record UpdateTodoRequest (
	Category category,
	String content,
	@FutureOrPresent(message = "마감일은 오늘 이후 날짜여야 합니다.")
	LocalDate dueDate
){}
