package io.bookster.domain.enumeration;

import io.bookster.domain.LendingRequest;

/**
 * Created on 08/05/16
 * author: nixoxo
 */
public class RequestFactory {

    private static RequestFactory requestFactory = new RequestFactory();

    private RequestFactory() {
    }

    public static Request pendingRequest() {
        return new PendingRequest();
    }

    public static RequestFactory getRequestFactory() {
        return requestFactory;
    }
}
