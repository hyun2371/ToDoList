package org.pwc.todo.share.controller;

import org.pwc.todo.common.dto.PageResponse;
import org.pwc.todo.share.dto.CreateShareRequest;
import org.pwc.todo.share.dto.ShareResponse;
import org.pwc.todo.share.service.ShareService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShareController {

	private final ShareService shareService;

	@Operation(summary = "공유 생성 API", description = "특정 사용자의 할일을 다른 사용자에게 조회 권한을 공유한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/api/todos/{ownerId}/shares")
	public ShareResponse createShare(
		@PathVariable Long ownerId,
		@RequestBody CreateShareRequest request
	) {
		return shareService.createShare(ownerId, request);
	}

	@Operation(summary = "공유 삭제 API", description = "특정 사용자의 공유 권한을 삭제한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@DeleteMapping("/api/todos/{ownerId}/shares/{shareId}")
	public ResponseEntity<Void> deleteShare(
		@PathVariable Long ownerId,
		@PathVariable Long shareId
	) {
		shareService.deleteShare(ownerId, shareId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "공유한 사용자 목록 조회 API", description = "owner가 누구에게 공유했는지 viewer 목록을 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/todos/{ownerId}/shares/viewers")
	public ResponseEntity<PageResponse<ShareResponse>> getViewers(
		@PathVariable Long ownerId,
		@PageableDefault Pageable pageable
	) {
		PageResponse<ShareResponse> response = shareService.getViewers(ownerId, pageable);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "공유받은 사용자 목록 조회 API", description = "viewer가 어떤 owner로부터 공유받았는지 목록을 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/todos/{viewerId}/shares/owners")
	public ResponseEntity<PageResponse<ShareResponse>> getOwners(
		@PathVariable Long viewerId,
		@PageableDefault Pageable pageable
	) {
		PageResponse<ShareResponse> response = shareService.getOwners(viewerId, pageable);
		return ResponseEntity.ok(response);
	}
}