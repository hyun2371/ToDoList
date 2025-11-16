package org.pwc.todo.share.service;

import org.pwc.todo.common.dto.PageMapper;
import org.pwc.todo.common.dto.PageResponse;
import org.pwc.todo.common.exception.NotFoundException;
import org.pwc.todo.common.exception.ValidationException;
import org.pwc.todo.share.ShareErrorCode;
import org.pwc.todo.share.domain.Share;
import org.pwc.todo.share.dto.CreateShareRequest;
import org.pwc.todo.share.dto.ShareMapper;
import org.pwc.todo.share.dto.ShareResponse;
import org.pwc.todo.share.repository.ShareRepository;
import org.pwc.todo.user.UserErrorCode;
import org.pwc.todo.user.UserRepository;
import org.pwc.todo.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShareService {

	private final ShareRepository shareRepository;
	private final UserRepository userRepository;

	@Transactional
	public ShareResponse createShare(Long ownerId, CreateShareRequest request) {
		User owner = getUserOrThrow(ownerId);
		User viewer = getUserOrThrow(request.viewerId());
		if (shareRepository.existsByOwnerIdAndViewerId(owner.getId(), viewer.getId())) {
			throw new ValidationException(ShareErrorCode.ALREADY_EXIST);
		}
		Share share = shareRepository.save(new Share(owner, viewer));
		return ShareMapper.toShareResponse(share);
	}

	@Transactional
	public void deleteShare(Long ownerId, Long shareId) {
		User owner = getUserOrThrow(ownerId);
		Share share = getShareOrThrow(shareId);
		if (!share.getOwner().getId().equals(owner.getId())) {
			throw new ValidationException(ShareErrorCode.PERMISSION_DENIED);
		}
		shareRepository.delete(share);
	}

	@Transactional(readOnly = true)
	public PageResponse<ShareResponse> getViewers(Long userId, Pageable pageable) {
		User user = getUserOrThrow(userId);
		Slice<ShareResponse> shareResponsePage = shareRepository
			.findAllByOwnerId(user.getId(), pageable)
			.map(ShareMapper::toShareResponse);
		return PageMapper.toPageResponse(shareResponsePage);
	}

	@Transactional(readOnly = true)
	public PageResponse<ShareResponse> getOwners(Long userId, Pageable pageable) {
		User user = getUserOrThrow(userId);
		Slice<ShareResponse> shareResponsePage = shareRepository
			.findAllByViewerId(user.getId(), pageable)
			.map(ShareMapper::toShareResponse);
		return PageMapper.toPageResponse(shareResponsePage);
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_USER));
	}

	private Share getShareOrThrow(Long shareId) {
		return shareRepository.findById(shareId)
			.orElseThrow(() -> new NotFoundException(ShareErrorCode.NOT_FOUND_SHARE));
	}
}
