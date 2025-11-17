package org.pwc.todo.common.exception;

public record ErrorResponse(
	String message,
	String code
) {
}
