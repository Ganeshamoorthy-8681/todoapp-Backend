package com.projects.todoapp.service;

import com.projects.todoapp.DTO.*;
import com.projects.todoapp.entity.Task;
import com.projects.todoapp.entity.User;
import com.projects.todoapp.enums.SortOrder;
import com.projects.todoapp.enums.TaskCategory;
import com.projects.todoapp.enums.TaskStatus;
import com.projects.todoapp.exception.TaskNotFoundException;
import com.projects.todoapp.exception.UserNotFoundException;
import com.projects.todoapp.mapper.TaskMapper;
import com.projects.todoapp.paging.Pagination;
import com.projects.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;

    @Autowired private  UserService userService ;

    public TaskResponseDto createTask(TaskRequestDto taskRequestDto, int userId) throws UserNotFoundException {
        User user = userService.getUser(userId);
        Task task = TaskMapper.convertTaskRequestDtoToTaskEntity(taskRequestDto,user);
        Task responseTask = this.taskRepository.save(task);
        return TaskMapper.convertTaskToTaskResponseDto(responseTask) ;
    }

    public TaskCountByStatusResponseDto countByStatus(int userId) {
      List<Task> tasks = this.taskRepository.getTaskBasedOnUserId(userId);
       return TaskMapper.getTaskCountByStatus(tasks);
    }

    public Page<TaskResponseDto> getTaskList (String range, String sortBy, SortOrder sortOrder, TaskStatus status, TaskCategory category, boolean isTodayDue, int userId){

        Pagination pagination = new Pagination(range,sortBy,sortOrder);
        Pageable page = pagination.getPageableData();
        long startDate =0;
        long dueDate =0;
        if(isTodayDue){
            startDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            dueDate =  LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;
        }
        var taskList  = this.taskRepository.getAllTasks(startDate, dueDate, status, category, userId, page);
        return TaskMapper.convertTaskListToResponseDTO(taskList);
    }


    public TaskResponseDto updateTask(TaskUpdateRequestDTO taskUpdateRequestDTO, int userId) throws UserNotFoundException {
        User user = userService.getUser(userId);
        Task task = TaskMapper.convertTaskUpdateRequestDtoToTask(taskUpdateRequestDTO,user);
        return TaskMapper.convertTaskToTaskResponseDto(this.taskRepository.save(task)) ;
    }

    public String deleteTask(int taskId, int userId) throws UserNotFoundException {
        Optional<Task> task  =this.taskRepository.findById(taskId);
        if(task.isPresent()){
            if(task.get().getUser().getUserId() == userId){
                this.taskRepository.deleteById(taskId);
                return "DELETE SUCCESS";
            }
            throw new UserNotFoundException("User not found");
        }
        throw new TaskNotFoundException();
    }

    public TaskResponseDto getTaskById(int taskId) {
      Optional<Task> task =   this.taskRepository.findById(taskId);
        return task.map(TaskMapper::convertTaskToTaskResponseDto).orElse(null);
    }

    public List<TaskResponseDto> getSearchList(String query, int userId){
       List<Task> tasks = this.taskRepository.getTaskBasedOnSearchQuery(query, userId);
        return tasks.stream().map(TaskMapper::convertTaskToTaskResponseDto).toList();
    }

}
