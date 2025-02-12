package com.hancidev.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookID;
    private String bookName;
    private Integer pageNumber;
    private Integer stockAmount;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
