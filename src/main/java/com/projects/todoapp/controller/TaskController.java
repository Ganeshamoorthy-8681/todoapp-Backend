package com.projects.todoapp.controller;

import com.projects.todoapp.DTO.*;
import com.projects.todoapp.enums.SortOrder;
import com.projects.todoapp.enums.TaskStatus;
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
@RequestMapping("/api/task/")
@CrossOrigin( exposedHeaders = "Content-Range")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping("create")
    public ResponseEntity<TaskResponseDto> create(@RequestBody @Valid TaskRequestDto taskRequestDto){
        TaskResponseDto taskResponseDto = this.taskService.createTask(taskRequestDto);
        return  new ResponseEntity<TaskResponseDto>(taskResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("countByStatus")
    public ResponseEntity<TaskCountByStatusResponseDto> CountByStatus() {
        TaskCountByStatusResponseDto taskCountByStatus = this.taskService.countByStatus();
        return new ResponseEntity<TaskCountByStatusResponseDto>(taskCountByStatus, HttpStatus.OK);
    }
    @GetMapping("list")
    public List<TaskResponseDto> getTasks(
            @RequestHeader(value = "range", required = false ) String paginationRange,
            @RequestParam(defaultValue = "ASC",required = false) SortOrder sortOrder,
            @RequestParam(defaultValue = "id",required = false) String sortBy,
            @RequestParam(required = false) TaskStatus taskStatus,
            @RequestParam(required = false) boolean isTodayDue,
            HttpServletResponse response

    ) {
        Page<TaskResponseDto> paginatedResponse = this.taskService.getTaskList(paginationRange, sortBy,sortOrder,taskStatus,isTodayDue);
        String paginationString = PaginationUtil.getPaginationString( paginatedResponse.getSize(),  paginatedResponse.getNumber(), paginatedResponse.getNumberOfElements(),  paginatedResponse.getTotalElements());
        response.setHeader("Content-Range",paginationString);
        return  paginatedResponse.getContent();
    }

    @PutMapping("edit")
    public TaskResponseDto updateTask(@RequestBody @Valid TaskUpdateRequestDTO taskUpdateRequestDTO ){
            return this.taskService.updateTask(taskUpdateRequestDTO);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteTask( @PathVariable @NotNull int id){
        String deleteData = this.taskService.deleteTask(id);
        if(deleteData.equals("DELETE SUCCESS")){
            return new ResponseEntity<>(deleteData,HttpStatus.OK);
        }
        return new ResponseEntity<>(deleteData, HttpStatus.NOT_FOUND) ;
    }
}
