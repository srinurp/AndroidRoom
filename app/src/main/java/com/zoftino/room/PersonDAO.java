package com.zoftino.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PersonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertPerson(Person person);
    @Update
    public void updatePerson(Person person);
    @Delete
    public void deletePerson(Person person);
    @Query("SELECT * FROM person")
    public LiveData<List<Person>> getAllPersons();
    @Query("SELECT * FROM person where mobile = :mobileIn")
    public LiveData<Person> getPersonByMobile(String mobileIn);
    @Query("SELECT * FROM person where city In (:cityIn)")
    public List<Person> getPersonByCities(List<String> cityIn);
}
