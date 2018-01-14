package net.keremgokhan.lahmacuntracker.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import net.keremgokhan.lahmacuntracker.db.entity.Consumption;
import net.keremgokhan.lahmacuntracker.db.dao.ConsumptionDao;
import net.keremgokhan.lahmacuntracker.db.converter.DateConverter;
import net.keremgokhan.lahmacuntracker.db.entity.Lahmacun;
import net.keremgokhan.lahmacuntracker.db.dao.LahmacunDao;

@Database(entities = {Lahmacun.class, Consumption.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "lahmacun-tracker-db";

    public abstract LahmacunDao lahmacunDao();
    public abstract ConsumptionDao consumptionDao();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context);
                }
            }
        }
        return sInstance;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
    }
}
