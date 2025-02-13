package com.hancidev.bookservice.dto;

import lombok.Builder;

@Builder
public record BookRequest(String bookName, String author, int stockAmount, double price, int publishYear) {
}
