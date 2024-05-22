package com.oconnellj2.librarian.api.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconnellj2.librarian.api.entity.Book;
import com.oconnellj2.librarian.api.service.BookService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private BookService bookService;

	private Book book1;
	private Book book2;
	private static final Long ID = 1L;
	private static final String TITLE = "1984";
	private static final String AUTHOR = "George Orwell";

	@BeforeEach
	public void setUp() {
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
	public void testGetAllBooksExists() throws Exception {
		// Given/When/Then.
		mockMvc.perform(get("/books"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAllBooksReturnsEmptyList() throws Exception {
		// Given/When.
		Mockito.when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
		// Then.
		mockMvc.perform(get("/books"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void testGetAllBooksReturnsListOfBooks() throws Exception {
		// Given.
		List<Book> books = Arrays.asList(book1, book2);
		// When.
		Mockito.when(bookService.getAllBooks()).thenReturn(books);
		// Then.
		mockMvc.perform(get("/books"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].title", is(TITLE)))
				.andExpect(jsonPath("$[0].author", is(AUTHOR)))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].title", is("To Kill a Mockingbird")))
				.andExpect(jsonPath("$[1].author", is("Harper Lee")))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testGetBook() throws Exception {
		// Given/When.
		when(bookService.getBook(ID)).thenReturn(book1);
		// Then.
		mockMvc.perform(get("/books/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ID))
				.andExpect(jsonPath("$.title").value(TITLE))
				.andExpect(jsonPath("$.author").value(AUTHOR));
	}

	@Test
	public void testGetBookByNonExistingId() throws Exception {
		// Given.
		Long invalidId = 999L;
		// When.
		when(bookService.getBook(invalidId)).thenReturn(null);
		// Then.
		mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", invalidId))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testGetBookByInvalidId() throws Exception {
		// Given
		String invalidId = "abc";
		// When/Then
		mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", invalidId))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Invalid ID supplied"));
	}

	@Test
	public void testAddBook() throws Exception {
		// Given/When.
		when(bookService.addBook(any(Book.class))).thenReturn(ID.toString());
		// Then
		mockMvc.perform(MockMvcRequestBuilders.post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book1)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/books/" + ID))
				.andExpect(MockMvcResultMatchers.content().string("book created successfully"));
	}

	@Test
	public void testUpdateBook() throws Exception {
		// When.
		doReturn(book1).when(bookService).getBook(ID);
		doNothing().when(bookService).updateBook(book1);
		// Then.
		mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book2)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("book updated successfully"));
	}

	@Test
	public void testUpdateBookWithNoAuthor() throws Exception {
		// Given.
		book2.setAuthor(null);
		// When.
		doReturn(book1).when(bookService).getBook(ID);
		doNothing().when(bookService).updateBook(book1);
		// Then.
		mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book2)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("book updated successfully"));
	}

	@Test
	public void testUpdateBookWithNoTitle() throws Exception {
		// Given.
		book2.setTitle(null);
		// When.
		doReturn(book1).when(bookService).getBook(ID);
		doNothing().when(bookService).updateBook(book1);
		// Then.
		mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book2)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("book updated successfully"));
	}

	@Test
	public void testUpdateBookNotFound() throws Exception {
		// Given/When.
		doReturn(null).when(bookService).getBook(ID);
		// Then.
		mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book2)))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testDeleteExistingBook() throws Exception {
		// Given/When.
		when(bookService.getBook(ID)).thenReturn(book1);
		doNothing().when(bookService).deleteBook(ID);
		// THen.
		mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteNonExistingBook() throws Exception {
		// Given/When.
		when(bookService.getBook(ID)).thenReturn(null);
		// Then.
		mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
