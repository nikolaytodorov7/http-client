package org.example.http.impl;

import org.example.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HttpResponseImpl implements HttpResponse {
    private static final Set<String> VALID_HTTP_PROTOCOLS = new HashSet<>(Set.of(
            "HTTP/0.9", "HTTP/1.0", "HTTP/1.1", "HTTP/2", "HTTP/3"));

    private Map<String, String> headers = new HashMap<>();
    private String protocol;
    private int statusCode;
    private String statusMessage;
    private String strBody;

    public HttpResponseImpl(BufferedReader in) throws IOException {
        String line = in.readLine();
        if (line == null)
            throw new IllegalStateException("Invalid response!");

        String[] split = line.split(" ");
        if (split.length < 3)
            throw new IllegalStateException("First line must contain protocol and status code");

        protocol = split[0];
        if (!VALID_HTTP_PROTOCOLS.contains(protocol))
            throw new IllegalStateException("Invalid protocol!");

        try {
            statusCode = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("First part of the status code must be Integer!");
        }

        statusMessage = split[2];
        extractHeaders(in);
        extractBody(in);
    }

    private void extractBody(BufferedReader in) throws IOException {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && contentLength > 0) {
            System.out.println(contentLength);
            body.append(line).append(System.lineSeparator());
            contentLength -= line.length();
        }

        this.strBody = body.toString();
    }

    private void extractHeaders(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.isBlank())
                break;

            String[] entry = line.split(": ");
            if (entry.length != 2)
                throw new IllegalStateException(String.format("Invalid headers '%s'", line));

            String key = entry[0];
            String value = entry[1];
            headers.put(key, value);
        }
    }

    public int statusCode() {
        return statusCode;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String body() {
        return body();
    }

}
