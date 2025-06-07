package com.infinityco.notebookcam.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.infinityco.notebookcam.Adapters.rvAdapterFilters;
import com.infinityco.notebookcam.Helpers.PhotoEditor.Cropper.CropImageView;
import com.infinityco.notebookcam.Helpers.PhotoEditor.PictureThread;
import com.infinityco.notebookcam.Object.Folder;
import com.infinityco.notebookcam.Object.Photo;
import com.infinityco.notebookcam.R;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class EditPhotoActivity extends Activity {

    private TextView tvBrightnessValue,tvContrastValue,tvFilterName;
    private CropImageView ivImageCut;
    private ImageView ivImage;
    private Bitmap savedImage;
    private PictureThread pictureThread;
    private LinearLayout llSliders,llCut,llColors,llFilters,llFilterBar,llFilterBarNeon;
    private Button btCutCut,btCutCancel;
    private RecyclerView rvFilters;
    private SeekBar sbFilterRN,sbFilterGN,sbFilterBN,sbFilter;

    private String SubjectName,Folder_name,Folder_name_tv,PhotoPath;
    private Folder folder;


    private void setupFullscreen(){
        int currentApiVersion;

        // This work only for android 4.4+
        if(android.os.Build.VERSION.SDK_INT >= 19)
        {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }



    private boolean justEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        justEdit = getSubject();

        if(justEdit){
            setupFullscreen();

            setContentView(R.layout.activity_edit_photo);

            initViews();
        }
        else {
            startCamera();

            setupFullscreen();

            setContentView(R.layout.activity_edit_photo);
        }
    }

    private void initViews() {

        //Tools Layout
        llColors = findViewById(R.id.llColors);
        llCut = findViewById(R.id.llCut);
        llSliders = findViewById(R.id.llSliders);
        llFilters = findViewById(R.id.llFilters);
        llFilterBarNeon = findViewById(R.id.llFIlterBarNeon);

        //Views Init
        ivImageCut = findViewById(R.id.ivEditorCut);
        ivImage = findViewById(R.id.ivEditor);
        tvBrightnessValue = findViewById(R.id.txtBrightnessValue);
        tvContrastValue = findViewById(R.id.txtContrastValue);

        //PictreTread Init
        pictureThread = new PictureThread(ivImage,BitmapFactory.decodeFile(PhotoPath));

        //setUp Image View
        ivImage.setImageBitmap(pictureThread.getBitmap());




        //Brightness and Contrast
        SeekBar sbContrast = findViewById(R.id.sbContrast);
        SeekBar sbBrightness = findViewById(R.id.sbBrightness);

        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeContrastBrightness(contrast,((float) i-100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<260&i>250){
                    i=255;
                }
                changeContrastBrightness(((float) i)/255,brightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Colors
        SeekBar sbRed = findViewById(R.id.sbRed);
        SeekBar sbGreen = findViewById(R.id.sbGreen);
        SeekBar sbBlue = findViewById(R.id.sbBlue);

        sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<105 & i>95){
                    i=100;
                }
                pictureThread.adjustRed(((float)i)/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<105 & i>95){
                    i=100;
                }
                pictureThread.adjustGreen(((float)i)/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<150 & i>95){
                    i=100;
                }
                pictureThread.adjustBlue(((float)i)/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Buttons
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llFilterBarNeon.setVisibility(View.GONE);
                llFilters.setVisibility(View.GONE);
                llSliders.setVisibility(View.GONE);
                llColors.setVisibility(View.GONE);
            }
        });

        Button btCut = findViewById(R.id.btEditorCut);
        btCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpCutLayout();
            }
        });

        Button btSliders = findViewById(R.id.btEditSliders);
        btSliders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpSlidersLayout();
            }
        });

        Button btback = findViewById(R.id.btEditorBack);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btbackText = findViewById(R.id.btEditorBackText);
        btbackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btSave = findViewById(R.id.imgSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        Button btColors = findViewById(R.id.btEditorColors);
        btColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpColorLayout();
            }
        });

        Button btFilters = findViewById(R.id.btEditorFilters);
        btFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpFiltersLayout();
            }
        });


        Button btUndo = findViewById(R.id.imgUndo);
        btUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDialog();
            }
        });
    }


    //SetupCamera-----------------------------------------------------------------------------------

    private boolean getSubject(){
        Bundle extras = getIntent().getExtras();
        Boolean retorno = false;
        if(extras == null) {
            SubjectName= null;
        } else {
            PhotoPath = extras.getString("Photo");
            if(!PhotoPath.equals("ljngiogiaeafofi")){
                retorno = true;
            }
            else {
                SubjectName = extras.getString("Subject");
                Folder_name = extras.getString("Folder");
                Folder_name_tv = Folder_name;
                folder = new Folder(this,Folder_name);
            }
        }


        return retorno;
    }

    private static final int CAMERA_REQUEST = 1888;
    private Uri imageUri;
    public void startCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "NoteBook Cam");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Save(getRealPathFromURI(imageUri),folder.getDirectory()+SubjectName);
                    showEditDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void Save(final String photoPath,final String path) {

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        final String Date = simpleDateFormat.format(currentTime).toString();


        saving=true;

        char[] ext = new char[4];
        ext[0]=photoPath.charAt(photoPath.length()-4);
        ext[1]=photoPath.charAt(photoPath.length()-3);
        ext[2]=photoPath.charAt(photoPath.length()-2);
        ext[3]=photoPath.charAt(photoPath.length()-1);

        final File newfile=new File(path, Date + String.valueOf(ext));





        File dir = new File(photoPath);
        dir.renameTo(newfile);
        galleryAddPic(newfile);
        galleryAddPic(dir);
        if(dir.exists()){
            dir.delete();
        }
        try{
            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                deleteLatest();
            }
        }
        catch (Exception e){
        }

        PhotoPath = newfile.getPath();
        saving=false;
        initViews();

    }
    private void galleryAddPic(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void deleteLatest() {
        // TODO Auto-generated method stub
        File f = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera" );

        //Log.i("Log", "file name in delete folder :  "+f.toString());
        File [] files = f.listFiles();

        //Log.i("Log", "List of files is: " +files.toString());
        if(files.length>0) {
            Arrays.sort(files, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {

                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        //         Log.i("Log", "Going -1");
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        //     Log.i("Log", "Going +1");
                        return 1;
                    } else {
                        //     Log.i("Log", "Going 0");
                        return 0;
                    }
                }

            });

            //Log.i("Log", "Count of the FILES AFTER DELETING ::"+files[0].length());
            files[0].delete();
            galleryAddPic(files[0]);
        }

    }


    //SetupLayouts----------------------------------------------------------------------------------
    private void setUpCutLayout(){
        if(llCut.getVisibility()==View.VISIBLE){
            llCut.setVisibility(View.GONE);
            ivImageCut.setVisibility(View.GONE);
            if(ivImage.getVisibility()!=View.VISIBLE) {
                ivImage.setVisibility(View.VISIBLE);
            }
        }
        else {
            llCut.setVisibility(View.VISIBLE);
            ivImageCut.setVisibility(View.VISIBLE);
            ivImage.setVisibility(View.GONE);
            ivImageCut.setImageBitmap(pictureThread.getOriginalBitmap());
            if(btCutCut==null||btCutCancel==null){
                btCutCut = findViewById(R.id.btEditorCutCut);
                btCutCancel = findViewById(R.id.btEditorCutCancel);

                btCutCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setUpCutLayout();
                    }
                });

                btCutCut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImage();
                        setUpCutLayout();
                    }
                });
            }
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
    }

    private void setUpSlidersLayout(){
        if(llSliders.getVisibility()==View.VISIBLE){
            llSliders.setVisibility(View.GONE);
        }
        else {
            llSliders.setVisibility(View.VISIBLE);
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);
        ivImageCut.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        if(ivImage.getVisibility()!=View.VISIBLE) {
            ivImage.setVisibility(View.VISIBLE);
        }
    }

    private void setUpColorLayout(){
        if(llColors.getVisibility()==View.VISIBLE){
            llColors.setVisibility(View.GONE);
        }
        else {
            llColors.setVisibility(View.VISIBLE);
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
        ivImageCut.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        if(ivImage.getVisibility()!=View.VISIBLE) {
            ivImage.setVisibility(View.VISIBLE);
        }
    }

    private void setUpFiltersLayout(){
        if(llFilters.getVisibility()==View.VISIBLE){
            llFilters.setVisibility(View.GONE);
        }
        else {
            llFilters.setVisibility(View.VISIBLE);
        }

        if(rvFilters == null){
            rvFilters = findViewById(R.id.rvFilters);

            rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvFilters.setHasFixedSize(true);

            rvFilters.setAdapter(new rvAdapterFilters(this,pictureThread.getOriginalBitmap()));

            sbFilterRN = findViewById(R.id.sbRedNeon);
            sbFilterGN = findViewById(R.id.sbGreenNeon);
            sbFilterBN = findViewById(R.id.sbBlueNeon);

            llFilterBar = findViewById(R.id.llFIlterBar);
            tvFilterName = findViewById(R.id.tvFilterName);
            sbFilter = findViewById(R.id.sbFilter);

            Button btNeonClose = findViewById(R.id.btEditorFiltersNeonClose);
            btNeonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llFilters.setVisibility(View.VISIBLE);
                    llFilterBarNeon.setVisibility(View.GONE);
                }
            });

            ((rvAdapterFilters) rvFilters.getAdapter()).setOnItemClickListener(new rvAdapterFilters.MyClickListener() {
                @Override
                public void onItemClick(final int position, View v) {

                    selectFilter(position);
                }

            });
        }


        llFilterBarNeon.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
        ivImageCut.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);
        if(ivImage.getVisibility()!=View.VISIBLE) {
            ivImage.setVisibility(View.VISIBLE);
        }
    }



    //Tools-----------------------------------------------------------------------------------------

    //Cut
    private void cropImage(){

        pictureThread.upDateBitmap(ivImageCut.getCroppedImage());

        ivImage.setImageBitmap(pictureThread.getBitmap());

    }

    //ContrastBrightness
    float brightness=0,contrast=1;
    public void changeContrastBrightness(float ct,float bn) {

        brightness = bn;
        contrast = ct;


        pictureThread.changeContrastBrightness(contrast,brightness);



        tvBrightnessValue.setText(brightness+"");
        tvContrastValue.setText(((int)(contrast*100))/10+"");
    }

    //Filters
    private void selectFilter(final int position){
        if(position==3){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterAVERAGE_BLUR));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position ==4){
            llFilterBarNeon.setVisibility(View.VISIBLE);
            llFilterBar.setVisibility(View.GONE);
            llFilters.setVisibility(View.GONE);
            sbFilterRN.setProgress(100);
            sbFilterGN.setProgress(100);
            sbFilterBN.setProgress(100);

            sbFilterRN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[3];

                            confg[0] = sbFilterRN.getProgress();
                            confg[1] = sbFilterGN.getProgress();
                            confg[2] = sbFilterBN.getProgress();

                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
            sbFilterGN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[3];

                            confg[0] = sbFilterRN.getProgress();
                            confg[1] = sbFilterGN.getProgress();
                            confg[2] = sbFilterBN.getProgress();

                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
            sbFilterBN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[3];

                            confg[0] = sbFilterRN.getProgress();
                            confg[1] = sbFilterGN.getProgress();
                            confg[2] = sbFilterBN.getProgress();

                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position==5){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterPIXELATE));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position==10){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterLIGHT));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position==16){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterGAUSSIANBLUR));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else {

            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.GONE);
            showLoading(getString(R.string.PhotoEditorApplying));
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    pictureThread.applayFilter(position,null);
                    hideLoading();
                }
            }, 1);

        }
    }

    //Undo
    private void undoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.PhotoEditorUndoMessage));
        builder.setPositiveButton(getString(R.string.PhotoEditorUndoMessagePositive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pictureThread.undo(BitmapFactory.decodeFile(PhotoPath));
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    //Progress dialog
    private ProgressDialog mProgressDialog;
    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }



    private boolean saving = false;
    @SuppressLint("MissingPermission")
    private void saveImage() {
        if(!saving) {
            saving = true;
            showLoading(getString(R.string.savingMensage));
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {

                    final File file = new File(PhotoPath);

                    if(justEdit){
                        file.delete();
                    }

                    try {
                        FileOutputStream fOut = new FileOutputStream(file);

                        pictureThread.getBitmap().compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                        MediaScannerConnection.scanFile(EditPhotoActivity.this,
                                new String[]{PhotoPath}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                                    @Override
                                    public void onMediaScannerConnected() {

                                    }

                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {

                                        savedImage = Bitmap.createBitmap(pictureThread.getBitmap());
                                        saving = false;
                                        hideLoading();
                                        //showSnackbar(getString(R.string.savingSucessMensage)+" "+Environment.getExternalStorageDirectory() + "/"+bv.getEditedPhotoDirectory());
                                    }
                                });
                    } catch (Exception e) {
                        saving = false;
                        hideLoading();
                    }
                }
            }, 1);

        }
    }

    private void createDirIfNotExists(String path) {

        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
            }
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.PhotoEditorSaveMessage));
        builder.setPositiveButton(getString(R.string.Save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(getString(R.string.PhotoEditorDiscard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.PhotoEditorWannaEditMessage));
        builder.setPositiveButton(getString(R.string.Edit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.create().show();

    }

    private  boolean equals(Bitmap b1, Bitmap b2) {
        if (b1.getWidth() == b2.getWidth() && b1.getHeight() == b2.getHeight()) {
            int[] pixels1 = new int[b1.getWidth() * b1.getHeight()];
            int[] pixels2 = new int[b2.getWidth() * b2.getHeight()];
            b1.getPixels(pixels1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
            b2.getPixels(pixels2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());
            if (Arrays.equals(pixels1, pixels2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        if(!equals(pictureThread.getOriginalBitmap(),pictureThread.getBitmap())){
            if(savedImage==null){
                showSaveDialog();
            }
            else {
                if(!equals(savedImage,pictureThread.getBitmap())){
                    showSaveDialog();
                }
                else {
                    finish();
                }
            }
        }
        else {
            finish();
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);

    }
}
