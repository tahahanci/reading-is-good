package com.hancidev.bookservice.dto;

public record BookRequest(String bookName, String author, int stockAmount, double price, int publishYear) {
}
