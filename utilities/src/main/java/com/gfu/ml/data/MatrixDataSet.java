package com.gfu.ml.data;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 * Automatically fold the bias/intercept term into the Xi[0];
 *
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class MatrixDataSet {

    // Pair: index:value
    private final List<List<Pair<Integer, Integer>>> examples;
    private final List<Integer> labels;
    private final int M;

    private MatrixDataSet(final List<List<Pair<Integer, Integer>>> examples, final List<Integer> labels, final int M) {
        checkArgument(examples.size() == labels.size(),
                "The number of examples and and the number of labels are not equal.");
        this.examples = examples;
        this.labels = labels;
        this.M = M;
    }

    /**
     * @return The number of examples
     */
    public int N() {
        return this.examples.size();
    }

    /**
     * @return The number of features (including bias/intercept term).
     */
    public int M() {
        return this.M;
    }

    public List<Pair<Integer, Integer>> example(final int i) {
        return examples.get(i);
    }

    public int label(final int i) {
        return labels.get(i);
    }

    public double[] exampleVector(final int i) {
        final double[] vector = new double[M];
        for (final Pair<Integer, Integer> j : examples.get(i)) {
            vector[j.getFirst()] = j.getSecond();
        }
        return vector;
    }

    /**
     * @param M The number of features, including bias/intercept term. (i.e. if you have 2 features, M = 3).
     * @return
     */
    public static Builder builder(final int M) {
        return new Builder(M);
    }

    public static class Builder {
        final int M;
        final ImmutableList.Builder<List<Pair<Integer, Integer>>> examples;
        final ImmutableList.Builder<Integer> labels;

        private Builder(final int M) {
            this.examples = new ImmutableList.Builder<>();
            this.labels = new ImmutableList.Builder<>();
            this.M = M;
        }

        public Builder addExample(final int ystari, final List<Pair<Integer, Integer>> features) {
            final Optional<Pair<Integer, Integer>> index0 =
                    features.stream()
                            .filter(pair -> pair.getFirst() == 0)
                            .findFirst();

            checkArgument(index0.isPresent(), "The bias term must be added.");
            checkArgument(index0.get().getSecond() == 1, "The value of the bias term must be 1");

            final Optional<Pair<Integer, Integer>> outOfBoundary =
                    features.stream().filter(pair -> pair.getFirst() >= M).findAny();
            checkArgument(!outOfBoundary.isPresent(), "There is a pair of which the first value is larger than M.");

            labels.add(ystari);
            examples.add(features);
            return this;
        }

        public MatrixDataSet build() {
            return new MatrixDataSet(this.examples.build(), this.labels.build(), M);
        }

    }
}
