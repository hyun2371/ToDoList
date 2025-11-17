package org.pwc.todo.todo.exception;

import org.pwc.todo.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode {
	NOT_FOUND_TODO("해당 아이디의 할일이 존재하지 않습니다.", "T_001"),
	PERMISSION_DENIED("권한이 없습니다.", "T_002"),
	INVALID_CATEGORY("카테고리 값이 잘못되었습니다.", "T_003");
	private final String message;
	private final String code;
}

