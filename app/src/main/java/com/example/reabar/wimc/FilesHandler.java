package com.example.reabar.wimc;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by reabar on 24/07/2016.
 */

public class FilesHandler {

    public void writeToFile(String text,String fileName) {
        String filename = fileName;
        FileOutputStream outputStream;

        try {
            outputStream = MyApplication.getAppContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            return file.delete();
        }

        else{
            return false;
        }
    }

    public String readFromFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if(file.exists()){
            try {
                InputStream inputStream = MyApplication.getAppContext().openFileInput(fileName);

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    return stringBuilder.toString();
                }
                else
                    return null;
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }
        return null;
    }
}
