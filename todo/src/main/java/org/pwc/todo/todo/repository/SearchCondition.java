package org.pwc.todo.todo.repository;

import org.pwc.todo.todo.domain.Category;

public record SearchCondition (
	Category category,
	Boolean isCompleted
){}
