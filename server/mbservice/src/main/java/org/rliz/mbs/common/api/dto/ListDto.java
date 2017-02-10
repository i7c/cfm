package org.rliz.mbs.common.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Represents a list of other resources.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListDto<T extends AbstractDto> extends ResourceSupport {

    private final Integer pageNumber;
    private final Integer totalPages;
    private final Long totalElements;
    private final Integer pageSize;
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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
