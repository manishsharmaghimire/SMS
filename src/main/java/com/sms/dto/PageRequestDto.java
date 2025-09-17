package com.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Common pagination request DTO that can be used across all list endpoints.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDto {
    // Factory method to create from individual parameters
    public static PageRequestDto of(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortBy = sortParams.length > 0 ? sortParams[0] : DEFAULT_SORT_FIELD;
        String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";
        
        return PageRequestDto.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
    }
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_SORT_FIELD = "id";
    
    private int page = 0;
    private int size = DEFAULT_PAGE_SIZE;
    private String sortBy = DEFAULT_SORT_FIELD;
    private String sortDirection = "asc";
    
    // For backward compatibility
    public PageRequestDto(int page, int size, String sort) {
        this.page = page;
        this.size = size;
        
        String[] sortParams = sort.split(",");
        this.sortBy = sortParams.length > 0 ? sortParams[0] : DEFAULT_SORT_FIELD;
        this.sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";
    }
    
    /**
     * Converts this DTO to Spring's Pageable
     */
    public Pageable toPageable() {
        if (size <= 0) size = DEFAULT_PAGE_SIZE;
        if (size > MAX_PAGE_SIZE) size = MAX_PAGE_SIZE;
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
            
        return PageRequest.of(
            page, 
            size, 
            Sort.by(direction, sortBy)
        );
    }
}
