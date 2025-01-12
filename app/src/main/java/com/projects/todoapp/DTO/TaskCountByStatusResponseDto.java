package com.projects.todoapp.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCountByStatusResponseDto {
//    @JsonProperty(value = "NOT_READY_STATUS")
    private int totalTaskCount;
    private int inProgressTaskCount;
    private int upComingTaskCount;
    private int completedTaskCount;
    private int overDueTaskCount;
}
