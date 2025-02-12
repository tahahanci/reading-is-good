package com.hancidev.bookservice.service;

import com.hancidev.bookservice.dto.BookDetailResponse;
import com.hancidev.bookservice.dto.BookRequest;

public interface BookService {

    BookDetailResponse createBook(BookRequest bookRequest);

    BookDetailResponse findBook(String bookID);
}
