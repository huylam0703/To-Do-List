package com.web.todolist.service.impl;

import com.web.todolist.dto.request.TodoCreateRequest;
import com.web.todolist.dto.request.TodoUpdateRequest;
import com.web.todolist.dto.response.PageResponse;
import com.web.todolist.dto.response.TodoResponse;
import com.web.todolist.entity.Todo;
import com.web.todolist.exception.AppException;
import com.web.todolist.exception.ErrorCode;
import com.web.todolist.mapper.TodoMapper;
import com.web.todolist.repository.TodoRepository;
import com.web.todolist.service.TodoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TodoServiceImpl implements TodoService {
    TodoRepository todoRepository;
    TodoMapper todoMapper;

    @Override
    public TodoResponse createTodo(TodoCreateRequest request) {
        if(todoRepository.existsByTitle(request.getTitle())){
            throw new AppException(ErrorCode.TITLE_EXISTS);
        }
        Todo todo = todoMapper.toTodo(request);

        Todo saveTodo = todoRepository.save(todo);

        return todoMapper.toTodoResponse(saveTodo);
    }

    @Override
    public PageResponse<TodoResponse> getTodo(String keyword, String status, int pageNo, int pageSize) {
        if (pageNo < 0) {
            pageNo = 1;
        }

        if (pageSize <= 0) {
            pageSize = 5;
        }

        String searchKeyword = normalizeKeyword(keyword);
        Boolean completed = parseStatus(status);

        int pageIndex = pageNo - 1;

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Todo> todoPage = todoRepository.searchTodos(searchKeyword, completed, pageable);

        List<TodoResponse> todoResponses = todoPage.getContent()
                .stream()
                .map(todoMapper::toTodoResponse)
                .toList();

        return PageResponse.<TodoResponse>builder()
                .pageNo(todoPage.getNumber() + 1)
                .pageSize(todoPage.getSize())
                .totalPages(todoPage.getTotalPages())
                .totalElements(todoPage.getTotalElements())
                .content(todoResponses)
                .build();
    }

    @Override
    public TodoResponse getTodoById(String todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new AppException(ErrorCode.TODO_NOT_FOUND));
        return todoMapper.toTodoResponse(todo);
    }

    @Override
    public TodoResponse updateTodo(TodoUpdateRequest request, String todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new AppException(ErrorCode.TODO_NOT_FOUND));

        if(todoRepository.existsByTitle(request.getTitle())){
            throw new AppException(ErrorCode.TITLE_EXISTS);
        }

        todoMapper.updateTodo(todo, request);

        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.toTodoResponse(savedTodo);
    }

    @Override
    public void deleteTodo(String todoId) {
        todoRepository.deleteById(todoId);
    }

    @Override
    public TodoResponse toggleTodoStatus(String todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new AppException(ErrorCode.TODO_NOT_FOUND));

        todo.setCompleted(!todo.isCompleted());

        Todo savedTodo = todoRepository.save(todo);

        return todoMapper.toTodoResponse(savedTodo);
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        return keyword.trim();
    }

    private Boolean parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }

        return switch (status.toLowerCase()) {
            case "all" -> null;
            case "completed" -> true;
            case "pending" -> false;
            default -> throw new IllegalArgumentException("Invalid status. Status must be all, completed or pending");
        };
    }

}
