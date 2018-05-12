package com.griddynamics.internship.dao;

import java.util.List;

public interface GenericDAO<E, K> {
    List<E> getAll();

    int update(E entity);

    E getEntityById(K id);

    int delete(K id);

    int create(E entity);
}
