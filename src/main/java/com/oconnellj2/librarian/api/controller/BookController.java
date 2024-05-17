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
import com.oconnellj2.librarian.api.exception.BookNotFoundException;
import com.oconnellj2.librarian.api.service.BookService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Books", description = "Endpoints related to managing books.")
@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
	private final BookService bookService;

	@Operation(summary = "Get all books in the library.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all books.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = List[].class)) })
	})
	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {
		return ResponseEntity.ok(bookService.getAllBooks());
	}

	@Operation(summary = "Get a book by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Book found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Book not found") })
	@GetMapping("/{id}")
	public ResponseEntity<Book> getbook(
			@Parameter(description = "ID of the book to be retrieved.", required = true) @PathVariable Long id) {
		Book book = bookService.getBook(id);
		if (book == null) {
			throw new BookNotFoundException("Book not found with ID: " + id);
		}
		return ResponseEntity.ok(book);
	}

	@Operation(summary = "Add a new book to the library.", description = "Create a new book record in the library.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Book created successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
	})
	@PostMapping
	public ResponseEntity<String> addbook(
			@Parameter(description = "Book object to be created", required = true) @RequestBody @Validated Book book) {
		String bookId = bookService.addBook(book);
		return ResponseEntity.created(URI.create("/books/" + bookId)).body("book created successfully");
	}

	@Operation(summary = "Update an existing book by its ID.", description = "Update the details of an existing book in the library using its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Book updated successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
			@ApiResponse(responseCode = "404", description = "Book not found", content = @Content)

	})
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

	@Operation(summary = "Delete a book by its ID.", description = "Delete an existing book from the library using its unique identifier.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Book deleted successfully."),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Book not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBook(
			@Parameter(description = "ID of the book to be deleted", required = true) @PathVariable Long id) {
		if (bookService.getBook(id) == null) {
			throw new BookNotFoundException("Book not found with ID: " + id);
		}
		bookService.deleteBook(id);
		return ResponseEntity.ok("Book deleted successfully");
	}
}