package com.web.todolist.mapper;

import com.web.todolist.dto.request.TodoCreateRequest;
import com.web.todolist.dto.request.TodoUpdateRequest;
import com.web.todolist.dto.response.TodoResponse;
import com.web.todolist.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completed", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Todo toTodo(TodoCreateRequest request);

    TodoResponse toTodoResponse(Todo todo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateTodo(@MappingTarget Todo todo, TodoUpdateRequest request);
}
