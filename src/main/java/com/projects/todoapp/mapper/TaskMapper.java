package com.projects.todoapp.mapper;

import com.projects.todoapp.DTO.TaskCountByStatusResponseDto;
import com.projects.todoapp.DTO.TaskRequestDto;
import com.projects.todoapp.DTO.TaskResponseDto;
import com.projects.todoapp.DTO.TaskUpdateRequestDTO;
import com.projects.todoapp.entity.Task;
import com.projects.todoapp.entity.TaskCountByStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static Task convertTaskRequestDtoToTaskEntity(TaskRequestDto taskRequestDto){
        Task task = new Task();
        task.setTaskStatus( taskRequestDto.getTaskStatus());
        task.setTaskName(taskRequestDto.getTaskName());
        task.setTaskDescription(taskRequestDto.getTaskDescription());
        task.setDueDate(taskRequestDto.getDueDate());
        return task;
    }

    public static TaskResponseDto convertTaskToTaskResponseDto(Task task){
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setTaskStatus(task.getTaskStatus());
        taskResponseDto.setTaskDescription(task.getTaskDescription());
        taskResponseDto.setId(task.getId());
        taskResponseDto.setUpdatedOn(task.getUpdatedOn());
        taskResponseDto.setCreatedOn(task.getCreatedOn());
        taskResponseDto.setTaskName(task.getTaskName());
        return taskResponseDto;
    }

    public static TaskCountByStatusResponseDto convertTaskCountByStatusToResponseDto(List<TaskCountByStatus> taskCountByStatus){
        TaskCountByStatusResponseDto taskCountByStatusResponseDto = new TaskCountByStatusResponseDto();

        for (TaskCountByStatus taskStatus : taskCountByStatus) {
            switch (taskStatus.getStatus()) {
                case COMPLETED:
                    taskCountByStatusResponseDto.setCompletedStatusCount(taskStatus.getCount());
                    break;
                case IN_PROGRESS:
                    taskCountByStatusResponseDto.setInProgressStatusCount(taskStatus.getCount());
                    break;
                case NOT_READY:
                    taskCountByStatusResponseDto.setNotReadyStatusCount(taskStatus.getCount());
                    break;
            }
        }
    return taskCountByStatusResponseDto;
    }

    public static Page<TaskResponseDto> convertTaskListToResponseDTO(Page<Task> taskPage) {
        Sort.Order updatedOnSortOrder = taskPage.getSort().getOrderFor("updatedOn");
        // Filter tasks and map them to DTOs
        List<Task> filteredTasks = taskPage.getContent().stream()
                    .filter(task ->{
                        boolean isUpdatedOnSorted = updatedOnSortOrder != null;
                        return !isUpdatedOnSorted || task.getUpdatedOn() > 0;
                    } )
                   .toList();

        // Map each task to TaskListResponseDto and return a Page
        List<TaskResponseDto> dtoList = filteredTasks.stream()
                .map(task -> {
                    TaskResponseDto dto = new TaskResponseDto();
                    dto.setTaskName(task.getTaskName());
                    dto.setTaskStatus(task.getTaskStatus());
                    dto.setTaskDescription(task.getTaskDescription());
                    dto.setDueDate(task.getDueDate());
                    dto.setCreatedOn(task.getCreatedOn());
                    dto.setId(task.getId());
                    dto.setUpdatedOn(task.getUpdatedOn());
                    return dto;
                }).collect(Collectors.toList());

        // Recreate the Page using the filtered tasks and original Pageable information
        return new PageImpl<>(dtoList, taskPage.getPageable(), updatedOnSortOrder!=null ? dtoList.size(): taskPage.getTotalElements());
    }

    public static Task convertTaskUpdateRequestDtoToTask(TaskUpdateRequestDTO taskUpdateRequestDTO){
        Task task = new Task();
        task.setId(taskUpdateRequestDTO.getId());
        task.setTaskName(taskUpdateRequestDTO.getTaskName());
        task.setTaskStatus(taskUpdateRequestDTO.getTaskStatus());
        task.setTaskDescription(taskUpdateRequestDTO.getTaskDescription());
        task.setDueDate(taskUpdateRequestDTO.getDueDate());
        task.setCreatedOn(taskUpdateRequestDTO.getCreatedOn());
        return task;
    }


}
