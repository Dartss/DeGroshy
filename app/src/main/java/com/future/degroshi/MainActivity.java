package com.future.degroshi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements DlgColorPicker.OnColorChangedListener {

    FileWorker fileWorker = new FileWorker(this);
    DBHelper dbHelper = new DBHelper(this);
    public static ArrayList<Spent> mSpents = new ArrayList<>();
    public static ArrayList<String> mSpentsNames = new ArrayList<>();

    DialogFragment dlgAddSpent;
    DialogFragment dlgInputSum;
    DialogFragment dlgDeleteSpent;

    public static Spent mNewSpent;
    public static Context context;
    public static Paint mPaint = new Paint();

    int mIndexOfChangedSpent;
    ListView list;
    String mNameOfSpent;
    String mDateOfSpent;
    int mSumOfSpent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();


        //deleteFile("spentsList.json");
        //deleteDatabase("myDB");
        if(!fileWorker.isCreated("spentsList.json")) {
            final Spent mDefaultSpent = new Spent("Food", Color.GRAY);
            mSpents = new ArrayList<Spent>(){{
                add(mDefaultSpent);}};
            Log.d("---Log---", "Default stats was wroten" + mDefaultSpent);
        }else{
            mSpents = fileWorker.readFile();
            Log.d("---Log---", "Default stats was NOT wroten" + mSpents);
        }
        //mSpentsNames = listOfNames(mSpents);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        list = (ListView)findViewById(R.id.listOfSpents);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIndexOfChangedSpent = position;
                mNameOfSpent = mSpentsNames.get(position);
                dlgInputSum.show(getSupportFragmentManager(), "dlg_input_sum");
            }
        });
        updListView();
        dlgAddSpent = new DlgAddNewSpent();
        dlgInputSum = new DlgInputSum();
        dlgDeleteSpent = new DlgDeleteSpent();

    }


    //обновить вид списка трат
    public void updListView(){
        mSpentsNames.clear();
        mSpentsNames = listOfNames(mSpents);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mSpentsNames);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void onClick(View v){
        updListView();
        switch (v.getId()){
            case R.id.btn_add_spents:
                dlgAddSpent.show(getSupportFragmentManager(), "add dialog");
                break;
            case R.id.btn_statistics_view:
                Intent intent = new Intent(this, StatisticsViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_delete_spent:
                dlgDeleteSpent.show(getSupportFragmentManager(), "delete dialog");
                break;
            case R.id.btn_eraseDB:
                dbHelper.eraseDB();
                break;
        }
    }

    public void confirmNewSpentDlg(String tempName){
        mNewSpent = new Spent(tempName,0);
        new DlgColorPicker(this, this).show();
        mSpents.add(mNewSpent);
        Log.d("---Log---","New spent was added " + mNewSpent);
        updListView();

    }


    //удалить траты
    public void confirmDeleteSpentDlg(ArrayList<String> mSelectedSpents){

        //удалить записи выбранных трат из БД
        dbHelper.delFromDB(mSelectedSpents);
        //Cоздать временный список трат
        ArrayList<Spent> mCutedList = new ArrayList<>();
        //каждую трату из нынешнего списка
        int size = mSpents.size();
        Log.d("---Log---", "Size of mSpents before deleting " + size);
        for(int i = 0; i<size;i++){
            //сравнить со списком выделенных на удаление
            if(!mSelectedSpents.contains(mSpents.get(i).mName)){
                //если трата не выделена на удаление
                //добавить во временный список
                mCutedList.add(mSpents.get(i));
            }
        }

        //обновить список трат
        mSpents = mCutedList;
        updListView();

    }

    public void confirmSumDlg(Integer mInputSum) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.FF.yy");
        mDateOfSpent = sdf.format(new Date(System.currentTimeMillis()));
        mSumOfSpent = mInputSum;
        dbHelper.addToDB(mNameOfSpent, mSumOfSpent, mDateOfSpent);

    }

    public ArrayList<String> listOfNames(ArrayList<Spent> spents){
        ArrayList<String> tmpAL = new ArrayList<>();
        for(Spent spent : spents){
            tmpAL.add(spent.mName);
        }
        Log.d("---Log---", "listOfNames " + tmpAL);
        return tmpAL;

    }

    public void onSpentColorChaged(){
        mNewSpent = new Spent(mSpents.get(mIndexOfChangedSpent).mName,0);
        new DlgColorPicker(this, this).show();
        mSpents.remove(mSpents.get(mIndexOfChangedSpent));
        mSpents.add(mNewSpent);
        updListView();

    }


    @Override
    public void colorChanged(int color) {

        //mPaint.setColor(color);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("---Log---","Was paused");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("---Log---","Was Resumed");
    }

    @Override
    protected void onStop(){
        deleteFile("spentsList.json");
        fileWorker.writeFile(mSpents);
        super.onStop();

        Log.d("---Log---","Was Stoped");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("---Log---","Was destroyed");
    }


}
