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

public class Folder implements Comparable<Folder>{

    private Context context;
    private String folderName, sdcard,folderDir;
    private int color;

    public Folder(Context context){
        this.context = context;
        this.sdcard = Environment.getExternalStorageDirectory().toString()+"/";
    }

    public Folder(Context context,String name){
        this.sdcard = Environment.getExternalStorageDirectory().toString()+"/";

        this.context = context;
        this.folderName = name;
        LoadColor();
    }


    public Folder(Context context,String name,int color){
        this.sdcard = Environment.getExternalStorageDirectory().toString()+"/";

        this.context = context;
        this.folderName = name;
        this.color = color;
    }

    public boolean EditFolder(String rename,int newColor){
        writeColor(newColor);
        Boolean result = Rename(rename);
        return result;
    }

    private boolean Rename(String rename){

        if(!existNotebookCamFolder()){
            createNoteBookCamFolder();
        }

        folderDir = sdcard+"NoteBookCam/"+folderName;
        File dir = new File(folderDir);
        File newfile=new File(sdcard+"NoteBookCam/"+rename);

        folderName = rename;
        folderDir = sdcard+"NoteBookCam/"+folderName;

        return dir.renameTo(newfile);
    }

    public void saveFolder(){
        if(folderName != null){

            if(!existNotebookCamFolder()){
                createNoteBookCamFolder();
            }

            folderDir = sdcard+"NoteBookCam/"+folderName;
            File dir = new File(folderDir);
            try{
                if(dir.mkdir()){
                    writeColor(color);
                    Toast.makeText(context, R.string.NewFolderCreated, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, R.string.NewFolderFail, Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public void delete(){
        if(deleteDirectory(new File(folderDir))){
            Toast.makeText(context, R.string.DeleteFolder, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, R.string.DeleteFolderFail, Toast.LENGTH_SHORT).show();
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

    private  void writeColor(int color) {
        FileOutputStream fos = null;

        try {
            final File dir = new File(folderDir);

            if (!dir.exists())
            {
                if(!dir.mkdirs()){
                    Log.e("ALERT","could not create the directories");
                }
            }

            final File myFile = new File(dir,"folder_info.txt");

            if (!myFile.exists())
            {
                myFile.createNewFile();
            }

            fos = new FileOutputStream(myFile);

            String Color = color+"";
            fos.write(Color.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void LoadColor(){
        if(folderDir==null){
            folderDir = sdcard+"NoteBookCam/"+folderName;
        }
        File file = new File(folderDir,"folder_info.txt");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        }
        catch (IOException e) {
            String c = e.getMessage();
        }
        color = Integer.parseInt(text.toString());
    }
    public int getColor(){
        return color;
    }

    public String getDirectory(){
        return sdcard+"NoteBookCam/"+folderName+"/";
    }
    public String getFolderName(){
        return folderName;
    }

    public boolean existNotebookCamFolder(){

        File folder = new File(sdcard+"NoteBookCam");

        return folder.exists();
    }
    public void createNoteBookCamFolder(){
        File dir = new File(sdcard+"NoteBookCam");
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

    @Override
    public int compareTo(Folder f) {
        return getFolderName().compareTo(f.getFolderName());
    }
}
