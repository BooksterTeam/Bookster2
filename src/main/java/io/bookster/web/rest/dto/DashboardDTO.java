package io.bookster.web.rest.dto;

import io.bookster.domain.Copy;
import io.bookster.domain.Lending;
import io.bookster.domain.LendingRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 05/06/16
 * author: nixoxo
 */
public class DashboardDTO {

    private List<LendingRequest> lendingRequests = new ArrayList<>();

    private List<Lending> lendings = new ArrayList<>();

    private List<Copy> copies = new ArrayList<>();

    public DashboardDTO(List<LendingRequest> lendingRequests, List<Lending> lendings, List<Copy> copies) {
        this.lendingRequests = lendingRequests;
        this.lendings = lendings;
        this.copies = copies;
    }


    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

    public List<Lending> getLendings() {
        return lendings;
    }

    public void setLendings(List<Lending> lendings) {
        this.lendings = lendings;
    }

    public List<LendingRequest> getLendingRequests() {
        return lendingRequests;
    }

    public void setLendingRequests(List<LendingRequest> lendingRequests) {
        this.lendingRequests = lendingRequests;
    }
}
