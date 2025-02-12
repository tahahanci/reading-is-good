package com.hancidev.bookservice.dto;

import lombok.Builder;

@Builder
public record BookDetailResponse(String bookID, String bookName, int publishYear, boolean inStock, String authorName, double price) {
}
