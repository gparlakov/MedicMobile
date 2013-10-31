package com.parlakov.uow;

import java.util.Collection;

/**
 * Created by georgi on 13-10-31.
 */
public interface IUsersRepository<T> {
    public T getById(String id);

    public Collection<T> getAll();

    public Boolean delete(T entity);

    public Boolean delete(String id);

    public T update(T entity);

    public String login(String username, String password);

    public String register(Object user);
}
