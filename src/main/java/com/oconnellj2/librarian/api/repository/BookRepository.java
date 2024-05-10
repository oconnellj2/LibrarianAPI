package com.oconnellj2.librarian.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oconnellj2.librarian.api.entity.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
