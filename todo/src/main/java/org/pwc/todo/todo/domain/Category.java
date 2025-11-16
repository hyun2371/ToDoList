package org.pwc.todo.todo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	STUDY("공부"),
	WORK("업무"),
	HOME("집안일"),
	EXERCISE("운동"),
	HOBBY("취미"),
	ETC("기타");

	private final String label;
}
