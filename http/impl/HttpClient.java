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

    public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseHandler) throws IOException {
        URI uri = request.uri();
        InetAddress address = InetAddress.getByName(uri.getHost());
        Socket socket = new Socket(address, DEFAULT_PORT);
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        printWriter.println(String.format("%s %s HTTP/1.1", request.method(), uri.getPath()));
        printWriter.println(String.format("Host: %s", uri.getHost()));
        printWriter.println();
        printWriter.flush();

        BodyPublisher bodyPublisher = request.bodyPublisher;
        BodyType bodyType = bodyPublisher.bodyType;
        switch (bodyType) {
            case BYTE_ARR -> {
                byte[] bodyByteArray = bodyPublisher.body;
                outputStream.write(bodyByteArray);
            }
            case FILE -> {
                FileInputStream bodyFileInputStream = bodyPublisher.fileInputStream;
                bodyFileInputStream.transferTo(outputStream);
            }
            case INPUT_STREAM -> {
                InputStream bodyInputStream = bodyPublisher.inputStream;
                bodyInputStream.transferTo(outputStream);
            }
        }
        outputStream.flush();

        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return new HttpResponse(bufferedReader, responseHandler.fileOutputStream);
    }
}
