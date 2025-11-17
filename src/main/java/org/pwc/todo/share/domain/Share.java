package org.pwc.todo.share.domain;

import static jakarta.persistence.FetchType.*;

import org.pwc.todo.common.TimeBaseEntity;
import org.pwc.todo.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"owner_id", "viewer_id"})
	}
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Share extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "share_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "viewer_id", nullable = false)
	private User viewer;

	public Share(User owner, User viewer) {
		this.owner = owner;
		this.viewer = viewer;
	}
}
