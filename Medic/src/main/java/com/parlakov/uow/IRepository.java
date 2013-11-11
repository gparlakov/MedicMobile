package com.parlakov.uow;

import com.parlakov.medic.models.Examination;

import java.util.Collection;
import java.util.List;

/**
 * Created by georgi on 13-10-31.
 */
public interface IRepository<T> {

    T getById(Object id);

    public Object getAll();

    public void add(T entity);

    public void delete(T entity);

    public void update(T entity);

    public void close();

}
