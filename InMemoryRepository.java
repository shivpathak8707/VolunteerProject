package com.volunteerapp;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRepository<T> implements ICRUDRepository<T, Integer> {

    private final Map<Integer, T> store = new HashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    @Override
    public synchronized T save(T entity) {
        int id = sequence.getAndIncrement();
        store.put(id, entity);
        return entity;
    }

    @Override
    public synchronized Optional<T> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public synchronized List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public synchronized void deleteById(Integer id) {
        store.remove(id);
    }
}