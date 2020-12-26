package com.gfu.ml.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class Parser {

    /**
     * @param header The first line of the input file.
     * @return trimmed attributes
     */
    public String[] parseHeader(final String header) {
        final String[] attributes = header.split(",");
        final String[] trimmedAttributes = Arrays.stream(attributes)
                .map(String::trim)
                .toArray(String[]::new);
        return Arrays.copyOf(trimmedAttributes, trimmedAttributes.length-1);
    }


    public Function<String[], List<Boolean>> nyParser() {
        return fields -> {
            final List<Boolean> row = new ArrayList<>();
            for (int i = 0; i < fields.length-1; i++) {
                if ("n".equals(fields[i])) {
                    row.add(false);
                } else if ("y".equals(fields[i])){
                    row.add(true);
                } else {
                    throw new Error("Unrecognized field " + fields[i]);
                }
            }
            return row;
        };
    }

    public Function<String[], Boolean> politicianParser() {
        return fields -> {
            final int outIdx = fields.length-1;
            if ("democrat".equals(fields[outIdx])) {
                return false;
            } else if ("republican".equals(fields[outIdx])){
                return true;
            } else {
                throw new Error("Unrecognized field " + fields[outIdx]);
            }
        };
    }
}
