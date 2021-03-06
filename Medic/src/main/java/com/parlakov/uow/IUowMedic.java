package com.parlakov.uow;

import com.parlakov.medic.models.Examination;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.models.User;

/**
 * Created by georgi on 13-10-31.
 */
public interface IUowMedic {
    IUsersRepository<User> getUsers();

    IRepository<Patient> getPatients();

    IRepository<Examination> getExaminations();

    void closeDb();
}
