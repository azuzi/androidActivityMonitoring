package com.dead.acctivi_classification;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dead.acctivi_classification.distanceAlgorithm.DistanceAlgorithm;
import com.dead.acctivi_classification.distanceAlgorithm.EuclideanDistance;


import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";


    private HandlerThread movementThread = new HandlerThread("thread");
    private Handler threadHandler;

//    private final ReentrantLock bufferLock = new ReentrantLock();

    RunTimeCalculations calculations = new RunTimeCalculations();
    private ArrayList<Float> dataX = new ArrayList<Float>();
    private ArrayList<Float> dataY = new ArrayList<Float>();
    private ArrayList<Float> dataZ = new ArrayList<Float>();

    private DistanceAlgorithm[] distanceAlgorithms = {new EuclideanDistance()};
    private Classifier classifier;
    private List<DataPoint> listDataPoint = new ArrayList<>();
    private List<DataPoint> listDataPointOriginal = new ArrayList<>();



    private TextView activity;
    private ImageView floor_map;
    private SensorManager mSensorManager;
    private Sensor mSensorAccelero;
    private Sensor mAccelero;
    private Sensor magneto;

    float avgX,avgY,avgZ, varX,varY,varZ,sdX,sdY,sdZ;
    private Button btStart, btStop;

    private int K;
    private double spRatio;


    private int mAzimuth = 0; // degree
    float[] gData = new float[3]; // accelerometer
    float[] mData = new float[3]; // magnetometer
    float[] rMat = new float[9];
    float[] iMat = new float[9];
    float[] orientation = new float[3];


    private String peviousState;
    private String currentAngle;
    private String PreviusAngle;
    private static String state;
    private static int stepCount = 0;
    private static int walkingFrequency = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btStart = (Button) findViewById(R.id.btStart);
        btStop = (Button) findViewById(R.id.btStop);
        activity = (TextView) findViewById(R.id.textViewZ);
        floor_map = (ImageView) findViewById(R.id.floormap);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelero = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mAccelero = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneto = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        K = 11;
        spRatio = 0.8;

        String sensor_error = getResources().getString(R.string.error_no_sensor);
        start();

        if (mSensorAccelero == null) {activity.setText(sensor_error);}
        if (mAccelero == null) {activity.setText(sensor_error);}
        if (magneto == null) {activity.setText(sensor_error);}
        else{ activity.setText(" Waiting for Data");}

        classifier = new Classifier();
        populateList();
        //runClassifier();


        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start();
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // stop();
            }
        });
        state = "Idle";
        movementThread.start();
        threadHandler = new Handler(movementThread.getLooper());
        threadHandler.postDelayed(new movementDetector(), 100);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//
        int sensorType = event.sensor.getType();
        float[] data;
        if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION) {
            try {
                if (dataX.size() > 30) {
                    dataX.remove(0);
                }
                dataX.add(event.values[0]);

                if (dataY.size() > 30) {
                    dataY.remove(0);
                }
                dataY.add(event.values[1]);

                if (dataZ.size() > 30) {
                    dataZ.remove(0);
                }
                dataZ.add(event.values[2]);
            }finally {
            }
            processsData();
        }

        if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            mData = event.values.clone();
        }
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            gData = event.values.clone();
        }
        if ( SensorManager.getRotationMatrix( rMat, iMat, gData, mData ) ) {
            mAzimuth= (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;
            Float aa = Float.valueOf(mAzimuth);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        movementThread.quit();
        stop();
    }


    private void populateList() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open
                    ("analised.csv")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] point = line.split(",");
                double vX = Double.parseDouble(point[0]);
                double vY = Double.parseDouble(point[1]);
                double vZ = Double.parseDouble(point[2]);
                double sdX = Double.parseDouble(point[3]);
                double sdY = Double.parseDouble(point[4]);
                double sdZ = Double.parseDouble(point[5]);
                int category = Integer.parseInt(point[6]);
                DataPoint dataPoint = new DataPoint(vX, vY, vZ, sdX, sdY, sdZ, Category.values()[category]);
                listDataPointOriginal.add(new DataPoint(dataPoint));
                listDataPoint.add(dataPoint);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        classifier.setDistanceAlgorithm(distanceAlgorithms[0]);

        classifier.setK(11);
        classifier.setListDataPoint(listDataPoint);
        classifier.addTrainData();
        listDataPoint.clear();
        listDataPoint.addAll(classifier.getListTrainData());

    }

    void runClassifier() {
        classifier.reset();
        classifier.setDistanceAlgorithm(distanceAlgorithms[0]);
        classifier.setK(11);
        classifier.setSplitRatio(0.8);
        classifier.setListDataPoint(listDataPoint);
        classifier.splitData();
        listDataPoint.clear();
        listDataPoint.addAll(classifier.getListTestData());
        listDataPoint.addAll(classifier.getListTrainData());
    }

    public void start() {
        Log.d(TAG, "olga::start");
        super.onStart();
        if (mSensorAccelero != null) {
            mSensorManager.registerListener(this, mSensorAccelero, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mAccelero != null) {
            mSensorManager.registerListener(this, mAccelero, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magneto != null) {
            mSensorManager.registerListener(this, magneto, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Toast.makeText(getBaseContext(), "Data Recording Started", Toast.LENGTH_LONG).show();
    }



    public void stop() {
        Log.d(TAG, "olga::stop");
        super.onStop();
        mSensorManager.unregisterListener(this);
        Toast.makeText(getBaseContext(), "Data Recording Stopped", Toast.LENGTH_LONG).show();
    }

    public void processsData () {
        Log.d(TAG, "olga::processing data");
        avgX = calculations.findAverage(dataX);
        avgY = calculations.findAverage(dataY);
        avgZ = calculations.findAverage(dataZ);

        varX = calculations.findVariance(dataX, avgX);
        varY = calculations.findVariance(dataY, avgY);
        varZ = calculations.findVariance(dataY, avgZ);

        sdX = calculations.findStandardDeviation(varX);
        sdY = calculations.findStandardDeviation(varY);
        sdZ = calculations.findStandardDeviation(varZ);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Category category = classifier.predictNew(varX, varY,varZ,sdX,sdY,sdZ);
                String cat = category.toString();
                state = cat;
                activity.setText(cat);


            }
        });
    }
    static long StartTime ;
    static long StopTime ;

    static class movementDetector implements Runnable{
        @Override
        public void run() {
            if (state == "Idle" ){
                StartTime = System.nanoTime(); //set sta rt time 0
            }
            while (state == "Walk" ){
                StopTime = System.nanoTime();
                stepCount =(int)(StopTime - StartTime)/1000000000 * walkingFrequency;
            }

        }
    }

}
