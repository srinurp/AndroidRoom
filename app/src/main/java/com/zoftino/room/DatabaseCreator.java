package com.zoftino.room;


import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseCreator {

    private static PersonDatabase personDatabase;
    private static final Object LOCK = new Object();

    public synchronized static PersonDatabase getPersonDatabase(Context context){
        if(personDatabase == null) {
            synchronized (LOCK) {
                if (personDatabase == null) {
                    personDatabase = Room.databaseBuilder(context,
                            PersonDatabase.class, "person db").build();
                }
            }
        }
        return personDatabase;
    }
}
