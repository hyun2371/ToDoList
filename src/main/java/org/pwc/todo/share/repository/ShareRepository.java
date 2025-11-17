package org.pwc.todo.share.repository;

import org.pwc.todo.share.domain.Share;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share,Long> {
	boolean existsByOwnerIdAndViewerId(Long ownerId, Long viewerId);

	Slice<Share> findAllByOwnerId(Long ownerId, Pageable pageable);

	Slice<Share> findAllByViewerId(Long viewerId, Pageable pageable);
}
