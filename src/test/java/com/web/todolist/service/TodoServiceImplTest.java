package com.web.todolist.service;

import com.web.todolist.dto.request.TodoCreateRequest;
import com.web.todolist.dto.request.TodoUpdateRequest;
import com.web.todolist.dto.response.TodoResponse;
import com.web.todolist.entity.Todo;
import com.web.todolist.exception.AppException;
import com.web.todolist.mapper.TodoMapper;
import com.web.todolist.repository.TodoRepository;
import com.web.todolist.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo;
    private TodoResponse todoResponse;

    @BeforeEach
    void setUp() {
        todo = Todo.builder()
                .id("todo-1")
                .title("Learn SpringBoot")
                .description("Practice todo API")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        todoResponse = TodoResponse.builder()
                .id("todo-1")
                .title("Learn SpringBoot")
                .description("Practice todo API")
                .completed(false)
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }

    @Test
    void createTodo_success() {
        TodoCreateRequest request = TodoCreateRequest.builder()
                .title("Learn SpringBoot")
                .description("Practice todo API")
                .build();

        when(todoRepository.existsByTitle("Learn SpringBoot")).thenReturn(false);
        when(todoMapper.toTodo(request)).thenReturn(todo);
        when(todoRepository.save(todo)).thenReturn(todo);
        when(todoMapper.toTodoResponse(todo)).thenReturn(todoResponse);

        TodoResponse result = todoService.createTodo(request);

        assertNotNull(result);
        assertEquals("todo-1", result.getId());
        assertEquals("Learn SpringBoot", result.getTitle());
        assertFalse(result.isCompleted());

        verify(todoRepository, times(1)).existsByTitle("Learn SpringBoot");
        verify(todoMapper, times(1)).toTodo(request);
        verify(todoRepository, times(1)).save(todo);
        verify(todoMapper, times(1)).toTodoResponse(todo);
    }

    @Test
    void createTodo_titleExists_throwException() {
        TodoCreateRequest request = TodoCreateRequest.builder()
                .title("Learn SpringBoot")
                .description("Practice todo API")
                .build();

        when(todoRepository.existsByTitle("Learn SpringBoot")).thenReturn(true);

        assertThrows(AppException.class, () -> todoService.createTodo(request));

        verify(todoRepository, times(1)).existsByTitle("Learn SpringBoot");
        verify(todoRepository, never()).save(any(Todo.class));
        verify(todoMapper, never()).toTodo(any(TodoCreateRequest.class));
    }

    @Test
    void getTodoById_success() {
        when(todoRepository.findById("todo-1")).thenReturn(Optional.of(todo));
        when(todoMapper.toTodoResponse(todo)).thenReturn(todoResponse);

        TodoResponse result = todoService.getTodoById("todo-1");

        assertNotNull(result);
        assertEquals("todo-1", result.getId());
        assertEquals("Learn SpringBoot", result.getTitle());

        verify(todoRepository, times(1)).findById("todo-1");
        verify(todoMapper, times(1)).toTodoResponse(todo);
    }

    @Test
    void getTodoById_notFound_throwException() {
        when(todoRepository.findById("todo-1")).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> todoService.getTodoById("todo-1"));

        verify(todoRepository, times(1)).findById("todo-1");
        verify(todoMapper, never()).toTodoResponse(any(Todo.class));
    }

    @Test
    void updateTodo_success() {
        TodoUpdateRequest request = TodoUpdateRequest.builder()
                .title("Learn Docker")
                .description("Run Todo App with Docker")
                .completed(true)
                .build();

        Todo updatedTodo = Todo.builder()
                .id("todo-1")
                .title("Learn Docker")
                .description("Run Todo App with Docker")
                .completed(true)
                .createdAt(todo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        TodoResponse updatedResponse = TodoResponse.builder()
                .id("todo-1")
                .title("Learn Docker")
                .description("Run Todo App with Docker")
                .completed(true)
                .createdAt(updatedTodo.getCreatedAt())
                .updatedAt(updatedTodo.getUpdatedAt())
                .build();

        when(todoRepository.findById("todo-1")).thenReturn(Optional.of(todo));
        doNothing().when(todoMapper).updateTodo(todo, request);
        when(todoRepository.save(todo)).thenReturn(updatedTodo);
        when(todoMapper.toTodoResponse(updatedTodo)).thenReturn(updatedResponse);

        TodoResponse result = todoService.updateTodo(request, "todo-1");

        assertNotNull(result);
        assertEquals("Learn Docker", result.getTitle());
        assertEquals("Run Todo App with Docker", result.getDescription());
        assertTrue(result.isCompleted());

        verify(todoRepository, times(1)).findById("todo-1");
        verify(todoMapper, times(1)).updateTodo(todo, request);
        verify(todoRepository, times(1)).save(todo);
        verify(todoMapper, times(1)).toTodoResponse(updatedTodo);
    }

    @Test
    void toggleTodoStatus_success() {
        Todo toggledTodo = Todo.builder()
                .id("todo-1")
                .title("Learn SpringBoot")
                .description("Practice todo API")
                .completed(true)
                .createdAt(todo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        TodoResponse toggledResponse = TodoResponse.builder()
                .id("todo-1")
                .title("Learn SpringBoot")
                .description("Practice todo API")
                .completed(true)
                .createdAt(toggledTodo.getCreatedAt())
                .updatedAt(toggledTodo.getUpdatedAt())
                .build();

        when(todoRepository.findById("todo-1")).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(toggledTodo);
        when(todoMapper.toTodoResponse(toggledTodo)).thenReturn(toggledResponse);

        TodoResponse result = todoService.toggleTodoStatus("todo-1");

        assertNotNull(result);
        assertTrue(result.isCompleted());

        verify(todoRepository, times(1)).findById("todo-1");
        verify(todoRepository, times(1)).save(todo);
        verify(todoMapper, times(1)).toTodoResponse(toggledTodo);
    }

    @Test
    void deleteTodo_success() {
        todoService.deleteTodo("todo-1");

        verify(todoRepository, times(1)).deleteById("todo-1");
    }
}