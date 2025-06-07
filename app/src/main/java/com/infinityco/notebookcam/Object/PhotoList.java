package com.infinityco.notebookcam.Object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by gabri on 24/08/2017.
 */

public class PhotoList {

    private Context context;
    private ArrayList<Photo> photos;
    private String Folder,Subject;

    public PhotoList(Context context, String Folder, String Subject){
        this.context = context;

        this.Subject = Subject;
        this.Folder = Folder;
        photos = new ArrayList<>();
    }

    public boolean isEmpty(){
        if(photos.size()==0)
            return true;
        else
            return false;
    }

    public void add(Photo photo){
        photos.add(photo);
    }

    public void deletPhoto(int pos){
        photos.get(pos).deletPhoto();
        photos.remove(pos);
    }

    /*public void LoadPhotos(int width,int heigth){
        photos = new ArrayList<>();
        File dir = new File(Environment.getExternalStorageDirectory().toString()+"/NoteBookCam/"+Folder+"/"+Subject);

        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                photos.add(new Photo(context,listFile[i].getPath(),new Date(listFile[i].lastModified()),width,heigth));
            }
            Collections.sort(photos);
        }
    }*/

    public void LoadPhotos() {
        photos = new ArrayList<>();
        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/NoteBookCam/" + Folder + "/" + Subject);

        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                photos.add(new Photo(context, listFile[i].getPath(), new Date(listFile[i].lastModified())));
            }
            Collections.sort(photos);
        }
    }

    public void AddPhoto(Photo photo){
        photos.add(photo);
    }

    public Photo get(int pos){
        return photos.get(pos);
    }

    public String getID(){
        return Folder+Subject;
    }

    public int size(){
        return photos.size();
    }
}
