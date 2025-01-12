package com.projects.todoapp.paging;

import com.projects.todoapp.enums.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
@AllArgsConstructor
public class Pagination {

    String range;
    String sortBy;
    SortOrder sortOrder;


    public Pageable getPageableData(){
        int pageNumber = 0;
        int pageSize = 10;
        if(range!=null){
            String[] parts = range.split("=");
            if (parts.length > 1) {
                int requestPageStart = Integer.parseInt(parts[1].split("-")[0]);
                int requestPageEnd =   Integer.parseInt(parts[1].split("-")[1]);
                pageSize =(requestPageEnd - requestPageStart) + 1;
                pageNumber = (requestPageStart - 1) / pageSize ;
            }
        }
        Sort sort = sortOrder.equals(SortOrder.ASC) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber,pageSize,sort);
    }
}
