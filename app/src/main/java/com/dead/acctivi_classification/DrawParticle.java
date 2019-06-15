package com.dead.acctivi_classification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;

class DrawParticle extends View {


    private final Bitmap mBitmap;
   // private final Bitmap bitmap;
    private final Bitmap mutableBitmap;
    private final Canvas mCanvas;
    private final Paint mPaint;
    private final Bitmap workingBitmap;


    private float radius = 1;

    public DrawParticle(Context context) {
        super(context);
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.capture, myOptions);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);

        workingBitmap = Bitmap.createBitmap(mBitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        mCanvas = new Canvas(mutableBitmap);

        // mBitmap = BitmapFactory.decodeResource(getResources(), R.id.imageView);//get the floormap
       // bitmap = Bitmap.createBitmap(mBitmap);//create the bitmap

        //mutableBitmap = bitmap.reconfigure(500,300, Bitmap.Config.ARGB_8888);
       // mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);//configure it to abe able to change ant runtime

        //mCanvas = new Canvas(mutableBitmap);//create a canvas object

      //  mPaint = new Paint();//initialize paint object
      //  mPaint.setColor(Color.BLACK);//colour of the particle
    }

    public void DrawParticleView(ImageView floorPlan, Particle[] particles) {
        for (Particle particle : particles) {
            //set the x and y dimensions
            float x = particle.x;//this has to be changed according to our x and y
            float y = particle.y;
            mCanvas.drawCircle(x, y, radius, mPaint);//draw the particle
            floorPlan.setAdjustViewBounds(true);

            floorPlan.setImageBitmap(mutableBitmap);

        }
    }
}
