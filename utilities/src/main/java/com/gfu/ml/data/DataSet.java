package com.gfu.ml.data;

import java.util.ArrayList;
import java.util.List;


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

    DataSet(
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

    public String getAttribute(final int attrIdx) {
        return attributeNames[attrIdx];
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

    /**
     * [attr0, attr1, ..., attrN-1, Y]
     *
     * @return
     */
    boolean[][] getData() {
        final int outputIdx = matrix[0].length-1;
        final boolean[][] copy = new boolean[rows.length][attributes.length+1];
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < attributes.length; j++) {
                copy[i][j] = matrix[rows[i]][attributes[j]];
            }
            copy[i][attributes.length] = matrix[i][outputIdx];
        }
        return copy;
    }


    public DataSet[] split(final int attrIdx) {
        if (attributes.length == 0) {
            throw new Error(String.format("there is only one attribute in current dataset. "));
        }

        final int[] newAttributes = new int[attributes.length-1];
        final String[] nAttributeNames = new String[attributes.length-1];
        for (int i = 0, j = 0; i < attributes.length; i++) {
            if (attributes[i] == attrIdx) {
                continue;
            }
            newAttributes[j] = attributes[i];
            nAttributeNames[j++] = attributeNames[i];
        }

        final List<Integer> row0 = new ArrayList<>();
        final List<Integer> row1 = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            if (!matrix[i][attrIdx]) {
                row0.add(i);
            } else {
                row1.add(i);
            }
        }

        final DataSet dataSet0 = new DataSet(newAttributes, nAttributeNames, matrix, toArray(row0));
        final DataSet dataSet1 = new DataSet(newAttributes, nAttributeNames, matrix, toArray(row1));
        return new DataSet[] {dataSet0, dataSet1};
    }

    private static int[] toArray(final List<Integer> rowL) {
        final int[] rowA = new int[rowL.size()];
        for (int i = 0; i < rowA.length; i++) {
            rowA[i] = rowL.get(i);
        }
        return rowA;
    }

}
