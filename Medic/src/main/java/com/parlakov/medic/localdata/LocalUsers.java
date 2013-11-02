package com.parlakov.medic.localdata;

import com.parlakov.medic.models.User;
import com.parlakov.uow.IUsersRepository;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalUsers implements IUsersRepository<User> {
    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public Boolean delete(User entity) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void login(String username, String password) throws IOException {

    }

    @Override
    public void register(Object user) throws IOException {

    }
}
