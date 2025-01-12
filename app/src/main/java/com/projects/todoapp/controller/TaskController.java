package com.projects.todoapp.controller;

import com.projects.todoapp.DTO.*;
import com.projects.todoapp.enums.SortOrder;
import com.projects.todoapp.enums.TaskCategory;
import com.projects.todoapp.enums.TaskStatus;
import com.projects.todoapp.exception.UserNotFoundException;
import com.projects.todoapp.repository.UserRepository;
import com.projects.todoapp.service.TaskService;
import com.projects.todoapp.util.PaginationUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/user/{userId}/task/")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService, UserRepository taskRepository) {
        this.taskService = taskService;
    }

    @PostMapping("create")
    public ResponseEntity<TaskResponseDto> create(@RequestBody @Valid TaskRequestDto taskRequestDto, @PathVariable int userId ) throws UserNotFoundException {
        TaskResponseDto taskResponseDto = this.taskService.createTask(taskRequestDto,userId);
        return  new ResponseEntity<TaskResponseDto>(taskResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("countByStatus")
    public ResponseEntity<TaskCountByStatusResponseDto> CountByStatus(@PathVariable int userId) {
        TaskCountByStatusResponseDto taskCountByStatus = this.taskService.countByStatus(userId);
        return new ResponseEntity<TaskCountByStatusResponseDto>(taskCountByStatus, HttpStatus.OK);
    }
    @GetMapping("list")
    public List<TaskResponseDto> getTasks(
            @RequestHeader(value = "range", required = false ) String paginationRange,
            @RequestParam(defaultValue = "ASC",required = false) SortOrder sortOrder,
            @RequestParam(defaultValue = "id",required = false) String sortBy,
            @RequestParam(required = false) TaskStatus taskStatus,
            @RequestParam(required = false) TaskCategory category,
            @RequestParam(required = false) boolean isTodayDue,
            @PathVariable int userId,
            HttpServletResponse response

    ) {
        System.out.println(userId);
        Page<TaskResponseDto> paginatedResponse = this.taskService.getTaskList(paginationRange, sortBy,sortOrder,taskStatus,category,isTodayDue,userId);
        String paginationString = PaginationUtil.getPaginationString( paginatedResponse.getSize(),  paginatedResponse.getNumber(), paginatedResponse.getNumberOfElements(),  paginatedResponse.getTotalElements());
        response.setHeader("Content-Range",paginationString);
        return  paginatedResponse.getContent();
    }

    @PutMapping("edit")
    public TaskResponseDto updateTask(@RequestBody @Valid TaskUpdateRequestDTO taskUpdateRequestDTO, @PathVariable int userId ) throws UserNotFoundException {
        return this.taskService.updateTask(taskUpdateRequestDTO,userId);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteTask( @PathVariable @NotNull int id, @PathVariable int userId) throws UserNotFoundException {
        String deleteData = this.taskService.deleteTask(id,userId);
        if(deleteData.equals("DELETE SUCCESS")){
            return new ResponseEntity<>(deleteData,HttpStatus.OK);
        }
        return new ResponseEntity<>(deleteData, HttpStatus.NOT_FOUND) ;
    }

    @GetMapping("{id}")
    public TaskResponseDto getTaskById( @PathVariable @NotNull int id){
       return this.taskService.getTaskById(id);
    }

    @GetMapping("search")
    public List<TaskResponseDto> getTaskBasedOnSearchQuery( @RequestParam String query, @PathVariable int userId) {
        return this.taskService.getSearchList(query,userId);
    }

}
