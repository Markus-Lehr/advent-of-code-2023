package com.markus.day8;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CyclicList<T> implements Iterable<T> {
    private final List<T> elements;

    public CyclicList(List<T> elements) {
        this.elements = elements;
    }

    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new CyclicIterator<>(elements);
    }

    public static class CyclicIterator<T> implements Iterator<T> {
        private final List<T> elements;
        private int currentIndex = 0;

        public CyclicIterator(List<T> elements) {
            this.elements = new ArrayList<>(elements);
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public T next() {
            if (currentIndex == elements.size()) {
                currentIndex = 0;
            }
            T result = elements.get(currentIndex);
            currentIndex++;
            return result;
        }
    }
}
