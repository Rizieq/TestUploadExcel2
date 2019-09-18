package com.ziadsyahrul.testuploadexcel2.Database.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ziadsyahrul.testuploadexcel2.Database.ModelDB.Cart;

@Database(entities = {Cart.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDao cartDAO();
    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context)
    {
        instance = Room.databaseBuilder(context,CartDatabase.class,"EDMT_DrinkShop")
                .allowMainThreadQueries()
                .build();
        return instance;
    }


}
