package io.bookster.web.rest.dto;

import io.bookster.domain.Book;
import io.bookster.domain.LendingRequest;

/**
 * Created on 05/06/16
 * author: nixoxo
 */
public class CopyDashDTO {

    public CopyDashDTO(Long id, Book book, Boolean available, LendingRequest lendingRequest) {
        this.id = id;
        this.book = book;
        this.available = available;
        this.lendingRequest = lendingRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private boolean available;
    private Book book;

    public LendingRequest getLendingRequest() {
        return lendingRequest;
    }

    public void setLendingRequest(LendingRequest lendingRequest) {
        this.lendingRequest = lendingRequest;
    }

    private LendingRequest lendingRequest;

    public CopyDashDTO() {
    }

    public CopyDashDTO(Book book, Boolean available) {
        this.book = book;
        this.available = available;
    }

    public CopyDashDTO(Long id, Book book, Boolean available) {
        this.id = id;
        this.book = book;
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    /*
    public LendingRequest getLendingRequest() {
        return lendingRequest;
    }

    public void setLendingRequest(LendingRequest lendingRequest) {
        this.lendingRequest = lendingRequest;
    }
    */
}
