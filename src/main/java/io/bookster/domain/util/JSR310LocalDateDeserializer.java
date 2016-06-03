package io.bookster.domain.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Custom Jackson deserializer for transforming a JSON object (using the ISO 8601 date formatwith optional time)
 * to a JSR310 LocalDate object.
 */
public class JSR310LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    public static final JSR310LocalDateDeserializer INSTANCE = new JSR310LocalDateDeserializer();

    private static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME;

    private JSR310LocalDateDeserializer() {}

    static {
        ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_OFFSET_TIME)
            .toFormatter();
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken jsonToken = parser.getCurrentToken();
        if (jsonToken == JsonToken.START_ARRAY) {
            return getLocalDateFromArray(parser, context);
        } else if (jsonToken == JsonToken.VALUE_STRING) {
            return getLocalDateFromString(parser);
        } else {
            throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
        }
    }

    private LocalDate getLocalDateFromString(JsonParser parser) throws IOException {
        String string = parser.getText().trim();
        if (string.length() == 0) {
            return null;
        }
        return LocalDate.parse(string, ISO_DATE_OPTIONAL_TIME);
    }

    private LocalDate getLocalDateFromArray(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.nextToken() == JsonToken.END_ARRAY) {
            return null;
        }
        int year = parser.getIntValue();

        parser.nextToken();
        int month = parser.getIntValue();

        parser.nextToken();
        int day = parser.getIntValue();

        if (parser.nextToken() != JsonToken.END_ARRAY) {
            throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
        }
        return LocalDate.of(year, month, day);
    }
}
