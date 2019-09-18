package com.ziadsyahrul.testuploadexcel2.Database.ModelDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cart")
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sno")
    public int sno;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "det")
    public String det;


}
