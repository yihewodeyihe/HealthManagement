package com.example.uitest;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBclass extends SQLiteOpenHelper {
    public DBclass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句并执行
        String sql="create table BaseDatas(id integer primary key autoincrement,BeatRate integer,HighPressure integer,LowPressure integer,BloodGlucose integer,BloodOxygen integer,CreatTime DATETIME)";
        String sql1="create table UserDatas(useid integer primary key autoincrement,Username varchar(20),password varchar(20),age integer)";
        String sql2="create table ScoreDatas(id integer primary key autoincrement,BeatSocres integer,BloodPScores integer,SugarSocres integer,OxygenSocres integer,CreatTime DATETIME)";
        String sql3="create table illDatas(id integer primary key autoincrement,Heart varchar(20),Pressure varchar(20),Sugar varchar(20),Oxygen varchar(20),CreatTime DATETIME)";
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

