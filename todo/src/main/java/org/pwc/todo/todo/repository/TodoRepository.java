package org.pwc.todo.todo.repository;

import java.util.List;

import org.pwc.todo.todo.domain.Todo;
import org.pwc.todo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoQueryRepository {
	long countByUser(User user);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("update Todo t set t.orderIndex = t.orderIndex + 1 "
		+ "where t.user = :user and t.orderIndex between :start and :end")
	void incrementOrderBetween(User user, int start, int end);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("update Todo t set t.orderIndex = t.orderIndex - 1 "
		+ "where t.user = :user and t.orderIndex between :start and :end")
	void decrementOrderBetween(User user, int start, int end);

	List<Todo> findByUserOrderByOrderIndexAsc(User user);
}
