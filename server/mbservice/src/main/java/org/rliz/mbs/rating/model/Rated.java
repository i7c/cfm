package org.rliz.mbs.rating.model;

/**
 * A rated something.
 */
public class Rated<T> implements Comparable {

    private T it;

    private int rating;

    public Rated(T it, int rating) {
        this.it = it;
        this.rating = rating;
    }

    public T getIt() {
        return it;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(((Rated) o).getRating(), rating);
    }
}
