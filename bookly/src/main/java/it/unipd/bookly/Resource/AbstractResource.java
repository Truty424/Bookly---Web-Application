package it.unipd.bookly.Resource;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstract base class for resources that can be serialized to JSON.
 * Subclasses must implement the {@link #writeJSON(OutputStream)} method
 * to define how the resource is serialized.
 */
public abstract class AbstractResource implements Resource {

    /**
     * Logger for logging messages and errors.
     */
    protected static final Logger LOGGER = LogManager.getLogger(it.unipd.bookly.rest.AbstractRestResource.class, StringFormatterMessageFactory.INSTANCE);

    /**
     * The JSON factory to be used for creating JSON parsers and generators.
     */
    protected static final JsonFactory JSON_FACTORY;

    static {
        // set up the JSON factory
        JSON_FACTORY = new JsonFactory();
        JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);

        LOGGER.debug("JSON factory successfully setup.");
    }

    /**
     * Default constructor for AbstractResource.
     * Initializes the base class for resources that can be serialized to JSON.
     */
    public AbstractResource() {
        // Default constructor
    }

    /**
     * Serializes the resource to JSON and writes it to the provided output stream.
     *
     * @param out The output stream to which the JSON representation of the resource is written.
     * @throws IOException If an error occurs during serialization.
     */
    @Override
    public void toJSON(final OutputStream out) throws IOException {

        if (out == null) {
            LOGGER.error("The output stream cannot be null.");
            throw new IOException("The output stream cannot be null.");
        }

        try {
            // delegates writing to subclsses
            writeJSON(out);
        } catch (Exception e) {
            LOGGER.error("Unable to serialize the resource to JSON.", e);
            throw new IOException("Unable to serialize the resource to JSON.", e);
        }

    }

    /**
     * Performs the actual writing of JSON.
     *
     * Subclasses have to implement this method to provide the actual logic
     * needed for representing the {@code
     * Resource} to JSON.
     *
     * @param out the stream to which the JSON representation of the
     * {@code Resource} has to be written.
     *
     * @throws Exception if something goes wrong during writing.
     */
    protected abstract void writeJSON(OutputStream out) throws Exception;
}
