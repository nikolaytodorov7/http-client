package org.example.http.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestImpl extends HttpRequest {
    private String method;
    private URI uri;
    private Map<String, String> headers = new HashMap<>();

    public static HttpRequestBuilder newBuilder() {
        return new HttpRequestBuilder();
    }

    public HttpRequestImpl(HttpRequestBuilder builder) {
        this.method = builder.method;
        this.uri = builder.uri;
        if (builder.headers != null && builder.headers.size() != 0)
            this.headers = builder.headers;
    }

    public String method() {
        return method;
    }

    public URI uri() {
        return uri;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String toString() {
        return uri + " " + method;
    }

    public class BodyPublisher {
    }
}
