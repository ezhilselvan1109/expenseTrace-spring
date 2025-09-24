package com.expensetrace.app.dto.response.tag;

import lombok.Data;

@Data
public class TagsResponseDto {
    private TagResponseDto tag;
    private Integer transactions=0;
    private Integer scheduledTransactions=0;
}
