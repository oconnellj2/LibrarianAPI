package com.oconnellj2.librarian.api.service;

import com.oconnellj2.librarian.api.entity.Book;
import com.oconnellj2.librarian.api.exception.BookNotFoundException;
import com.oconnellj2.librarian.api.repository.BookRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
	private BookRepository repo;

	public List<Book> getAllBooks() {
		return repo.findAll();
	}

	public Book getBook(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
	}

	public String addBook(Book book) {
		return String.valueOf(repo.save(new Book(book.getTitle(), book.getAuthor())).getId());
	}

	public void updateBook(Book book) {
		repo.save(book);
	}

	public void deleteBook(Long id) {
		repo.deleteById(id);
	}
}
