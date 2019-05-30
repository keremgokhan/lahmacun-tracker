package net.keremgokhan.lahmacuntracker.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.keremgokhan.lahmacuntracker.db.entity.Consumption;

import java.util.List;

@Dao
public interface ConsumptionDao {

    @Query("SELECT * FROM consumption ORDER BY id DESC LIMIT 1")
    List<Consumption> getLast();

    @Query("SELECT * FROM consumption")
    List<Consumption> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Consumption consumption);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Consumption... consumptions);

    @Delete
    void delete(Consumption consumption);
}