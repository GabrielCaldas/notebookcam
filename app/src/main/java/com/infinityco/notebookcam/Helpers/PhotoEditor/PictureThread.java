package com.infinityco.notebookcam.Helpers.PhotoEditor;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.infinityco.notebookcam.R;

import net.alhazmy13.imagefilter.ImageFilter;

import java.util.logging.LogRecord;

public class PictureThread extends Thread {

    private ImageView imageView;
    private Bitmap bitmap,temp_bitmap,beforeFilter;
    private Canvas canvas;
    private Paint paint;
    private ColorMatrix colorMatrix;
    private ColorMatrixColorFilter colorMatrixColorFilter;
    private Handler handler;
    private boolean running = false;
    private float contrast = 1,brightness =0,red=1,blue=1,green=1;

    public PictureThread(ImageView imageView, Bitmap bitmap){
        this.imageView = imageView;
        this.bitmap = Bitmap.createBitmap(bitmap);
        temp_bitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        canvas = new Canvas(temp_bitmap);
        paint = new Paint();
        handler = new Handler();
        start();
        changeContrastBrightness(contrast,brightness);
    }

    public void upDateBitmap(Bitmap bmp){
        this.bitmap = Bitmap.createBitmap(bmp);
        temp_bitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        canvas = new Canvas(temp_bitmap);
        changeContrastBrightness(contrast,brightness);
    }

    public void undo(Bitmap bmp){
        this.bitmap = Bitmap.createBitmap(bmp);
        temp_bitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        canvas = new Canvas(temp_bitmap);

        contrast = 1;
        brightness =0;
        red=1;
        blue=1;
        green=1;

        changeContrastBrightness(contrast,brightness);
    }

    public void changeContrastBrightness(float contrast,float brightness){
        this.contrast = contrast;
        this.brightness = brightness;
        colorMatrix = new ColorMatrix(new float[]
                {
                        contrast*red, 0, 0, 0, brightness,
                        0, contrast*green, 0, 0, brightness,
                        0, 0, contrast*blue, 0, brightness,
                        0, 0, 0, 1, 0
                });
        colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        running = true;
    }

    public void adjustRed(float red){



        this.red = red;
        colorMatrix = new ColorMatrix(new float[]
                {

                        red/contrast, 0, 0, 0, brightness,
                        0, green/contrast, 0, 0, brightness,
                        0, 0, blue/contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });
        colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        running = true;
    }
    public void adjustGreen(float green){

        this.green = green;
        colorMatrix = new ColorMatrix(new float[]
                {
                        contrast*red, 0, 0, 0, brightness,
                        0, contrast*green, 0, 0, brightness,
                        0, 0, contrast*blue, 0, brightness,
                        0, 0, 0, 1, 0
                });
        colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        running = true;
    }
    public void adjustBlue(float blue){

        this.blue = blue;
        colorMatrix = new ColorMatrix(new float[]
                {
                        contrast*red, 0, 0, 0, brightness,
                        0, contrast*green, 0, 0, brightness,
                        0, 0, contrast*blue, 0, brightness,
                        0, 0, 0, 1, 0
                });

        colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        running = true;
    }

    public void applayFilter(int filter,int[] config){

       if(beforeFilter==null){
           beforeFilter = Bitmap.createBitmap(temp_bitmap);
       }

       setFilter(filter,config);

    }


    private void setFilter(int i,int[] confg){
        if(i==0){
            temp_bitmap = Bitmap.createBitmap(beforeFilter);
        }
        else if(i==1){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.GRAY);
        }
        else if(i==2){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.RELIEF);
        }
        else if(i==3){
            if(confg[0]==0){
                confg[0]=1;
            }
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.AVERAGE_BLUR,10/confg[0]);
        }
        else if(i==4){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.NEON,confg[0],confg[1],confg[2]);
        }
        else if(i==5){
            if(confg[0]==0){
                confg[0]=1;
            }
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.PIXELATE,beforeFilter.getWidth()/confg[0]);
        }
        else if(i==6){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.TV);
        }
        else if(i==7){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.INVERT);
        }
        else  if(i==8){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.BLOCK);
        }
        else if(i==9){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.OLD);
        }
        else if(i==10){
            int width = beforeFilter.getWidth();
            int height = beforeFilter.getHeight();
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.LIGHT,width / 2, height / 2, Math.min(width / 2, confg[0]*height / 2));
        }
        else if(i==11){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.LOMO);
        }
        else if(i==12){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.HDR);
        }
        else if(i==13){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.SOFT_GLOW);
        }
        else if(i==14){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.SKETCH);
        }
        else if(i==15){
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.GOTHAM);
        }
        else if(i==16){
            double sigma = (double)confg[0]/10;
            temp_bitmap = ImageFilter.applyFilter(beforeFilter, ImageFilter.Filter.GAUSSIAN_BLUR,sigma);
        }


        upDateBitmap(temp_bitmap);
    }


    @Override
    public void run(){
        super.run();
        while (true){
            if(running){
                canvas.drawBitmap(bitmap,0,0,paint);

                handler.post(new Runnable() {
                    @Override
                    public void run(){
                        imageView.setImageBitmap(temp_bitmap);
                        running = false;
                    }
                });
            }
        }
    }

    public Bitmap getOriginalBitmap() {
        return bitmap;
    }

    public Bitmap getBitmap() {
        return temp_bitmap;
    }
}
