package com.dead.acctivi_classification;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.dead.acctivi_classification.distanceAlgorithm.DistanceAlgorithm;
import com.dead.acctivi_classification.distanceAlgorithm.EuclideanDistance;


import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final int REQUEST_CODE = 101;

    private DistanceAlgorithm[] distanceAlgorithms = {new EuclideanDistance()};
    private Classifier classifier;
    private List<DataPoint> listDataPoint = new ArrayList<>();
    private List<DataPoint> listDataPointOriginal = new ArrayList<>();
    RunTimeCalculations calculations = new RunTimeCalculations();


    private TextView activity;
    private SensorManager mSensorManager;
    private Sensor mSensorAccelero;


    // TextViews to display current sensor values
    private Button btStart, btStop;

    private int K;
    private double spRatio;
    private ArrayList<Float> dataY = new ArrayList<Float>();
    private ArrayList<Float> dataZ = new ArrayList<Float>();

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
        mSensorAccelero = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        K = 11;
        spRatio = 0.8;

        String sensor_error = getResources().getString(R.string.error_no_sensor);

        if (mSensorAccelero == null) {
            activity.setText(sensor_error);
        }

        classifier = new Classifier();
        populateList();
        runClassifier();
        classifier.addTrainData();


        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                //Toast.makeText(getBaseContext(), "Data Recording Started", Toast.LENGTH_LONG).show();
                //classifier.classify();
                //activity.setText("Accuracy = " + classifier.getAccuracy());
                //getDelay(300);
                // here we have to give the calculated run time values

            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //int sensorType = event.sensor.getType();

        float currentValueY = event.values[1];
        float currentValueZ = event.values[2];

        if (dataY.size() < 11) {
            dataY.add(currentValueY);
        }
        if (dataZ.size() < 11) {

            dataZ.add(currentValueZ);
        }


        if (dataY.size() == 10 && dataZ.size() == 10) {
            activity.setText("Y "+dataY.size()+ " "+"Z " +dataZ.size());
            Toast.makeText(getBaseContext(), "Calculating", Toast.LENGTH_LONG).show();

            //mSensorManager.unregisterListener(this);

            float avgY = calculations.findAverage(dataY);
            float varY = calculations.findVariance(dataY, avgY);
            float sdY = calculations.findStandardDeviation(varY);
            float avgZ = calculations.findAverage(dataZ);
            float varZ = calculations.findVariance(dataY, avgZ);
            float sdZ = calculations.findStandardDeviation(varZ);

            Category category = classifier.predictNew(avgY, varY, sdY, avgZ, varZ, sdZ);
            activity.setText(category.toString());

            dataZ.clear();
            dataY.clear();
            try {
                //set time in mili
                Toast.makeText(getBaseContext(), "Sleeping....", Toast.LENGTH_LONG).show();
                sleep(3000);

                //

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            //start();

        }

        //activity.setText("Please  Wait......");


    }
    //\Users\ZIZOU\AndroidStudioProjects\Acctivi_Classification\app\build\intermediates\split-apk\debug\slices\slice_1.apk.

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
}
