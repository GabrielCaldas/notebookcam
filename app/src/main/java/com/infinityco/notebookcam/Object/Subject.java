package com.infinityco.notebookcam.Object;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.infinityco.notebookcam.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gabri on 22/08/2017.
 */

public class Subject implements Comparable<Subject>{

    private Context context;
    private String subjectName, sdcard,folderDir;

    public Subject(Context context){
        this.context = context;
        this.sdcard = Environment.getExternalStorageDirectory().toString()+"/";
    }

    public Subject(Context context, String name,String Folder){
        this.sdcard = Environment.getExternalStorageDirectory().toString()+"/NoteBookCam/"+Folder;

        this.context = context;
        this.subjectName= name;
        this.folderDir = sdcard+"/"+subjectName;
    }


    public void saveSubject(){
        if(subjectName != null){

            if(!existSubject()){
                createSubject();
            }

            folderDir = sdcard+"/"+subjectName;
            File dir = new File(folderDir);
            try{
                if(dir.mkdir()){
                    Toast.makeText(context, R.string.NewSubjectCreated, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, R.string.NewSubjectFail, Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public String getSubjectName(){
        return subjectName;
    }

    public boolean existSubject(){

        File folder = new File(sdcard);

        return folder.exists();
    }
    public void createSubject(){
        File dir = new File(sdcard);
        try{
            if(dir.mkdir()){
            }
            else{
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean EditSubject(String rename){
        Boolean result = Rename(rename);
        return result;
    }

    private boolean Rename(String rename){


        File dir = new File(folderDir);
        File newfile=new File(sdcard+"/"+rename);

        subjectName = rename;
        folderDir = sdcard+"/"+subjectName;

        return dir.renameTo(newfile);
    }

    public void delete(){
        if(deleteDirectory(new File(folderDir))){
            Toast.makeText(context, R.string.DeleteSubject, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, R.string.DeleteSubjectFail, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    @Override
    public int compareTo(Subject subject) {
        return getSubjectName().compareTo(subject.getSubjectName());
    }

}
