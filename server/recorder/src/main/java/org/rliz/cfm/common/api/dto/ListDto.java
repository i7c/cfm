package org.rliz.cfm.common.api.dto;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Represents a list of other resources.
 */
public class ListDto<T extends AbstractDto> extends ResourceSupport {

    private final int pageNumber;
    private final int totalPages;
    private final long totalElements;
    private final int pageSize;
    private List<T> elements;

    public ListDto(List<T> elements, Page<?> page) {
        this.elements = elements;
        this.pageNumber = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageSize = page.getSize();
    }

    public List<T> getElements() {
        return elements;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }
}
