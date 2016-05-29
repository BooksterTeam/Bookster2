package io.bookster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @ManyToOne
    private BooksterUser booksterUser;

    @OneToOne
    @JoinColumn(unique = true)
    private Lending lending;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
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

    public Lending getLending() {
        return lending;
    }

    public void setLending(Lending lending) {
        this.lending = lending;
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
            ", date='" + date + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
