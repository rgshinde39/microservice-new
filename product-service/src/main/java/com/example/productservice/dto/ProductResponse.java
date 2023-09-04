package com.example.productservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
}
