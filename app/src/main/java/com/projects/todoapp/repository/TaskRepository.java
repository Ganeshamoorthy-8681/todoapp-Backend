package com.projects.todoapp.repository;

import com.projects.todoapp.entity.Task;
import com.projects.todoapp.entity.TaskCountByStatus;
import com.projects.todoapp.enums.TaskCategory;
import com.projects.todoapp.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT COUNT(*), status from task  GROUP BY status", nativeQuery = true)
    List<TaskCountByStatus> countByStatus();


    Page<Task> findByTaskStatus(TaskStatus status, Pageable pageable);

    @Query(value = "SELECT t FROM task t WHERE t.dueDate BETWEEN :startDate AND :dueDate")
    Page<Task> findByStartAndEndDates(@Param("startDate") long startDate, @Param("dueDate") long dueDate, Pageable pageable);

    @Query("SELECT t FROM task t WHERE "
            + "(:category IS NULL OR t.category = :category) AND "
            + "(:status IS NULL OR t.taskStatus = :status) AND "
            + "(:startDate <= 0  OR t.dueDate >= :startDate) AND "
            + "(:dueDate <= 0 OR t.dueDate <= :dueDate) AND "
            + "t.user.userId = :userId"
           )
    Page<Task> getAllTasks(
            @Param("startDate") Long startDate,
            @Param("dueDate") Long dueDate,
            @Param("status") TaskStatus status,
            @Param("category") TaskCategory category,
            @Param("userId") int userId,
            Pageable pageable);

    @Query("SELECT t FROM task t WHERE t.user.userId = :userId AND (LOWER(t.taskName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.taskDescription) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.category) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.priority) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.taskStatus) LIKE LOWER(CONCAT('%', :query, '%')))"
    )
    List<Task>  getTaskBasedOnSearchQuery(@Param("query") String query, @Param("userId") int userId);

    @Query("SELECT t FROM task t WHERE t.user.userId = :userId")
    List<Task> getTaskBasedOnUserId(@Param("userId")int userId);


}
