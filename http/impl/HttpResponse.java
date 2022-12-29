package org.example.http.impl;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class HttpResponse<T> {
    private static final Set<String> VALID_HTTP_PROTOCOLS = new HashSet<>(Set.of(
            "HTTP/0.9", "HTTP/1.0", "HTTP/1.1", "HTTP/2", "HTTP/3"));

    private Map<String, String> headers = new HashMap<>();
    private String protocol;
    private int statusCode;
    private String statusMessage;
    private String body;

    public HttpResponse(BufferedReader in, FileOutputStream fileOutputStream) throws IOException {
        String line = in.readLine();
        if (line == null)
            throw new IllegalStateException("Invalid response!");

        String[] split = line.split(" ");
        if (split.length < 3) {
            statusCode = 400;
            throw new IllegalStateException("First line must contain protocol and status code");
        }

        protocol = split[0];
        if (!VALID_HTTP_PROTOCOLS.contains(protocol)) {
            statusCode = 400;
            throw new IllegalStateException("Invalid protocol!");
        }

        try {
            statusCode = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            statusCode = 400;
            throw new IllegalStateException("First part of the status code must be Integer!");
        }

        statusMessage = split[2];
        extractHeaders(in);

        if (fileOutputStream == null)
            extractStringBody(in);
        else
            extractFileBody(in, fileOutputStream);

        in.close();
        statusCode = 200;
    }

    private void extractFileBody(BufferedReader in, FileOutputStream fileOutputStream) {
        try {
            in.transferTo(new OutputStreamWriter(fileOutputStream));
        } catch (IOException e) {
            throw new RuntimeException("No output stream found!");
        }
    }

    private void extractStringBody(BufferedReader in) throws IOException {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && contentLength > 0) {
            body.append(line).append(System.lineSeparator());
            contentLength -= line.length();
        }

        this.body = body.toString();
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
        return body;
    }

    public static class BodyHandlers {
        public static BodyHandler<String> ofString() {
            return new BodyHandler<>();
        }

        public static BodyHandler<Path> ofFile(Path file) {
            return new BodyHandler<>(file);
        }
    }
}