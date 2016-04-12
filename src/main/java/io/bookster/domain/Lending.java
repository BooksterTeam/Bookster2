package io.bookster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "lending")
    @JsonIgnore
    private LendingRequest lendingRequest;

    @ManyToOne
    private BooksterUser holder;

    @ManyToOne
    private Copy copie;

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

    public LendingRequest getLendingRequest() {
        return lendingRequest;
    }

    public void setLendingRequest(LendingRequest lendingRequest) {
        this.lendingRequest = lendingRequest;
    }

    public BooksterUser getHolder() {
        return holder;
    }

    public void setHolder(BooksterUser booksterUser) {
        this.holder = booksterUser;
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
