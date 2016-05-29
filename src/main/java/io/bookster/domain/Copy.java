package io.bookster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Copy.
 */
@Entity
@Table(name = "copy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "copy")
public class Copy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne
    private Book book;

    @ManyToOne
    private BooksterUser booksterUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BooksterUser getBooksterUser() {
        return booksterUser;
    }

    public void setBooksterUser(BooksterUser booksterUser) {
        this.booksterUser = booksterUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Copy copy = (Copy) o;
        if(copy.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, copy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Copy{" +
            "id=" + id +
            ", verified='" + verified + "'" +
            ", available='" + available + "'" +
            '}';
    }
}
