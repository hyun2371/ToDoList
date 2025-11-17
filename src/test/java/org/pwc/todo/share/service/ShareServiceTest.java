package org.pwc.todo.share.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.pwc.todo.common.exception.ValidationException;
import org.pwc.todo.share.ShareErrorCode;
import org.pwc.todo.share.domain.Share;
import org.pwc.todo.share.dto.CreateShareRequest;
import org.pwc.todo.share.dto.ShareResponse;
import org.pwc.todo.share.repository.ShareRepository;
import org.pwc.todo.support.IntegrationTestSupport;
import org.pwc.todo.user.UserRepository;
import org.pwc.todo.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

class ShareServiceTest extends IntegrationTestSupport {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ShareRepository shareRepository;

	@Autowired
	ShareService shareService;

	private User viewer, owner;

	@BeforeEach
	void setUp() {
		viewer = userRepository.save(new User("user1@gmail.com", "dddd", "유저1"));
		owner = userRepository.save(new User("user2@gmail.com", "dddd", "유저2"));
	}

	@AfterEach
	void tearDown() {
		shareRepository.deleteAll();
	}

	@Nested
	class createShare {
		@Test
		@DisplayName("소유자가 다른 사용자에게 공유할 수 있다.")
		void createShareSuccess() {
			// given
			CreateShareRequest request = new CreateShareRequest(viewer.getId());

			// when
			shareService.createShare(owner.getId(), request);

			// then
			assertThat(shareRepository.existsByOwnerIdAndViewerId(owner.getId(), viewer.getId()))
				.isTrue();
		}

		@Test
		@DisplayName("이미 존재하는데 생성하면 예외를 반환한다")
		void createShareFail() {
			//given
			shareRepository.save(new Share(owner, viewer));
			CreateShareRequest request = new CreateShareRequest(viewer.getId());
			//when & then
			ValidationException exception = assertThrows(ValidationException.class,
				() -> shareService.createShare(owner.getId(), request));

			assertThat(exception.getMessage())
				.isEqualTo(ShareErrorCode.ALREADY_EXIST.getMessage());
		}
	}

	@Nested
	class DeleteShare {

		@Test
		@DisplayName("소유자는 자신의 공유를 삭제할 수 있다.")
		void deleteShareSuccess() {
			// given
			Share saved = shareRepository.save(new Share(owner, viewer));

			// when
			shareService.deleteShare(owner.getId(), saved.getId());

			// then
			assertThat(shareRepository.existsById(saved.getId())).isFalse();
		}

		@Test
		@DisplayName("소유자가 아닌 사용자가 삭제하면 예외 발생")
		void deleteShareFail_permissionDenied() {
			// given
			Share saved = shareRepository.save(new Share(owner, viewer));
			User otherUser = userRepository.save(new User("other@mail.com", "a", "다른유저"));

			// when
			ValidationException exception = assertThrows(
				ValidationException.class,
				() -> shareService.deleteShare(otherUser.getId(), saved.getId())
			);

			// then
			assertThat(exception.getMessage())
				.isEqualTo(ShareErrorCode.PERMISSION_DENIED.getMessage());
		}
	}

	@Test
	@DisplayName("owner는 자신이 공유한 viewer 목록을 조회할 수 있다.")
	void getViewers() {
		User owner2 = userRepository.save(new User("owner2@mail.com", "pw", "오너2"));
		User viewer2 = userRepository.save(new User("v2@mail.com", "pw", "뷰어2"));
		shareRepository.saveAll(List.of(
			new Share(owner, viewer),
			new Share(owner, viewer2),
			new Share(owner2, viewer2)
		));

		List<ShareResponse> response = shareService.getViewers(owner.getId(), pageRequest).content();
		Assertions.assertAll(
			() -> assertThat(response).hasSize(2),
			() -> assertThat(response)
				.extracting(ShareResponse::viewerId)
				.containsExactlyInAnyOrder(viewer.getId(), viewer2.getId())
		);
	}

	@Test
	@DisplayName("viewer는 자신에게 공유한 owner 목록을 조회할 수 있다.")
	void getOwners() {
		// given
		User owner2 = userRepository.save(new User("owner2@mail.com", "pw", "오너2"));
		User viewer2 = userRepository.save(new User("v2@mail.com", "pw", "뷰어2"));
		shareRepository.saveAll(List.of(
			new Share(owner, viewer),
			new Share(owner2, viewer),
			new Share(owner2, viewer2)
		));

		List<ShareResponse> response = shareService.getOwners(viewer.getId(), pageRequest).content();
		Assertions.assertAll(
			() -> assertThat(response).hasSize(2),
			() -> assertThat(response)
				.extracting(ShareResponse::ownerId)
				.containsExactlyInAnyOrder(owner.getId(), owner2.getId())
		);
	}
}