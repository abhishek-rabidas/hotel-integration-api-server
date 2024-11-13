package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> {
    private int pageSize;
    private int pageNumber;
    private int totalPages;
    public List<T> list;
    private long totalCount;
}
