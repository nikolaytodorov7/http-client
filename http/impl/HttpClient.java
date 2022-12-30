package org.example.http.impl;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

public class HttpClient {
    private final static int DEFAULT_PORT = 80;

    public HttpClient() {
    }

    public HttpClient(HttpClientBuilder builder) {
    }

    public static HttpClientBuilder newBuilder() {
        return new HttpClientBuilder();
    }

    public <T> HttpResponse<T> send(HttpRequest request, BodyHandler responseHandler) throws IOException {
        URI uri = request.uri();
        InetAddress address = InetAddress.getByName(uri.getHost());
        Socket socket = new Socket(address, DEFAULT_PORT);
        OutputStream outputStream = socket.getOutputStream();
        sendHeaders(request, outputStream);
        sendBody(request, outputStream);

        HttpResponse<T> httpResponse = new HttpResponse<>(socket.getInputStream(), responseHandler.fileOutputStream);
        socket.close();

        return httpResponse;
    }

    private static void sendHeaders(HttpRequest request, OutputStream outputStream) {
        URI uri = request.uri();
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        printWriter.println(String.format("%s %s HTTP/1.1", request.method(), uri.getPath()));
        printWriter.println(String.format("Host: %s", uri.getHost()));
        int available = 0;
        try {
            available = request.bodyPublisher.getBody().available();
        } catch (IOException ignored) {
        }

        printWriter.println(String.format("Content-length: %s", available));
        printWriter.println();
        printWriter.flush();
    }

    private static void sendBody(HttpRequest request, OutputStream outputStream) throws IOException {
        BodyPublisher bodyPublisher = request.bodyPublisher;
        InputStream bodyInputStream = bodyPublisher.getBody();
        bodyInputStream.transferTo(outputStream);
        outputStream.flush();
    }
}
