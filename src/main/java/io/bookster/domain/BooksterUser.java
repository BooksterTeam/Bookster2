package io.bookster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BooksterUser.
 */
@Entity
@Table(name = "bookster_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "booksteruser")
public class BooksterUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "booksterUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LendingRequest> froms = new HashSet<>();

    @OneToMany(mappedBy = "booksterUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Copy> ownerCopies = new HashSet<>();

    @OneToMany(mappedBy = "booksterUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lending> lendings = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<LendingRequest> getFroms() {
        return froms;
    }

    public void setFroms(Set<LendingRequest> lendingRequests) {
        this.froms = lendingRequests;
    }

    public Set<Copy> getOwnerCopies() {
        return ownerCopies;
    }

    public void setOwnerCopies(Set<Copy> copys) {
        this.ownerCopies = copys;
    }

    public Set<Lending> getLendings() {
        return lendings;
    }

    public void setLendings(Set<Lending> lendings) {
        this.lendings = lendings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BooksterUser booksterUser = (BooksterUser) o;
        if(booksterUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, booksterUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BooksterUser{" +
            "id=" + id +
            '}';
    }
}
