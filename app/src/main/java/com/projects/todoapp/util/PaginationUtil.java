package com.projects.todoapp.util;

public class PaginationUtil {

    public static  String getPaginationString(int pageSize, int pageNumber, int totalData, long totalRecords) {
        int startIndex = (pageNumber) * pageSize + 1;
        int endIndex = (int) Math.min((long) totalData * (pageNumber + 1), totalRecords);
        return String.format("items %d-%d/%d",startIndex,endIndex,totalRecords);
    }

}
