package org.pwc.todo.share.dto;

public record ShareResponse(
	long shareId,
	long ownerId,
	long viewerId,
	String viewerNickname
) {
}
