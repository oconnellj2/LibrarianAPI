package com.oconnellj2.librarian.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Book {
	private @Id @GeneratedValue Long id;
	private String title;
	private String author;

	Book() {}

	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}

}
