package com.oconnellj2.librarian.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.oconnellj2.librarian.api.entity.Book;
import com.oconnellj2.librarian.api.service.BookService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
	private final BookService bookService;

	@GetMapping
	public ResponseEntity<List<Book>> getAllbooks() {
		return ResponseEntity.ok(bookService.getAllBooks());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getbook(@PathVariable Long id) {
		return ResponseEntity.ok(bookService.getBook(id));
	}

	@PostMapping
	public ResponseEntity<String> addbook(@RequestBody @Validated Book book) {
		String bookId = bookService.addBook(book);
		return ResponseEntity.created(URI.create("/books/" + bookId)).body("book created successfully");
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody @Validated Book book) {
		Book existingbook = bookService.getBook(id);
		if (existingbook == null) {
			return ResponseEntity.notFound().build();
		}
		if (book.getTitle() != null) {
			existingbook.setTitle(book.getTitle());
		}
		if (book.getAuthor() != null) {
			existingbook.setAuthor(book.getAuthor());
		}
		bookService.updateBook(existingbook);

		return ResponseEntity.ok("book updated successfully");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletebook(@PathVariable Long id) {
		if (bookService.getBook(id) == null) {
			return ResponseEntity.notFound().build();
		}
		bookService.deleteBook(id);

		return ResponseEntity.ok("book deleted successfully");
	}
}