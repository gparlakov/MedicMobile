package com.parlakov.uow;

import java.util.Collection;
import java.util.List;

/**
 * Created by georgi on 13-10-31.
 */
public interface IRepository<T> {

    public T getById(int id);

    public Collection<T> getAll();

    public void add(T entity);

    public Boolean delete(T entity);

    public Boolean delete(int id);

    public T update(T entity);

}
