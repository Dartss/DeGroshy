package com.future.degroshi;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsViewActivity extends Activity {
    DrawArcs mDrawArcs;
    DBHelper dbHelper;
    FileWorker fileWorker;
    HashMap<String,Integer> mapOfSpnts = new HashMap<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("---Log---", "Was Created Statistics");
        setContentView(R.layout.activity_statistics_view);
        fileWorker = new FileWorker(this);
        dbHelper = new DBHelper(this);
        mDrawArcs = new DrawArcs(this,calculatingProcents());
        super.onCreate(savedInstanceState);
        RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.fragment_diagram);
        relativeLayout.addView(mDrawArcs);

    }

    public void onClick(View v) {
    }

    //расчитать доли трат
    private SparseIntArray calculatingProcents(){
        Long wastedMoney = calculateWastedMoney();

        SparseIntArray mColorsAndProcents = new SparseIntArray();
        ArrayList<Spent> tmpSpnt = MainActivity.mSpents;

        for (int i=0;i<mapOfSpnts.size();i++){
            String tmpSpntName = tmpSpnt.get(i).mName;
            if(mapOfSpnts.containsKey(tmpSpntName)) {
                long proc = ((long) mapOfSpnts.get(tmpSpntName) * 360) / wastedMoney;
                int color = tmpSpnt.get(i).mColor;
                mColorsAndProcents.put(color, (int) proc);
                Log.d("---Log---", "Spent named " + tmpSpntName + " Colored with " +
                        tmpSpnt.get(i).mColor + " have procents " + (int) proc + " wastedmoney " + wastedMoney);
            }

        }
        Log.d("---Log---", "mapColorandProc " + mColorsAndProcents + " has Size = "+ mColorsAndProcents.size());
        return mColorsAndProcents;
    }

    private long calculateWastedMoney() {
        long wastedMoney=0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("spents", null, null, null, null, null, null);

        //если список трат не пуст
        if (c.moveToFirst()) {

            // определить номера столбцов по имени в выборке
            int nameColIndex= c.getColumnIndex("name");
            int sumColIndex = c.getColumnIndex("sum");
            int dateColIndex = c.getColumnIndex("date");
            do {
                //определить общую сумму каждой траты
                String nameSpnt = c.getString(nameColIndex);
                Integer sumSpnt = c.getInt(sumColIndex);

                wastedMoney += sumSpnt;
                if(!mapOfSpnts.containsKey(nameSpnt)){
                    mapOfSpnts.put(nameSpnt, sumSpnt);
                }else{
                    mapOfSpnts.put(nameSpnt, mapOfSpnts.get(nameSpnt) + sumSpnt);
                }

            } while (c.moveToNext());

        } else {
            Log.d("---Log---", "size is zerro");
            Toast.makeText(MainActivity.context.getApplicationContext(),
                    "Cтатистика нульова", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return 0;
        }
        Log.d("---Log---", "\t" + mapOfSpnts);
        c.close();
        db.close();
        return wastedMoney;
    }

}
