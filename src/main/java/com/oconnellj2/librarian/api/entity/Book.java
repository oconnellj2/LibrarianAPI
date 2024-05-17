package com.oconnellj2.librarian.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@Schema(description = "Entity representing a book in the library.")
public class Book {
	@Schema(description = "Unique identifier of the book.", example = "1")
	private @Id @GeneratedValue Long id;
	@Schema(description = "Title of the book.", example = "The Great Gatsby", required = true)
	private String title;
	@Schema(description = "Author of the book.", example = "F. Scott Fitzgerald", required = true)
	private String author;

	Book() {}

	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}

}
