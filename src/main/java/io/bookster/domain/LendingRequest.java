package io.bookster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import io.bookster.domain.enumeration.RequestStatus;

/**
 * A LendingRequest.
 */
@Entity
@Table(name = "lending_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lendingrequest")
public class LendingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "created", nullable = false)
    private LocalDate created;

    @NotNull
    @Column(name = "from", nullable = false)
    private LocalDate from;

    @NotNull
    @Column(name = "due", nullable = false)
    private LocalDate due;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @ManyToOne
    private BooksterUser booksterUser;

    @OneToOne
    @JoinColumn(unique = true)
    private Copy copie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public BooksterUser getBooksterUser() {
        return booksterUser;
    }

    public void setBooksterUser(BooksterUser booksterUser) {
        this.booksterUser = booksterUser;
    }

    public Copy getCopie() {
        return copie;
    }

    public void setCopie(Copy copy) {
        this.copie = copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LendingRequest lendingRequest = (LendingRequest) o;
        if(lendingRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lendingRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LendingRequest{" +
            "id=" + id +
            ", created='" + created + "'" +
            ", from='" + from + "'" +
            ", due='" + due + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
