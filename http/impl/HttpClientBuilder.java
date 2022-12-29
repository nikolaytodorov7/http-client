package org.example.http.impl;

public class HttpClientBuilder {
    public HttpClient build() {
        return new HttpClient(this);
    }
}
