package com.dead.acctivi_classification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.Image;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

class DrawParticles extends View {
    Paint mPaint;
    Bitmap mBitmap;
    Canvas canvas;

    Bitmap workingBitmap;
    Bitmap mutableBitmap;


    private final static double xScaling = 500 / 14.33; // gives x pixels per meter
    private final static double yScaling = 300 / 47.50; // gives y pixels per meter

    public DrawParticles(Context context) {
        super(context);
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.Floor, myOptions);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);

        workingBitmap = Bitmap.createBitmap(mBitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        canvas = new Canvas(mutableBitmap);

    }

    public void drawParticles(ImageView iV, Particles[] p, Position currentPosition) {

        for (Particles particle : p) {

            int x = (int) (particle.getX() * xScaling);
            int y = (int) (particle.getY() * yScaling);

            canvas.drawCircle(x, y, 1, mPaint);

            iV.setAdjustViewBounds(true);
            iV.setImageBitmap(mutableBitmap);
        }

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        int x = (int) (currentPosition.getX() * xScaling);
        int y = (int) (currentPosition.getY() * yScaling);

        canvas.drawCircle(x, y, 3, mPaint);

        iV.setAdjustViewBounds(true);
        iV.setImageBitmap(mutableBitmap);
    }

    public void clearPanel(ImageView floorPlanImageView) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(mutableBitmap);
        floorPlanImageView.setAdjustViewBounds(true);
        floorPlanImageView.setImageBitmap(mutableBitmap);
    }

}