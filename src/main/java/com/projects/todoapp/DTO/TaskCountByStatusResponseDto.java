package com.projects.todoapp.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCountByStatusResponseDto {
//    @JsonProperty(value = "NOT_READY_STATUS")
    private long notReadyStatusCount;
    private long inProgressStatusCount;
    private long completedStatusCount;
}
