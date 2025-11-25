package com.volunteerapp;

import java.util.List;
import java.util.Optional;
public interface ICRUDRepository<T, ID> {
    T save(T entity) throws AppException;
    Optional<T> findById(ID id) throws AppException;
    List<T> findAll() throws AppException;
    void deleteById(ID id) throws AppException;
}
