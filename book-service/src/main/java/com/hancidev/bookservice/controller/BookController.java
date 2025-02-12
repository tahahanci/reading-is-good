package com.hancidev.bookservice.controller;

import com.hancidev.bookservice.dto.BookDetailResponse;
import com.hancidev.bookservice.dto.BookRequest;
import com.hancidev.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/save")
    public ResponseEntity<BookDetailResponse> saveBook(@RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.createBook(bookRequest));
    }

    @GetMapping("/find/{bookID}")
    public ResponseEntity<BookDetailResponse> findBook(@PathVariable String bookID) {
        return ResponseEntity.ok(bookService.findBook(bookID));
    }
}
