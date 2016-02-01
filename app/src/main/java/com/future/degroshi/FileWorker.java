package com.future.degroshi;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileWorker {
    Context context;
    protected FileWorker(Context context){
        this.context = context;
    }
    private final static String FILENAME = "spentsList.json";

    boolean isCreated(String filename){
        InputStream inputStream;
        try {
            inputStream = context.openFileInput(filename);
            return inputStream != null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //добавляет в тескстовый файл новую трату
    void writeFile(ArrayList<Spent> spents){
        Gson gson = new Gson();
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(FILENAME, Context.MODE_APPEND)));
            for (Spent spent : spents) {
                String jsonRepresentation = gson.toJson(spent);
                bw.write(jsonRepresentation);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Читает текстовый файл и заносит все траты в список
    public ArrayList<Spent> readFile() {
        ArrayList<Spent> mSpents = new ArrayList<>();
        Gson gson = new Gson();
        try {
            BufferedReader buffered = new BufferedReader(new InputStreamReader(
                    context.openFileInput(FILENAME)));
            String readString;
            while ((readString = buffered.readLine()) != null) {
                Spent spent = gson.fromJson(readString, Spent.class);
                mSpents.add(spent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mSpents;
    }
}
