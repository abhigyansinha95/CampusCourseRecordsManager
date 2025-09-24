package edu.ccrm.interfaces;

import java.util.List;

@FunctionalInterface
public interface Searchable<T> {
    List<T> search(String keyword);
}