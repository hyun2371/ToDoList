package org.pwc.todo.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pwc.todo.common.dto.PageResponse;
import org.pwc.todo.todo.dto.CreateTodoRequest;
import org.pwc.todo.todo.dto.CreateTodoResponse;
import org.pwc.todo.todo.dto.TodoResponse;
import org.pwc.todo.todo.dto.UpdateTodoRequest;
import org.pwc.todo.todo.repository.SearchCondition;
import org.pwc.todo.todo.service.TodoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "할일 API")
@RestController
@RequiredArgsConstructor
public class TodoController {
	private final TodoService todoService;

	@Operation(summary = "할일 등록 API", description = "할일을 생성한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/api/todos/{userId}")
	public CreateTodoResponse createTodo(
		@PathVariable Long userId,
		@RequestBody @Valid CreateTodoRequest request
	) {
		 return todoService.createTodo(userId, request);
	}

	@Operation(summary = "할일 검색 API", description = "할일을 사용자, 카테고리, 완료여부로 필터링하여 사용자 지정 순서대로 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/todos/{userId}")
	public ResponseEntity<PageResponse<TodoResponse>> searchTodos(
		@PathVariable Long userId,
		SearchCondition condition,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(
			todoService.searchTodos(userId, condition, pageable)
		);
	}

	@Operation(summary = "할일 수정 API", description = "할일의 내용,카테고리,마감일자를 수정한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@PatchMapping("/api/todos/{userId}/{todoId}")
	public ResponseEntity<TodoResponse> updateTodo(
		@PathVariable Long userId,
		@PathVariable Long todoId,
		@RequestBody @Valid UpdateTodoRequest request
	) {
		return ResponseEntity.ok(
			todoService.updateTodo(userId, todoId, request)
		);
	}

	@Operation(summary = "할일 완료 API", description = "할일 완료여부에 체크 표시한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@PatchMapping("/api/todos/{userId}/{todoId}/complete")
	public TodoResponse completeTodo(
		@PathVariable Long userId,
		@PathVariable Long todoId
	) {
		return todoService.completeTodo(userId, todoId);
	}

	@Operation(summary = "할일 미완료 API", description = "할일 완료여부에 언체크 표시한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@PatchMapping("/api/todos/{userId}/{todoId}/uncomplete")
	public TodoResponse uncompleteTodo(
		@PathVariable Long userId,
		@PathVariable Long todoId
	) {
		return todoService.uncompleteTodo(userId, todoId);
	}

	@Operation(summary = "할일 순서변경 API", description = "할일의 순서를 변경한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@PatchMapping("/api/todos/{userId}/{todoId}/order")
	public ResponseEntity<Void> updateOrder(
		@PathVariable Long userId,
		@PathVariable Long todoId,
		@RequestParam int newOrderIndex
	) {
		todoService.updateOrder(userId, todoId, newOrderIndex);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "할일 삭제 API", description = "할일을 soft delete한다")
	@ApiResponse(useReturnTypeSchema = true)
	@DeleteMapping("/api/todos/{userId}/{todoId}")
	public ResponseEntity<Void> deleteTodo(
		@PathVariable Long userId,
		@PathVariable Long todoId
	) {
		todoService.deleteTodo(userId, todoId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "할일 공유 조회 API", description = "공유받은 사용자의 할일을 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/todos/{ownerId}/shares/{viewerId}")
	public ResponseEntity<PageResponse<TodoResponse>> searchSharedTodos(
		@PathVariable Long viewerId,
		@PathVariable Long ownerId,
		SearchCondition condition,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(
			todoService.searchSharedTodos(viewerId, ownerId, condition, pageable)
		);
	}
}
