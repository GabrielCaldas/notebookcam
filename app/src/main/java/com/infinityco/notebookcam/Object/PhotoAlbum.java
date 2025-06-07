package com.infinityco.notebookcam.Object;

import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by gabri on 13/09/2017.
 */

public class PhotoAlbum {
    ArrayList<PhotoList> photoLists = new ArrayList<>();

    public PhotoAlbum(){}

    public PhotoList getList(String ID){
        PhotoList Return = null;
        for(int i=0;i<photoLists.size();i++){
            if (photoLists.get(i).getID().equals(ID)){
                Return = photoLists.get(i);
                break;
            }
        }
        return Return;
    }

    public void addPhotoList(PhotoList photoList){
        photoLists.add(photoList);
    }

}
