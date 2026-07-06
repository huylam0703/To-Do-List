package com.web.todolist.repository;

import com.web.todolist.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, String> {
    @Query("""
            SELECT t FROM Todo t
            WHERE 
                (
                    :keyword IS NULL 
                    OR :keyword = '' 
                    OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            AND
                (
                    :completed IS NULL 
                    OR t.completed = :completed
                )
            """)
    Page<Todo> searchTodos(
            @Param("keyword") String keyword,
            @Param("completed") Boolean completed,
            Pageable pageable
    );

    Boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, String id);
}
