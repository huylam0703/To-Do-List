package com.web.todolist.controller;

import com.web.todolist.dto.request.TodoCreateRequest;
import com.web.todolist.dto.request.TodoUpdateRequest;
import com.web.todolist.dto.response.ApiResponse;
import com.web.todolist.dto.response.PageResponse;
import com.web.todolist.dto.response.TodoResponse;
import com.web.todolist.service.TodoService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TodoController {
    TodoService todoService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TodoResponse>> createTodo(@RequestBody @Valid TodoCreateRequest request){
        log.info("create new todo");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<TodoResponse>builder()
                        .code(1000)
                        .message("create new todo success")
                        .result(todoService.createTodo(request))
                        .build());
    }

    @GetMapping("/getTodo")
    public ResponseEntity<ApiResponse<PageResponse<TodoResponse>>> getTodo(@RequestParam(required = false) String keyword,
                                                                           @RequestParam(required = false) String status,
                                                                           @RequestParam(defaultValue = "1") int pageNo,
                                                                           @RequestParam(defaultValue = "10") int pageSize){
        log.info("Get todo search");

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<PageResponse<TodoResponse>>builder()
                        .code(1000)
                        .message("get todo search success")
                        .result(todoService.getTodo(keyword, status, pageNo, pageSize))
                        .build());
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<ApiResponse<TodoResponse>> getTodoById(@PathVariable String todoId){
        log.info("get todo by id");

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TodoResponse>builder()
                        .code(1000)
                        .message("get todo by id success")
                        .result(todoService.getTodoById(todoId))
                        .build());
    }

    @PutMapping("/update/{todoId}")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodo(@RequestBody @Valid TodoUpdateRequest request,
                                                                @PathVariable String todoId){
        log.info("update todo");

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TodoResponse>builder()
                        .code(1000)
                        .message("update todo success")
                        .result(todoService.updateTodo(request, todoId))
                        .build());
    }

    @DeleteMapping("/delete/{todoId}")
    public ResponseEntity<ApiResponse<String>> deleteTodo(@PathVariable String todoId){
        log.info("delete todo");
        todoService.deleteTodo(todoId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .code(1000)
                        .result("delete todo success")
                        .build());
    }

    @PatchMapping("/status/{todoId}")
    public ResponseEntity<ApiResponse<TodoResponse>> toggleTodoStatus(@PathVariable String todoId){
        log.info("toggle status todo");

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TodoResponse>builder()
                        .code(1000)
                        .message("toggle status success")
                        .result(todoService.toggleTodoStatus(todoId))
                        .build());
    }
}
