package org.pwc.todo.todo.domain;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDate;

import org.pwc.todo.common.TimeBaseEntity;
import org.pwc.todo.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "todo_id")
	private Long id;

	@Column(name = "order_index")
	private int orderIndex;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private Category category;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "content")
	private String content;

	@Column(name = "is_completed")
	private boolean isCompleted = false;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;


	public Todo(int orderIndex, Category category, User user, String content, LocalDate dueDate) {
		this.orderIndex = orderIndex;
		this.category = category;
		this.user = user;
		this.content = content;
		this.dueDate = dueDate;
	}

	//테스트용
	public Todo(long id, int orderIndex, Category category, User user, String content, LocalDate dueDate) {
		this.id = id;
		this.orderIndex = orderIndex;
		this.category = category;
		this.user = user;
		this.content = content;
		this.dueDate = dueDate;
	}

	public void update(String content, Category category, LocalDate dueDate) {
		if (content != null) {
			this.content = content;
		}
		if (category != null) {
			this.category = category;
		}
		if (dueDate != null) {
			this.dueDate = dueDate;
		}
	}

	public void markCompleted(){
		this.isCompleted = true;
	}
	public void markUncompleted(){
		this.isCompleted = false;
	}

	public void markDeleted(){
		this.isDeleted = true;
	}

	public void updateOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	@Override
	public String toString() {
		return "Todo{" +
			"id=" + id +
			", orderIndex=" + orderIndex +
			", category=" + category +
			", userId=" + (user != null ? user.getId() : null) +
			", content='" + content + '\'' +
			", isCompleted=" + isCompleted +
			", dueDate=" + dueDate +
			", isDeleted=" + isDeleted +
			'}';
	}
}
