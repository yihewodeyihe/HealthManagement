package com.example.uitest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.ArrayList;
import java.util.List;

public class JavaBean {
    public int BeatScore;
    public int OxygenScore;
    public int SugarScore;
    public int PressScore;
    public boolean LowPress;
    public boolean HighPress;

    public JavaBean(){}
    public void JBBeat(int beat){
        this.BeatScore = beat;
    }
    public void JBOxygen(int oxygen){
        this.OxygenScore = oxygen;
    }
    public void JBPress(int PressS,boolean PressH,boolean PressL){
        this.LowPress = PressL;
        this.HighPress = PressH;
        this.PressScore = PressS;
    }
    public void JBSugar(int sugar){
        this.SugarScore = sugar;
    }
    public static void MakeScores(DBclass dbb){
        String[] ArrayName=new String[]{"id","BeatRate","HighPressure","LowPressure","BloodGlucose" ,"BloodOxygen"};
        final SQLiteDatabase db = dbb.getWritableDatabase();
        Integer[] iData = new Integer[4];
        ContentValues values = new ContentValues();
        Python python=Python.getInstance();
        PyObject[][] obj;
        Integer[] Sum0 = new Integer[3];
        List<PyObject> params1 = new ArrayList<PyObject>();
        List<PyObject> params2 = new ArrayList<PyObject>();
        List<PyObject> params3 = new ArrayList<PyObject>();
        List<PyObject> params4 = new ArrayList<PyObject>();
        List<PyObject> params5 = new ArrayList<PyObject>();
        Cursor cursor = db.query("BaseDatas", ArrayName, null, null, null, null, null);
        for(int a=0;a<=4;a++){
            if(cursor.moveToNext()){
                params1.add(PyObject.fromJava(cursor.getInt(Math.abs((cursor.getColumnIndex(ArrayName[1]))))));
                params2.add(PyObject.fromJava(cursor.getInt(Math.abs((cursor.getColumnIndex(ArrayName[2]))))));
                params5.add(PyObject.fromJava(cursor.getInt(Math.abs((cursor.getColumnIndex(ArrayName[3]))))));
                params3.add(PyObject.fromJava(cursor.getInt(Math.abs((cursor.getColumnIndex(ArrayName[4]))))));
                params4.add(PyObject.fromJava(cursor.getInt(Math.abs((cursor.getColumnIndex(ArrayName[5]))))));
                iData[a] = cursor.getInt(Math.abs((cursor.getColumnIndex("id"))));


            }
            else
                break;
        }
        cursor.close();
        PyObject pyObject1=python.getModule("dafen").callAttr("beatscore",params1);
        PyObject pyObject2=python.getModule("dafen").callAttr("beatscore",params2,params5);
        PyObject pyObject3=python.getModule("dafen").callAttr("beatscore",params3);
        PyObject pyObject4=python.getModule("dafen").callAttr("beatscore",params4);
        List<PyObject> fast1 = pyObject1.asList();
        List<PyObject> fast2 = pyObject2.asList();
        List<PyObject> fast3 = pyObject3.asList();
        List<PyObject> fast4 = pyObject4.asList();
        JavaBean rapid = new JavaBean();
        rapid.JBBeat(fast1.get(0).toInt());
        values.put("id",iData[4]);
        values.put("BeatSocres",fast1.get(0).toInt());
        values.put("BloodPScores",fast2.get(0).toInt());
        values.put("SugarSocres",fast3.get(0).toInt());
        values.put("OxygenSocres",fast4.get(0).toInt());
        db.insert("ScoreDatas",null,values);



    }
}
