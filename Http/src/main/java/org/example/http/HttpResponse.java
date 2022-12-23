package org.example.http;

import java.util.Map;

public interface HttpResponse<T> {
    public int statusCode();

    public Map<String, String> headers();

    public T body();

    public class BodyHandler<T> {

    }

    public class BodyHandlers {
        public static BodyHandler<String> ofString() {
            return null;
        }
    }
}
