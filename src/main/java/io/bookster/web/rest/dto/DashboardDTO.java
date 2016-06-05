package io.bookster.web.rest.dto;

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

    private List<CopyDashDTO> copies = new ArrayList<>();

    private List<LendingRequest> externLendingRequests = new ArrayList<>();

    public DashboardDTO() {
    }

    public DashboardDTO(List<LendingRequest> externLendingRequests, List<LendingRequest> lendingRequests, List<Lending> lendings, List<CopyDashDTO> copies) {
        this.lendingRequests = lendingRequests;
        this.lendings = lendings;
        this.copies = copies;
        this.externLendingRequests = externLendingRequests;
    }

    public List<LendingRequest> getExternLendingRequests() {
        return externLendingRequests;
    }

    public void setExternLendingRequests(List<LendingRequest> externLendingRequests) {
        this.externLendingRequests = externLendingRequests;
    }

    public List<CopyDashDTO> getCopies() {
        return copies;
    }

    public void setCopies(List<CopyDashDTO> copies) {
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
