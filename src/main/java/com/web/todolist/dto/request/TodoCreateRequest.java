package com.web.todolist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoCreateRequest {
    @NotBlank(message = "TITLE_REQUIRED")
    @Size(max = 100, message = "TITLE_INVALID")
    private String title;

    @Size(max = 500, message = "DESCRIPTION_INVALID")
    private String description;
}
