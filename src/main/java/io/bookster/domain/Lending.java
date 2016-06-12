package io.bookster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Lending.
 */
@Entity
@Table(name = "lending")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lending")
public class Lending implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @ManyToOne
    private BooksterUser booksterUser;

    @OneToOne
    @JoinColumn(unique = true)
    private Copy copy;

    public Lending() {
    }

    public Lending(BooksterUser booksterUser, LocalDate fromDate, LocalDate dueDate, Copy copy) {
        this.booksterUser = booksterUser;
        this.fromDate = fromDate;
        this.dueDate = dueDate;
        this.copy = copy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BooksterUser getBooksterUser() {
        return booksterUser;
    }

    public void setBooksterUser(BooksterUser booksterUser) {
        this.booksterUser = booksterUser;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lending lending = (Lending) o;
        if(lending.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lending.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lending{" +
            "id=" + id +
            ", fromDate='" + fromDate + "'" +
            ", dueDate='" + dueDate + "'" +
            '}';
    }
}
