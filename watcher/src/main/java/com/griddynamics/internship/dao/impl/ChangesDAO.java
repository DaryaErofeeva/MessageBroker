package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.models.Change;

import java.util.List;

public class ChangesDAO implements GenericDAO<Change, Integer> {
    @Override
    public List<Change> getAll() {
        return null;
    }

    @Override
    public int update(Change entity) {
        return 0;
    }

    @Override
    public Change getEntityById(Integer id) {
        return null;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }

    @Override
    public int create(Change entity) {
        return 0;
    }
}
