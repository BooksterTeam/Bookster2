package io.bookster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
    @Column(name = "from", nullable = false)
    private ZonedDateTime from;

    @NotNull
    @Column(name = "due", nullable = false)
    private ZonedDateTime due;

    @ManyToOne
    private BooksterUser booksterUser;

    @OneToOne
    @JoinColumn(unique = true)
    private Copy copy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFrom() {
        return from;
    }

    public void setFrom(ZonedDateTime from) {
        this.from = from;
    }

    public ZonedDateTime getDue() {
        return due;
    }

    public void setDue(ZonedDateTime due) {
        this.due = due;
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
            ", from='" + from + "'" +
            ", due='" + due + "'" +
            '}';
    }
}
