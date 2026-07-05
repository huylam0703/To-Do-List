package com.web.todolist.service;

import com.web.todolist.dto.request.TodoCreateRequest;
import com.web.todolist.dto.request.TodoUpdateRequest;
import com.web.todolist.dto.response.PageResponse;
import com.web.todolist.dto.response.TodoResponse;

public interface TodoService {
    TodoResponse createTodo(TodoCreateRequest request);

    PageResponse<TodoResponse> getTodo(String keyword, String status, int pageNo, int pageSize);

    TodoResponse getTodoById(String todoId);

    TodoResponse updateTodo(TodoUpdateRequest request, String todoId);

    void deleteTodo(String todoId);

    TodoResponse toggleTodoStatus(String todoId);
}
