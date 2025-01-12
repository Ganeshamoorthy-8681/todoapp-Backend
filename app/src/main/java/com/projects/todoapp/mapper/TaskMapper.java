package com.projects.todoapp.mapper;

import com.projects.todoapp.DTO.TaskCountByStatusResponseDto;
import com.projects.todoapp.DTO.TaskRequestDto;
import com.projects.todoapp.DTO.TaskResponseDto;
import com.projects.todoapp.DTO.TaskUpdateRequestDTO;
import com.projects.todoapp.entity.Task;
import com.projects.todoapp.entity.User;
import com.projects.todoapp.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static Task convertTaskRequestDtoToTaskEntity(TaskRequestDto taskRequestDto, User user){
        Task task = new Task();
        task.setTaskStatus( taskRequestDto.getTaskStatus());
        task.setTaskName(taskRequestDto.getTaskName());
        task.setTaskDescription(taskRequestDto.getTaskDescription());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setCategory(taskRequestDto.getTaskCategory());
        task.setPriority(taskRequestDto.getTaskPriority());
        task.setUser(user);
        System.out.println(user.getEmail());
        return task;
    }

    public static TaskResponseDto convertTaskToTaskResponseDto(Task task){
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setTaskStatus(task.getTaskStatus());
        taskResponseDto.setTaskDescription(task.getTaskDescription());
        taskResponseDto.setId(task.getId());
        taskResponseDto.setDueDate(task.getDueDate());
        taskResponseDto.setUpdatedOn(task.getUpdatedOn());
        taskResponseDto.setCreatedOn(task.getCreatedOn());
        taskResponseDto.setTaskName(task.getTaskName());
        taskResponseDto.setTaskPriority(task.getPriority());
        taskResponseDto.setTaskCategory(task.getCategory());

        return taskResponseDto;
    }

    public static TaskCountByStatusResponseDto getTaskCountByStatus(List<Task> taskList) {
        TaskCountByStatusResponseDto taskCountByStatusResponseDto = new TaskCountByStatusResponseDto();
        int completedCount = 0;
        int inProgressCount = 0;
        int overDueCount = 0;
        int upComingCount = 0;

        for (Task task : taskList) {
            TaskStatus status = task.getTaskStatus();

            if (status == TaskStatus.COMPLETED) {
                completedCount++;
                continue;
            }

            long dueDate = task.getDueDate();
            if (isOverDueTask(dueDate)) {
                overDueCount++;
            } else if (status == TaskStatus.IN_PROGRESS) {
                inProgressCount++;
            } else {
                upComingCount++;
            }
        }

        // Set counts in the response DTO
        taskCountByStatusResponseDto.setCompletedTaskCount(completedCount);
        taskCountByStatusResponseDto.setTotalTaskCount(taskList.size());
        taskCountByStatusResponseDto.setInProgressTaskCount(inProgressCount);
        taskCountByStatusResponseDto.setOverDueTaskCount(overDueCount);
        taskCountByStatusResponseDto.setUpComingTaskCount(upComingCount);
        return taskCountByStatusResponseDto;
    }

    private static boolean isOverDueTask(long dueDate) {
       long currentDate = Instant.now().toEpochMilli();
       return currentDate >= dueDate ;
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
                .map(TaskMapper::convertTaskTOTaskResponseDTO).collect(Collectors.toList());

        // Recreate the Page using the filtered tasks and original Pageable information
        return new PageImpl<>(dtoList, taskPage.getPageable(), updatedOnSortOrder!=null ? dtoList.size(): taskPage.getTotalElements());
    }

    public static Task convertTaskUpdateRequestDtoToTask(TaskUpdateRequestDTO taskUpdateRequestDTO, User user){
        Task task = new Task();
        task.setId(taskUpdateRequestDTO.getId());
        task.setTaskName(taskUpdateRequestDTO.getTaskName());
        task.setTaskStatus(taskUpdateRequestDTO.getTaskStatus());
        task.setTaskDescription(taskUpdateRequestDTO.getTaskDescription());
        task.setDueDate(taskUpdateRequestDTO.getDueDate());
        task.setCreatedOn(taskUpdateRequestDTO.getCreatedOn());
        task.setPriority(taskUpdateRequestDTO.getTaskPriority());
        task.setCategory(taskUpdateRequestDTO.getTaskCategory());
        task.setUser(user);
        return task;
    }

    public static TaskResponseDto convertTaskTOTaskResponseDTO(Task task){
        TaskResponseDto dto = new TaskResponseDto();
        dto.setTaskName(task.getTaskName());
        dto.setTaskStatus(task.getTaskStatus());
        dto.setTaskDescription(task.getTaskDescription());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedOn(task.getCreatedOn());
        dto.setId(task.getId());
        dto.setUpdatedOn(task.getUpdatedOn());
        dto.setTaskCategory(task.getCategory());
        dto.setTaskPriority(task.getPriority());
        return dto;
    }



}
