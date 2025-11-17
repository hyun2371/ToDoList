package org.pwc.todo.user;

import org.pwc.todo.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	NOT_FOUND_USER("해당 아이디의 회원이 존재하지 않습니다.", "U_001");
	private final String message;
	private final String code;
}
