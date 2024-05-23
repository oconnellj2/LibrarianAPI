package com.oconnellj2.librarian.api.service;

import com.oconnellj2.librarian.api.entity.Book;
import com.oconnellj2.librarian.api.exception.BookNotFoundException;
import com.oconnellj2.librarian.api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {
	@Mock
	private BookRepository bookRepository;
	@InjectMocks
	private BookService bookService;

	private Book book1;
	private Book book2;
	private static final Long ID = 1L;
	private static final String TITLE = "1984";
	private static final String AUTHOR = "George Orwell";

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		book1 = new Book();
		book1.setId(ID);
		book1.setTitle(TITLE);
		book1.setAuthor(AUTHOR);

		book2 = new Book();
		book2.setId(2L);
		book2.setTitle("To Kill a Mockingbird");
		book2.setAuthor("Harper Lee");
	}

	@Test
	public void testGetAllBooks() {
		// Given.
		List<Book> books = new ArrayList<>();
		books.add(book1);
		books.add(book2);
		when(bookRepository.findAll()).thenReturn(books);
		// When.
		List<Book> result = bookService.getAllBooks();
		// Then.
		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	public void testGetBook() {
		// Given.
		when(bookRepository.findById(ID)).thenReturn(Optional.of(book1));
		// When.
		Book result = bookService.getBook(ID);
		// Then.
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals(TITLE, result.getTitle());
		assertEquals(AUTHOR, result.getAuthor());
	}

	@Test
	public void testGetBookNotFound() {
		// Given/When.
		when(bookRepository.findById(ID)).thenReturn(Optional.empty());
		// Then.
		assertThrows(BookNotFoundException.class, () -> bookService.getBook(ID));
	}

	@Test
	public void testAddBook() {
		// Given.
		when(bookRepository.save(any())).thenReturn(book1);
		// When.
		String result = bookService.addBook(book1);
		// Then.
		assertNotNull(result);
		assertEquals("1", result);
	}

	@Test
	public void testUpdateBook() {
		// Given/When.
		bookService.updateBook(book1);
		// Then.
		verify(bookRepository, times(1)).save(book1);
	}

	@Test
	public void testDeleteBook() {
		// Given/When.
		bookService.deleteBook(ID);
		// Then.
		verify(bookRepository, times(1)).deleteById(ID);
	}

}
