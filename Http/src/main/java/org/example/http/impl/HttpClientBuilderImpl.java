package org.example.http.impl;

public class HttpClientBuilderImpl {
    public HttpClientImpl build() {
        return new HttpClientImpl(this);
    }
}
