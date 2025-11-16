package org.pwc.todo.todo.dto;

import java.time.LocalDate;

import org.pwc.todo.todo.domain.Category;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTodoRequest (
	@NotBlank(message = "내용을 입력해주세요.")
	String content,
	@NotNull(message = "카테고리를 입력해주세요.")
	Category category,
	@FutureOrPresent(message = "마감일은 오늘 이후 날짜여야 합니다.")
	LocalDate dueDate
){
}
