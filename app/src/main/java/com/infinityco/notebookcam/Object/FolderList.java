package com.infinityco.notebookcam.Object;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by gabri on 24/08/2017.
 */

public class FolderList {

    private Context context;
    private ArrayList<Folder> folders;

    public FolderList(Context context){
        this.context = context;

        folders = new ArrayList<>();
    }

    public boolean isEmpty(){
        return folders.isEmpty();
    }

    public void add(Folder folder){
        folders.add(folder);
    }

    public void LoadFolders(){

        folders = new ArrayList<>();
        Folder folder = new Folder(context);


        if(!folder.existNotebookCamFolder())
            folder.createNoteBookCamFolder();

        File dir = new File(Environment.getExternalStorageDirectory().toString()+"/NoteBookCam");
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        File[] files = dir.listFiles(fileFilter);

        for(int i=0;i<files.length;i++){
            String folderName = files[i].getName();
            folder = new Folder(context,folderName);
            folders.add(folder);
        }
        Collections.sort(folders);
    }

    public Folder get(int pos){
        return folders.get(pos);
    }

    public int size(){
        return folders.size();
    }
}
