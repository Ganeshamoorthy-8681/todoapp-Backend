package com.projects.todoapp.repository;

import com.projects.todoapp.DTO.TaskCountByStatusResponseDto;
import com.projects.todoapp.entity.Task;
import com.projects.todoapp.entity.TaskCountByStatus;
import com.projects.todoapp.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT COUNT(*), status from task  GROUP BY status", nativeQuery = true)
    List<TaskCountByStatus> countByStatus();


    Page<Task> findByTaskStatus(TaskStatus status, Pageable pageable);

    @Query(value = "SELECT t FROM task t WHERE t.dueDate BETWEEN :startDate AND :dueDate")
    Page<Task> findByStartAndEndDates(@Param("startDate") long startDate, @Param("dueDate")long dueDate, Pageable pageable);

    @Query("SELECT t FROM task t WHERE "
            + "(:status IS NULL OR t.taskStatus = :status) AND "
            + "(:startDate <= 0  OR t.dueDate >= :startDate) AND "
            + "(:dueDate <= 0 OR t.dueDate <= :dueDate)")
    Page<Task> getAllTasks(
            @Param("startDate") Long startDate,
            @Param("dueDate") Long dueDate,
            @Param("status") TaskStatus status,
            Pageable pageable);



}
