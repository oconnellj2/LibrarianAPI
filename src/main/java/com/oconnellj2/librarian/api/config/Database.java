package com.oconnellj2.librarian.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oconnellj2.librarian.api.entity.Book;
import com.oconnellj2.librarian.api.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class Database {
	@Bean
	CommandLineRunner initDataBase(BookRepository repo) {
		return args -> {
			log.info("Preloading " + repo.save(new Book("The Hobbit", "J.R.R. Tolkien")));
			log.info("Preloading " + repo.save(new Book("Dune", "Frank Herbert")));
			log.info("Preloading " + repo.save(new Book("Harry Potter", "J.K. Rowling")));
		};
	}
}
