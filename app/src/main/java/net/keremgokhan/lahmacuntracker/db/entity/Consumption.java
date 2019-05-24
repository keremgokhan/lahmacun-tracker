package net.keremgokhan.lahmacuntracker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "consumption",
        foreignKeys = @ForeignKey(
            entity = Lahmacun.class,
            parentColumns = "id",
            childColumns = "lahmacun_id"),
        indices = {@Index("lahmacun_id")}
)
public class Consumption {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "lahmacun_id")
    public int lahmacunId;

    @ColumnInfo(name = "consumed_on")
    public Date consumedOn;

    public Consumption(int lahmacunId) {
        this.lahmacunId = lahmacunId;
        this.consumedOn = new Date();
    }
}
