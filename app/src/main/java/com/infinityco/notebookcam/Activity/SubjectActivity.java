package com.infinityco.notebookcam.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.notebookcam.Adapters.PhotosAdapter;
import com.infinityco.notebookcam.Adapters.SubjectAdapter;
import com.infinityco.notebookcam.Object.Folder;
import com.infinityco.notebookcam.Object.Photo;
import com.infinityco.notebookcam.Object.PhotoList;
import com.infinityco.notebookcam.Object.Subject;
import com.infinityco.notebookcam.Object.SubjectList;
import com.infinityco.notebookcam.Object.WeakHandler;
import com.infinityco.notebookcam.R;

import org.w3c.dom.Text;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SubjectActivity extends AppCompatActivity implements View.OnClickListener{

    //Layout
    private RecyclerView rvPhotos;
    private Button btFolderBack,btNewSubject;
    private TextView tvSubjectName,tvFolderName;
    private Typeface tittle_type;
    private ImageView SubjectColor;
    private CardView newPhoto;

    //Objects
    private PhotoList Photos;
    private static PhotoList PhotosStatic;
    private AlertDialog alertDialog; //createFolderAlertDialog()
    private int color;  //createFolderAlertDialog()
    private String SubjectName,Folder_name,Folder_name_tv;
    private Folder folder;
    private int width,height;
    public boolean needLoad=false, saving = false,firstResume = true,dontMuveAdapter=true;
    private WeakHandler mHandler;


    @Override
    public void onDestroy(){
        super.onDestroy();
        PhotosStatic = null;
    }

    @Override
    public void onBackPressed() {
        Finish();
    }
    @Override
    public void onResume(){
        super.onResume();

        if(firstResume){
            firstResume=false;
            needLoad=true;
        }
        else {
            dontMuveAdapter=false;
            affterLoad();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        getSubject();
        Init();
        SetUpTypeFace();
        SetUp();
        mHandler = new WeakHandler();
        mHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if(needLoad){
                            needLoad=false;
                            LoadPhotos();
                        }
                        mHandler.postDelayed(this, 100);
                    }
                });
    }

    private void getSubject(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            SubjectName= null;
        } else {
            SubjectName= extras.getString("Subject");
            Folder_name = extras.getString("Folder");
            Folder_name_tv = Folder_name;
        }
    }
    private void Init(){
        //Layout
        rvPhotos = (RecyclerView) findViewById(R.id.rvPhotos);
        rvPhotos.setHasFixedSize(true);
        rvPhotos.setLayoutManager(new GridLayoutManager(SubjectActivity.this, 2));

        tvFolderName = (TextView) findViewById(R.id.tv_folder_name);
        btFolderBack = (Button) findViewById(R.id.btSubjectBack);
        btNewSubject = (Button) findViewById(R.id.btNewPhoto);
        tvSubjectName = (TextView) findViewById(R.id.tv_subject_name);

        SubjectColor = (ImageView) findViewById(R.id.iv_subject_color);
        newPhoto= (CardView) findViewById(R.id.card_view_new_Photo);
    }

    private void SetUpTypeFace(){
        Typeface tittle_type_bold = MainActivity.getTyperFaceBold();
        tittle_type = MainActivity.getTyperFace();


        if(Folder_name_tv.length()>6){
            String temp = "";
            char[] c = Folder_name_tv.toCharArray();
            int i=0;
            while(c[i] != " ".toCharArray()[0] && i<5){
                i++;
            }
            char[] newName = new char[i];
            for(int b=0;b<i&&b<5;b++){
                newName[b]=c[b];
            }

            Folder_name_tv = String.valueOf(newName)+"...";

        }

        tvFolderName.setText(Folder_name_tv+"/");
        tvFolderName.setTypeface(tittle_type);
        tvSubjectName.setTypeface(tittle_type_bold);
        tvSubjectName.setText(SubjectName);
        folder = new Folder(this,Folder_name);
    }

    private void SetUp(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels/2;
        width = displayMetrics.widthPixels/2;

        switch (folder.getColor()){
            case 1:
                color = getResources().getColor(R.color.folderColor1);
                SubjectColor.setBackgroundColor(getResources().getColor(R.color.folderColor1));
                break;
            case 2:
                color = getResources().getColor(R.color.folderColor2);
                SubjectColor.setBackgroundColor(getResources().getColor(R.color.folderColor2));
                break;
            case 3:
                color = getResources().getColor(R.color.folderColor3);
                SubjectColor.setBackgroundColor(getResources().getColor(R.color.folderColor3));
                break;
            case 4:
                color = getResources().getColor(R.color.folderColor4);
                SubjectColor.setBackgroundColor(getResources().getColor(R.color.folderColor4));
                break;
            case 5:
                color = getResources().getColor(R.color.folderColor5);
                SubjectColor.setBackgroundColor(getResources().getColor(R.color.folderColor5));
                break;
            case 6:
                color = getResources().getColor(R.color.folderColor6);
                SubjectColor.setBackgroundColor(getResources().getColor(R.color.folderColor6));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        newPhoto.setCardBackgroundColor(color);

        LoadPhotos();

        btFolderBack.setOnClickListener(this);
        btNewSubject.setOnClickListener(this);
    }

    private void LoadPhotos(){

        Photos = new PhotoList(SubjectActivity.this,Folder_name,SubjectName);
        Photos.LoadPhotos();

        PhotosStatic = Photos;
        affterLoad();
    }
    private void affterLoad(){
        if(!Photos.isEmpty()) {
            if(dontMuveAdapter)
                rvPhotos.setAdapter(new PhotosAdapter(SubjectActivity.this, Photos));
            else {
                if(rvPhotos.getAdapter() != null) {
                    rvPhotos.setAdapter(new PhotosAdapter(SubjectActivity.this, Photos));
                    rvPhotos.scrollToPosition(Photos.size()-1);
                }
                else {
                    dontMuveAdapter=true;
                    LoadPhotos();
                }
            }
            setClickListner();
        }
    }

    private void setClickListner(){
        if(!Photos.isEmpty()) {
            ((PhotosAdapter) rvPhotos.getAdapter()).setOnItemClickListener(new PhotosAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent i = new Intent(SubjectActivity.this, showPhoto.class);
                    i.putExtra("Position", position);

                    overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                }

                /*
                @Override
                public void onLongClick(int position,View v){
                    ShowPhotoDialog(position);
                }*/

            });
        }
    }

    public static PhotoList getPhotos(){
        return PhotosStatic;
    }


    private void Finish(){
        if(!saving) {
            Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            vibrator.vibrate(45);
        }
        else
            SavingDialog();
    }

    private void LoadingDialog(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);
        RecDialog.setCancelable(false);

        Typeface tittle_type = MainActivity.getTyperFace();




        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.HORIZONTAL);
        dialogLayout.setGravity(Gravity.LEFT);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setPadding(5,20,5,20);

        TextView loading = new TextView(this);
        loading.setPadding(10,40,0,0);
        loading.setTextSize(25);
        loading.setGravity(Gravity.CENTER_VERTICAL);
        loading.setText(R.string.Loading);
        loading.setTypeface(tittle_type);

        dialogLayout.addView(progressBar);
        dialogLayout.addView(loading);


        RecDialog.setView(dialogLayout);

        alertDialog = RecDialog.create();
        alertDialog.show();
    }
    private void SavingDialog(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);
        RecDialog.setCancelable(false);

        Typeface tittle_type = MainActivity.getTyperFace();




        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.HORIZONTAL);
        dialogLayout.setGravity(Gravity.LEFT);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setPadding(5,20,5,20);

        TextView loading = new TextView(this);
        loading.setPadding(10,40,0,0);
        loading.setTextSize(25);
        loading.setGravity(Gravity.CENTER_VERTICAL);
        loading.setText(R.string.Saving);
        loading.setTypeface(tittle_type);

        dialogLayout.addView(progressBar);
        dialogLayout.addView(loading);


        RecDialog.setView(dialogLayout);




        alertDialog = RecDialog.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.btSubjectBack:
                Finish();
                break;
            case R.id.btNewPhoto:

                Intent i = new Intent(SubjectActivity.this,EditPhotoActivity.class);
                i.putExtra("Photo", "ljngiogiaeafofi");
                i.putExtra("Folder", Folder_name);
                i.putExtra("Subject",SubjectName);
                startActivity(i);
                dontMuveAdapter=true;
                firstResume=true;
                break;

        }
    }
}
