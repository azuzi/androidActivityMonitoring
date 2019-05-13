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

import android.widget.TextView;
import android.widget.Toast;

import com.dead.acctivi_classification.distanceAlgorithm.DistanceAlgorithm;
import com.dead.acctivi_classification.distanceAlgorithm.EuclideanDistance;


import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";


    private HandlerThread handlerThread = new HandlerThread("thread");
    private Handler threadHandler;

    private final int REQUEST_CODE = 101;
    private static int BUF_SIZE = 48;
    private final ReentrantLock bufferLock = new ReentrantLock();

    RunTimeCalculations calculations = new RunTimeCalculations();
    private ArrayList<Float> dataY = new ArrayList<Float>();
    private ArrayList<Float> dataZ = new ArrayList<Float>();

    private DistanceAlgorithm[] distanceAlgorithms = {new EuclideanDistance()};
    private Classifier classifier;
    private List<DataPoint> listDataPoint = new ArrayList<>();
    private List<DataPoint> listDataPointOriginal = new ArrayList<>();



    TextView activity;
    private SensorManager mSensorManager;
    private Sensor mSensorAccelero;

    float avgY,avgZ, varY,varZ,sdY,sdZ;


    // TextViews to display current sensor values
    private Button btStart, btStop;

    private int K;
    private double spRatio;


    //float avgX, avgY, avgZ, varX, varY, varZ, sdX, sdY, sdZ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        btStart = (Button) findViewById(R.id.btStart);
        btStop = (Button) findViewById(R.id.btStop);
        activity = (TextView) findViewById(R.id.textViewZ);

        //mSensorGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorAccelero = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        K = 11;
        spRatio = 0.8;

        String sensor_error = getResources().getString(R.string.error_no_sensor);
        start();

        if (mSensorAccelero == null) {
            activity.setText(sensor_error);
        }

        classifier = new Classifier();
        populateList();
        //runClassifier();
        classifier.addTrainData();




        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start();

            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper());
        threadHandler.postDelayed(new YRunnable(), 2000);


        //Log.d(TAG, String.valueOf(dataY.size()));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();

        if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION) {
            bufferLock.lock();
            try {
                if (dataY.size() > 10) {
                    Log.d(TAG, String.valueOf(dataY));
                    dataY.remove(0);
                }
                dataY.add(event.values[1]);
                if (dataZ.size() <= 10) {
                    dataY.remove(0);
                }
                dataZ.add(event.values[2]);
            }finally {
                bufferLock.unlock();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        handlerThread.quit();
    }

    private void populateList() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open
                    ("analised.csv")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] point = line.split(",");
                double mY = Double.parseDouble(point[0]);
                double vY = Double.parseDouble(point[1]);
                double sdY = Double.parseDouble(point[2]);
                double mZ = Double.parseDouble(point[3]);
                double vZ = Double.parseDouble(point[4]);
                double sdZ = Double.parseDouble(point[5]);
                int category = Integer.parseInt(point[6]);
                DataPoint dataPoint = new DataPoint(mY, vY, sdY, mZ, vZ, sdZ, Category.values()[category]);
                listDataPointOriginal.add(new DataPoint(dataPoint));
                listDataPoint.add(dataPoint);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
        if (mSensorAccelero != null) {
            mSensorManager.registerListener(this, mSensorAccelero, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    public void stop() {
        mSensorManager.unregisterListener(this);
        Toast.makeText(getBaseContext(), "Data Recording Stopped", Toast.LENGTH_LONG).show();
    }

    private class YRunnable implements Runnable{
        @Override
        public void run() {

            bufferLock.lock();
            try {
                avgY = calculations.findAverage(dataY);
                varY = calculations.findVariance(dataY, avgY);
                sdY = calculations.findStandardDeviation(varY);
                avgZ = calculations.findAverage(dataZ);
                varZ = calculations.findVariance(dataY, avgZ);
                sdZ = calculations.findStandardDeviation(varZ);

            } finally {
                bufferLock.unlock();
            }
            //threadHandler.post(new Predict()); threadHandler.post(new YRunnable());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Category category = classifier.predictNew(avgY, varY, sdY, avgZ, varZ, sdZ);
                    activity.setText(category.toString());
                }
            });


        }
    }



}
