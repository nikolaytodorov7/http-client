package org.example.http.impl;

import java.net.URI;
import java.util.Map;

public abstract class HttpRequest {
    public static HttpRequestBuilder newBuilder() {
        return new HttpRequestBuilder();
    }

    public abstract String method();

    public abstract URI uri();

    public abstract Map<String, String> headers();

    public static class BodyPublishers {
        public static BodyPublisher ofString(String sampleRequestBody) {
            return null;
        }
    }

    public class BodyPublisher {
    }
}
