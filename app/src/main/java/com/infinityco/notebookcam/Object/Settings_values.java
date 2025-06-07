package com.infinityco.notebookcam.Object;




import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Settings_values {
    private static final String TAG = "ManageFile";
    private Context context;

    public Settings_values(Context context) {
        this.context = context;
    }

    public boolean setAppColor(int Color) {
        String color = Color+"";
        try {
            FileOutputStream out = context.openFileOutput("color_status.txt", Context.MODE_PRIVATE);
            out.write(color.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public int getAppColor() throws FileNotFoundException, IOException {
        File file = context.getFilesDir();
        File textfile = new File(file + "/color_status.txt");

        FileInputStream input = context.openFileInput("color_status.txt");
        byte[] buffer = new byte[(int) textfile.length()];

        input.read(buffer);
        String result = new String(buffer);
        return Integer.parseInt(result);
    }

    public boolean setFirsTime() {
        try {
            FileOutputStream out = context.openFileOutput("first_status.txt", Context.MODE_PRIVATE);
            out.write("NoteBook Cam".getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean isFirstTime(){
        try {
            File file = context.getFilesDir();
            File textfile = new File(file + "/first_status.txt");

            FileInputStream input = context.openFileInput("first_status.txt");
            byte[] buffer = new byte[(int) textfile.length()];

            input.read(buffer);
            String result = new String(buffer);
            return false;
        }
        catch (Exception e){
            return true;
        }
    }

    public boolean setTypeFace(String typeface) {
        try {
            FileOutputStream out = context.openFileOutput("typeface_status.txt", Context.MODE_PRIVATE);
            out.write(typeface.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public int getTypeFace() throws FileNotFoundException, IOException {
        File file = context.getFilesDir();
        File textfile = new File(file + "/typeface_status.txt");

        FileInputStream input = context.openFileInput("typeface_status.txt");
        byte[] buffer = new byte[(int) textfile.length()];

        input.read(buffer);
        String result = new String(buffer);
        return Integer.parseInt(result);
    }
}
