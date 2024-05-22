package com.oconnellj2.librarian.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Schema(description = "Entity representing a book in the library.")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Unique identifier of the book.", example = "1")
	private Long id;

	@NotBlank(message = "Title is required")
	@Size(max = 255)
	@Schema(description = "Title of the book.", example = "The Great Gatsby", required = true)
	private String title;

	@NotBlank(message = "Author is required")
	@Size(max = 255)
	@Schema(description = "Author of the book.", example = "F. Scott Fitzgerald", required = true)
	private String author;

	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}
}
