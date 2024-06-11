package com.example.poskedai;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Random;

public class Database extends SQLiteOpenHelper {
    Context context;
    Cursor cursor;
    SQLiteDatabase database;

    private static final String DATABASE_NAME = "rarahaja_db";
    private static final String TABLE_NAME = "transactions";
    private static final int DATABASE_VERSION = 3;
    private SQLiteDatabase sqLiteDatabase;

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        database = getReadableDatabase();
        database = getWritableDatabase();

    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id_customer varchar(50) PRIMARY KEY, customer_name varchar(50), no_table int(50))";
        sqLiteDatabase.execSQL(query);

    }

    String random(){
        int rand = new Random().nextInt(10000);
        return String.valueOf(rand);
    }
    public void save_data (String customer_name, String no_table){
        database.execSQL( "INSERT INTO " + TABLE_NAME + "VALUES" +
                "('"+random()+"','"+customer_name+"','"+no_table+"')"
        );
        Toast.makeText(context, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
    }

    Cursor show_data(){
        cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db0, int db1, int db2) {

    }

}


