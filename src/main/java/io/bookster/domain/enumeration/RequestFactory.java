package io.bookster.domain.enumeration;

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

    public static Request cancelRequest() {
        return new CancelRequest();
    }

    public static Request acceptRequest(){
        return new AcceptRequest();
    }


    public static RequestFactory getRequestFactory() {
        return requestFactory;
    }
}
