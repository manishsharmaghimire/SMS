package com.sms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Generic pagination response DTO that can be used across all list endpoints.
 * @param <T> The type of the content in the page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private String sortBy;
    private String sortDirection;

    /**
     * Creates a PageResponse from a Spring Data Page
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .sortBy(page.getSort().isUnsorted() ? null : 
                       page.getSort().get().findFirst()
                          .map(Sort.Order::getProperty)
                          .orElse(null))
                .sortDirection(page.getSort().isUnsorted() ? null : 
                             page.getSort().get().findFirst()
                                .map(order -> order.getDirection().name().toLowerCase())
                                .orElse(null))
                .build();
    }
}
