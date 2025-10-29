package com.crisi.easy.repository;

import java.util.List;

public interface BaseRepository<T> {
    List<T> findAll();
    T findById(Long id);
    int save(T entity);
}