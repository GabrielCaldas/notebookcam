package com.infinityco.notebookcam.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinityco.notebookcam.Adapters.AppColorAdapter;
import com.infinityco.notebookcam.Adapters.MenuAdapter;
import com.infinityco.notebookcam.Adapters.OptionAdapter;
import com.infinityco.notebookcam.Object.Settings_values;
import com.infinityco.notebookcam.R;

import java.security.PrivateKey;

public class OptionsActivity extends AppCompatActivity {

    //Ui
    private Button btBack;
    private TextView tvSettings;
    private ImageView ivTopColor;
    private RecyclerView rvOptions;

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(45);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(MainActivity.getAppColor());
        }
        setContentView(R.layout.activity_options);

        Init();
        SetUpUI();
        SetUpRV();
    }

    private void Init(){
        tvSettings = (TextView) findViewById(R.id.tv_settings);
        ivTopColor = (ImageView) findViewById(R.id.ivTopBarColor);
        rvOptions = (RecyclerView) findViewById(R.id.rvOptinos);
        btBack = (Button) findViewById(R.id.btOptionsBack);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });

        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });
    }
    private void SetUpUI(){
        tvSettings.setTypeface(MainActivity.getTyperFaceBold());
        ivTopColor.setBackgroundColor(MainActivity.getAppColor());
    }
    private void SetUpRV(){
        rvOptions.setHasFixedSize(true);
        rvOptions.setLayoutManager(new LinearLayoutManager(this));
        rvOptions.setAdapter(new OptionAdapter(this,MainActivity.getTyperFace()));

        ((OptionAdapter) rvOptions.getAdapter()).setOnItemClickListener(new OptionAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });
    }
}
