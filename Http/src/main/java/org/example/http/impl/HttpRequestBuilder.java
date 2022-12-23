package org.example.http.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HttpRequestBuilder {
    URI uri = null;
    String method = null;
    Map<String, String> headers = new HashMap<>();
    String strBody;

    public HttpRequestBuilder uri(URI uri) {
        if (uri == null)
            throw new IllegalArgumentException("Uri can't be null!");

        checkURI(uri);
        this.uri = uri;
        return this;
    }

    public HttpRequestImpl build() {
        if (uri == null)
            throw new IllegalStateException("Uri can't be null");

        if (method == null)
            throw new IllegalStateException("Method can't be null");

        return new HttpRequestImpl(this);
    }

    private void checkURI(URI uri) {
        String scheme = uri.getScheme();
        if (scheme == null)
            throw new IllegalArgumentException("URI with undefined scheme");

        scheme = scheme.toLowerCase(Locale.US);
        if (!(scheme.equals("https") || scheme.equals("http")))
            throw new IllegalArgumentException(String.format("invalid URI scheme %s", scheme));

        if (uri.getHost() == null)
            throw new IllegalArgumentException(String.format("unsupported URI %s", uri));
    }

    public HttpRequestBuilder GET() {
        this.method = "GET";
        return this;
    }

    public HttpRequestBuilder POST(HttpRequest.BodyPublisher body) {
        this.method = "POST";
        return this;
    }

    public HttpRequestBuilder PUT(HttpRequest.BodyPublisher body) {
        this.method = "PUT";
        return this;
    }

    public HttpRequestBuilder DELETE() {
        this.method = "DELETE";
        return this;
    }

    public HttpRequestBuilder header(String key1, String value1) {
        String[] headers = new String[]{key1, value1};
        return headers(headers);
    }

    public HttpRequestBuilder headers(String... headers) {
        if (headers == null)
            throw new IllegalArgumentException("Headers can't be null!");

        if (headers.length == 0 || headers.length % 2 != 0)
            throw new IllegalArgumentException(String.format("Wrong number of parameters: %d!", headers.length));

        for (int i = 0; i < headers.length; i += 2) {
            String name = headers[i];
            String value = headers[i + 1];
            validateNameAndValue(name, value);
            this.headers.put(name, value);
        }

        return this;
    }

    private void validateNameAndValue(String name, String value) {
        if (name == null)
            throw new IllegalArgumentException(String.format("Invalid header name %s!", name));

        if (value == null)
            throw new IllegalArgumentException(String.format("Invalid header value %s!", value));
    }
}
