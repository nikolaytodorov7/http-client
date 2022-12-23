package org.example.http.impl;

import org.example.http.HttpResponse;

import java.io.IOException;

public abstract class HttpClient {
    public HttpClient() {
    }

    public HttpClient(HttpClientBuilderImpl httpClientBuilder) {

    }

    public static HttpClientBuilderImpl newBuilder() {
        return new HttpClientBuilderImpl();
    }

    public abstract <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseHandler) throws IOException;
}
