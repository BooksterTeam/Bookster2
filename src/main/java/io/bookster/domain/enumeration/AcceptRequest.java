package io.bookster.domain.enumeration;

import io.bookster.domain.LendingRequest;

/**
 * Created on 08/05/16
 * author: nixoxo
 */
public class AcceptRequest implements Request {

    public AcceptRequest(){

    }

    @Override
    public LendingRequest process(LendingRequest lendingRequest) {
        lendingRequest.setStatus(RequestStatus.ACCEPTED);
        return lendingRequest;
    }
}
