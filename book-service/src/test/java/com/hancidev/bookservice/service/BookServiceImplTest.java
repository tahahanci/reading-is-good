package com.hancidev.bookservice.service;

import com.hancidev.bookservice.dto.BookDetailResponse;
import com.hancidev.bookservice.dto.BookRequest;
import com.hancidev.bookservice.entity.Book;
import com.hancidev.bookservice.exception.BookNotExistException;
import com.hancidev.bookservice.repository.BookRepository;
import com.hancidev.bookservice.service.impl.BookServiceImpl;
import com.hancidev.bookservice.service.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    BookRequest bookRequest;
    Book book;
    BookDetailResponse bookDetailResponse;

    @BeforeEach
    void setUp() {
        bookRequest = BookRequest.builder()
                .bookName("Anna Karenina")
                .author("Tolstoy")
                .price(92.28)
                .publishYear(1877)
                .stockAmount(125)
                .build();

        BigDecimal price = new BigDecimal("92.28").setScale(2, RoundingMode.HALF_EVEN);

        book = Book.builder()
                .bookID("A12345")
                .bookName("Anna Karenina")
                .author("Tolstoy")
                .price(price)
                .publishYear(1877)
                .stockAmount(125)
                .build();

        bookDetailResponse = BookDetailResponse.builder()
                .bookID("A12345")
                .bookName("Anna Karenina")
                .authorName("Tolstoy")
                .price(92.28)
                .publishYear(1877)
                .inStock(true)
                .build();
    }

    @Test
    void shouldReturnBookDetailResponse_IfBookExist() {
        when(bookRepository.findByBookID("A12345")).thenReturn(Optional.of(book));
        when(bookMapper.bookDetailResponseFromBook(book)).thenReturn(bookDetailResponse);

        BookDetailResponse actualResponse = bookService.findBook("A12345");

        assertThat(actualResponse)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookDetailResponse);

        verify(bookRepository, times(1)).findByBookID("A12345");
        verify(bookMapper, times(1)).bookDetailResponseFromBook(book);
    }

    @Test
    void shouldThrowBookNotExistException_IfBookDoesNotExist() {
        when(bookRepository.findByBookID("A12345")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findBook("A12345"))
                .isInstanceOf(BookNotExistException.class);

        verify(bookRepository, times(1)).findByBookID("A12345");
        verify(bookMapper, never()).bookDetailResponseFromBook(any());
    }

    @Test
    void shouldCreateBook_ReturnBookDetailResponse() {
        when(bookMapper.bookFromBookRequest(bookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.bookDetailResponseFromBook(book)).thenReturn(bookDetailResponse);

        BookDetailResponse actualResponse = bookService.createBook(bookRequest);

        assertThat(actualResponse)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookDetailResponse);

        verify(bookMapper, times(1)).bookFromBookRequest(bookRequest);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).bookDetailResponseFromBook(book);
    }
}
