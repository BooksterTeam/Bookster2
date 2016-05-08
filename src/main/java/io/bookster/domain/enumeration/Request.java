package io.bookster.domain.enumeration;

import io.bookster.domain.LendingRequest;

/**
 * Created on 08/05/16
 * author: nixoxo
 */
public interface Request {

    public LendingRequest process(LendingRequest lr);

}
