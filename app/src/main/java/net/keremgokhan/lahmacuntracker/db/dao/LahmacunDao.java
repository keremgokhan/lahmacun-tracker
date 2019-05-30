package net.keremgokhan.lahmacuntracker.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.keremgokhan.lahmacuntracker.db.entity.Lahmacun;

import java.util.List;

@Dao
public interface LahmacunDao {

    @Query("SELECT * FROM lahmacun ORDER BY id DESC LIMIT 1")
    List<Lahmacun> getLast();

    @Query("SELECT * FROM lahmacun")
    List<Lahmacun> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Lahmacun lahmacun);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(Lahmacun... lahmacuns);

    @Delete
    void delete(Lahmacun lahmacun);
}
