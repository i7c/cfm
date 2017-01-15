package org.rliz.cfm.common.api.dto;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Represents a list of other resources.
 */
public class ListDto<T extends AbstractDto> extends ResourceSupport {

    private List<T> elements;

    public ListDto(List<T> elements) {
        this.elements = elements;
    }

    public List<T> getElements() {
        return elements;
    }
}
