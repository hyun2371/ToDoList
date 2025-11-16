package org.pwc.todo.share.dto;

import org.pwc.todo.share.domain.Share;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShareMapper {
	public static ShareResponse toShareResponse(Share share) {
		return new ShareResponse(
			share.getId(),
			share.getOwner().getId(),
			share.getViewer().getId(),
			share.getViewer().getNickname()
		);
	}
}
