package com.example.sukurax.linehack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by sukurax on 16/3/23.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_DATA="create table Data ("
            +" noteid integer primary key autoincrement,"
            +"username text,"
            +"sex text,"
            +"age text,"
            +"hobby text,"
            +"qqnum text)";
    private Context mcontext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mcontext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATA);
        Toast.makeText(mcontext,"Create succeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Date");
        onCreate(db);
    }
}
