package org.example.http.impl;


import org.example.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

public class HttpClientImpl extends HttpClient {
    public HttpClientImpl(HttpClientBuilderImpl builder) {
    }

    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseHandler)
            throws IOException {
        URI uri = request.uri();
        InetAddress address = InetAddress.getByName(uri.getHost());
        Socket socket = new Socket(address, 80);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(String.format("%s %s HTTP/1.1", request.method(), uri.getPath()));
        out.println(String.format("Host: %s", uri.getHost()));
        out.println();
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        HttpResponse<T> response = new HttpResponseImpl(in);
        socket.close();
        return response;
    }
}
