package org.pwc.todo.share;

import org.pwc.todo.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShareErrorCode implements ErrorCode {
	NOT_FOUND_SHARE("해당 아이디의 공유가 존재하지 않습니다.", "S_001"),
	PERMISSION_DENIED("권한이 없습니다.", "S_002"),
	ALREADY_EXIST("권한이 없습니다.", "S_003");
	private final String message;
	private final String code;
}
