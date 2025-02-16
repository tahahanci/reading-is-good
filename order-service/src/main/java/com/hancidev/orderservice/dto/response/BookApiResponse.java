package com.hancidev.orderservice.dto.response;

public record BookApiResponse(String bookID, String bookName, int publishYear, boolean inStock,
                              String authorName, double price) {
}
