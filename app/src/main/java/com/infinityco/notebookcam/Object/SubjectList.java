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

public class SubjectList {

    private Context context;
    private ArrayList<Subject> subjects;
    private String Folder;

    public SubjectList(Context context,String Folder){
        this.context = context;

        this.Folder = Folder;
        subjects = new ArrayList<>();
    }

    public boolean isEmpty(){
        return subjects.isEmpty();
    }

    public void add(Subject subject){
        subjects.add(subject);
    }

    public void LoadSubjects(){

        subjects = new ArrayList<>();
        Subject subject = new Subject(context);


        if(!subject.existSubject())
            subject.createSubject();

        File dir = new File(Environment.getExternalStorageDirectory().toString()+"/NoteBookCam/"+Folder);
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        File[] files = dir.listFiles(fileFilter);

        for(int i=0;i<files.length;i++){
            String subjectName = files[i].getName();
            subject = new Subject(context,subjectName,Folder);
            subjects.add(subject);
        }
        Collections.sort(subjects);
    }

    public Subject get(int pos){
        return subjects.get(pos);
    }

    public int size(){
        return subjects.size();
    }
}
