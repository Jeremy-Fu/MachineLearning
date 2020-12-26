package com.gfu.ml.data;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DataSet {

    // full data set
    private final boolean[][] matrix;
    // attributes that care about, each element is the index.
    private final int[] attributes;
    private final String[] attributeNames;
    // the rows that care about, each element is the sample index
    private final int[] rows;

    public DataSet(
            final boolean[][] matrix,
            final String[] attributesNames
    ) {
        final int[] attributes = new int[matrix[0].length-1]; // [attr1, attr2, ... attrN, Y]
        final int[] rows = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            rows[i] = i;
        }
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = i;
        }

        this.attributes = attributes;
        this.attributeNames = attributesNames;
        assert(this.attributes.length == this.attributeNames.length);
        this.matrix = matrix;
        this.rows = rows;
    }

    private DataSet(
            final int[] attributes,
            final String[] attributeNames,
            final boolean[][] matrix,
            final int[] rows
    ) {
        this.attributes = attributes;
        this.attributeNames = attributeNames;
        this.matrix = matrix;
        this.rows = rows;
    }

    public boolean isConverged() {
        final int yIdx = matrix[0].length-1;
        final boolean y = matrix[rows[0]][yIdx];
        for (int i : rows) {
            if (y != matrix[i][yIdx]) {
                return false;
            }
        }
        return true;
    }

    public boolean getMajority() {
        int positives = 0, negatives = 0;
        final int yIdx = matrix[0].length-1;
        for (int i : rows) {
            if (matrix[i][yIdx]) {
                positives++;
            } else {
                negatives++;
            }
        }
        return positives > negatives;
    }

    public String getAttribute(final int idx) {
        return attributeNames[attributes[idx]];
    }

    public int attributesCount() {
        return attributes.length;
    }

    public int getRowsCount() {
        return rows.length;
    }

    public boolean attrValue(final int row, final int attr) {
        final int rowIdx = rows[row];
        final int attrIdx = attributes[attr];
        return matrix[rowIdx][attrIdx];
    }

    public boolean outValue(final int row) {
        final int rowIdx = rows[row];
        final int outIdx = matrix[0].length-1;
        return matrix[rowIdx][outIdx];
    }

    public DataSet[] split(final int attrIdx) {
        if (attributes.length == 0) {
            throw new Error(String.format("there is only one attribute in current dataset. "));
        }

        final int[] newAttributes = new int[attributes.length-1];
        for (int i = 0, j = 0; i < attributes.length; i++) {
            if (i == attrIdx) {
                continue;
            }

            newAttributes[j++] = attributes[i];
        }

        final List<Integer> row0 = new ArrayList<>();
        final List<Integer> row1 = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            if (!matrix[rows[i]][attributes[attrIdx]]) {
                row0.add(rows[i]);
            } else {
                row1.add(rows[i]);
            }
        }

        final DataSet dataSet0 = new DataSet(newAttributes, attributeNames, matrix, toArray(row0));
        final DataSet dataSet1 = new DataSet(newAttributes, attributeNames, matrix, toArray(row1));
        return new DataSet[] {dataSet0, dataSet1};
    }

    public static Builder builder(final String[] attributeNames) {
        return new Builder(attributeNames);
    }

    @VisibleForTesting
    int getAttributesCount() {
        return attributes.length;
    }

    private static int[] toArray(final List<Integer> rowL) {
        final int[] rowA = new int[rowL.size()];
        for (int i = 0; i < rowA.length; i++) {
            rowA[i] = rowL.get(i);
        }
        return rowA;
    }

    public static class Builder {
        private final List<List<Boolean>> data;
        private final String[] attributeNames;

        private Builder(final String[] attributeNames) {
            data = new ArrayList<>();
            this.attributeNames = attributeNames;
        }

        public Builder addRow(
                final String row,
                final Function<String[], List<Boolean>> attrConverter,
                final Function<String[], Boolean> outputConverter
        ) {
            final String[] fields = row.split(",");
            final String[] trimmedFields = Arrays.stream(fields)
                    .map(String::trim)
                    .toArray(String[]::new);
            final List<Boolean> attributes = attrConverter.apply(trimmedFields);
            attributes.add(outputConverter.apply(trimmedFields));
            data.add(attributes);
            return this;
        }

        public DataSet build() {
            final int rows = data.size();
            final int columns = attributeNames.length+1;
            final boolean[][] matrix = new boolean[rows][columns];
            for (int i = 0; i < rows; i++) {
                final List<Boolean> row = data.get(i);
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = row.get(j);
                }
            }
            return new DataSet(matrix, attributeNames);
        }
    }

}
