package com.infinityco.notebookcam.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.infinityco.notebookcam.R;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Launcher extends AppCompatActivity {

    private TextView tvAppName, tvAppDevelopment;
    private boolean isShowing,readToGo=false;

    @Override
    protected void onResume() {
        super.onResume();
        isShowing = true;
        if(readToGo){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Start();
            } else Launcher();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShowing = false;
    }

    @TargetApi(19)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        tvAppName = (TextView) findViewById(R.id.tbLauncherAppName);
        tvAppDevelopment = (TextView) findViewById(R.id.tbLauncherAppDevelopment);

        Typeface tittle_type_bold = Typeface.createFromAsset(getAssets(),"tittle_font_bold.ttf");
        Typeface tittle_type = Typeface.createFromAsset(getAssets(),"tittle_font.ttf");

        tvAppName.setTypeface(tittle_type_bold);
        tvAppDevelopment.setTypeface(tittle_type);


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                if(isShowing) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermission()) {
                            Launcher();
                        }
                    } else Launcher();
                }
                else
                    readToGo=true;
            }
        }, 1500);

        if (!checkPermission()) {
            AlertDialog();
        }
    }

    boolean testrequest = false;
    public void Start(){
        if(checkPermission()) {
            Launcher();
        }
        else {
            if(!testrequest) {
                testrequest = true;
                AlertDialog();
                //requestPermission();
            }
        }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultwrite = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean retorno = false;
        if (result == PackageManager.PERMISSION_GRANTED && resultwrite == PackageManager.PERMISSION_GRANTED){
            retorno =  true;

        }
        return retorno;
    }

    private static final int PERMISSION_REQUEST_CODE = 1;
    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Launcher();
        } else {
            if(!isShowingAD) {
                AlertDialog();
            }
        }

        return;
    }

    private boolean isShowingAD = false;
    public void AlertDialog(){
        final android.app.AlertDialog alertDialog;
        final android.app.AlertDialog.Builder RecDialog = new android.app.AlertDialog.Builder(this);
        RecDialog.setTitle(R.string.Allow_access);
        RecDialog.setMessage(R.string.Allow_access_mensage);

        isShowingAD = true;


        RecDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here. The listner is bellow;
                    }
                });
        RecDialog.setNegativeButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here. The listner is bellow;
                    }
                });

        alertDialog = RecDialog.create();


        alertDialog.getWindow().setBackgroundDrawable(null);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();

        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                alertDialog.dismiss();
                isShowingAD = false;
            }
        });
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                isShowingAD = false;
                finish();
            }
        });
    }

    private void Launcher(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
