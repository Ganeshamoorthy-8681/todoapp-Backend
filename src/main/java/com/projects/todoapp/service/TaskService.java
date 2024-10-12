package com.projects.todoapp.service;

import com.projects.todoapp.DTO.*;
import com.projects.todoapp.entity.Task;
import com.projects.todoapp.entity.TaskCountByStatus;
import com.projects.todoapp.enums.SortOrder;
import com.projects.todoapp.enums.TaskStatus;
import com.projects.todoapp.mapper.TaskMapper;
import com.projects.todoapp.paging.Pagination;
import com.projects.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;

    public TaskResponseDto createTask(TaskRequestDto taskRequestDto){
        Task task = TaskMapper.convertTaskRequestDtoToTaskEntity(taskRequestDto);
        Task responseTask = this.taskRepository.save(task);
        return TaskMapper.convertTaskToTaskResponseDto(responseTask) ;
    }


    public TaskCountByStatusResponseDto countByStatus() {
       List<TaskCountByStatus> taskCountByStatus = this.taskRepository.countByStatus();
       return TaskMapper.convertTaskCountByStatusToResponseDto(taskCountByStatus);
    }

    public Page<TaskResponseDto> getTaskList (String range, String sortBy, SortOrder sortOrder, TaskStatus status, boolean isTodayDue){

        Pagination pagination = new Pagination(range,sortBy,sortOrder);
        Pageable page = pagination.getPageableData();
        long startDate =0;
        long dueDate =0;
        if(isTodayDue){
            startDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            dueDate =  LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;
        }
        return TaskMapper.convertTaskListToResponseDTO(this.taskRepository.getAllTasks(startDate,dueDate,status, page));
    }


    public TaskResponseDto updateTask(TaskUpdateRequestDTO taskUpdateRequestDTO){
        Task task = TaskMapper.convertTaskUpdateRequestDtoToTask(taskUpdateRequestDTO);
        return  TaskMapper.convertTaskToTaskResponseDto(this.taskRepository.save(task)) ;
    }

    public String deleteTask(int taskID){
        Optional<Task> task  =this.taskRepository.findById(taskID);
        if(task.isPresent()){
            this.taskRepository.deleteById((int) taskID);
            return "DELETE SUCCESS";
        }
        return "DELETE FAILED";
    }

}
