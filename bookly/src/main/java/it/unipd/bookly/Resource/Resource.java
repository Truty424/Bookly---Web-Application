package it.unipd.bookly.resource;
import java.io.IOException;
import java.io.OutputStream;

public interface Resource {
    void toJSON(OutputStream out) throws IOException;
}