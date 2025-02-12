package com.hancidev.bookservice.service.impl;

import com.hancidev.bookservice.dto.BookDetailResponse;
import com.hancidev.bookservice.dto.BookRequest;
import com.hancidev.bookservice.entity.Book;
import com.hancidev.bookservice.exception.BookNotExistException;
import com.hancidev.bookservice.repository.BookRepository;
import com.hancidev.bookservice.service.BookService;
import com.hancidev.bookservice.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDetailResponse createBook(BookRequest bookRequest) {
        Book book = bookRepository.save(bookMapper.bookFromBookRequest(bookRequest));
        log.info("Book saved with book id: {}", book.getBookID());
        return bookMapper.bookDetailResponseFromBook(book);
    }

    @Override
    public BookDetailResponse findBook(String bookID) {
        Book book = bookRepository.findByBookID(bookID)
                .orElseThrow(() -> new BookNotExistException("Book not exists with given id"));
        log.info("Book found with id: {}", book.getBookID());
        return bookMapper.bookDetailResponseFromBook(book);
    }
}
