package com.hancidev.bookservice.service.mapper;

import com.hancidev.bookservice.dto.BookDetailResponse;
import com.hancidev.bookservice.dto.BookRequest;
import com.hancidev.bookservice.entity.Book;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
public class BookMapper {

    public Book bookFromBookRequest(BookRequest from) {
        return Book.builder()
                .bookID(UUID.randomUUID().toString())
                .bookName(from.bookName())
                .author(from.author())
                .publishYear(from.publishYear())
                .stockAmount(from.stockAmount())
                .price(setScale(BigDecimal.valueOf(from.price())))
                .build();
    }

    public BookDetailResponse bookDetailResponseFromBook(Book from) {
        return BookDetailResponse.builder()
                .bookID(from.getBookID())
                .bookName(from.getBookName())
                .publishYear(from.getPublishYear())
                .inStock(from.getStockAmount() > 0)
                .authorName(from.getAuthor())
                .price(from.getPrice().doubleValue())
                .build();
    }

    private BigDecimal setScale(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }
}
