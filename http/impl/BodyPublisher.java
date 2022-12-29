package org.example.http.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

import static org.example.http.impl.BodyType.*;

public class BodyPublisher {
    BodyType bodyType;
    byte[] body;
    InputStream inputStream;
    FileInputStream fileInputStream;

    public BodyPublisher() {
        bodyType = NO_BODY;
    }

    public BodyPublisher(byte[] body) {
        this.body = body;
        bodyType = BYTE_ARR;
    }

    public BodyPublisher(Supplier<? extends InputStream> streamSupplier) {
        this.inputStream = streamSupplier.get();
        bodyType = INPUT_STREAM;
    }


    public BodyPublisher(Path path) {
        try {
            fileInputStream = new FileInputStream(path.toFile());
            bodyType = FILE;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Invalid path '%s' provided!", path));
        }
    }
}
