package it.unipd.bookly.Resource;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for resources that can be serialized to JSON.
 * Classes implementing this interface must provide a method to serialize
 * the resource to a JSON representation.
 */
public interface Resource {

    /**
     * Serializes the resource to JSON and writes it to the provided output stream.
     *
     * @param out The output stream to which the JSON representation of the resource is written.
     * @throws IOException If an error occurs during serialization.
     */
    void toJSON(OutputStream out) throws IOException;
}