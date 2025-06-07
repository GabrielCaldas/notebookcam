package com.infinityco.notebookcam.Object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.infinityco.notebookcam.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * Created by gabri on 22/08/2017.
 */

public class Photo implements Comparable<Photo>{

    private Context context;
    private String info;
    private File photo;
    private Date date;

    public Photo(Context context, String path, Date created){
        this.context = context;
        this.photo = new File(path);

        char[] ext = new char[4];
        ext[0]=path.charAt(path.length()-4);
        ext[1]=path.charAt(path.length()-3);
        ext[2]=path.charAt(path.length()-2);
        ext[3]=path.charAt(path.length()-1);


        this.info =  photo.getName().replaceAll(String.valueOf(ext),"");
        this.date = created;
    }

    public Photo(Context context, String path,String name, Date created){
        this.context = context;
        //this.foto = decodeFile(path,width,height);
        this.photo = new File(path);

        char[] ext = new char[4];
        ext[0]=path.charAt(path.length()-4);
        ext[1]=path.charAt(path.length()-3);
        ext[2]=path.charAt(path.length()-2);
        ext[3]=path.charAt(path.length()-1);


        this.info =  name;
        this.date = created;
    }

    public String getInfo(){
        return info;
    }

    public String getPath(){
        return photo.getPath();
    }


    public void deletPhoto(){
        photo.delete();
    }

    public Date getDateTime(){
        return date;
    }

    @Override
    public int compareTo(Photo o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
