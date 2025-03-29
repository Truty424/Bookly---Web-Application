package it.unipd.bookly.rest.resource;

import com.fasterxml.jackson.core.JsonGenerator;
import it.unipd.bookly.resource.AbstractResource;

import java.io.OutputStream;

/**
 * A generic wrapper that serializes primitive Java types into a simple JSON object.
 * Example output: { "value": 42 }
 *
 * @param <T> the type of the primitive value
 */
public class PrimitiveTypeJson<T> extends AbstractResource {

    private final T value;

    public PrimitiveTypeJson(T value) {
        this.value = value;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();

        switch (value.getClass().getSimpleName()) {
            case "Integer":
                jg.writeNumberField("value", (Integer) value);
                break;
            case "Double":
                jg.writeNumberField("value", (Double) value);
                break;
            case "String":
                jg.writeStringField("value", (String) value);
                break;
            case "Boolean":
                jg.writeBooleanField("value", (Boolean) value);
                break;
            default:
                jg.writeStringField("value", value.toString());
                break;
        }

        jg.writeEndObject();
        jg.flush();
    }
}
