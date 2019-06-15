package com.dead.acctivi_classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dead.acctivi_classification.distanceAlgorithm.DistanceAlgorithm;
import com.dead.acctivi_classification.distanceAlgorithm.EuclideanDistance;


public class Classifier {

    private int K;
    private double splitRatio;
    private double accuracy = 0;

    private DistanceAlgorithm distanceAlgorithm;
    private List<DataPoint> listDataPoint;
    private List<DataPoint> listTrainData;
    private List<DataPoint> listTestData;
    private List<DataPoint> listTestValidator;
    private List<Double> listDistance;

    public Classifier(){
        K = 11;
        splitRatio = 0.8;
        distanceAlgorithm = new EuclideanDistance();
        listDataPoint = new ArrayList<>();
        listTrainData = new ArrayList<>();
        listTestData = new ArrayList<>();
        listTestValidator = new ArrayList<>();
    }
    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public double getSplitRatio() {
        return splitRatio;
    }

    public void setSplitRatio(double splitRatio) {
        this.splitRatio = splitRatio;
    }

    public List<DataPoint> getListDataPoint() {
        return listDataPoint;
    }

    public void setListDataPoint(List<DataPoint> listDataPoint) {
        this.listDataPoint.clear();
        this.listDataPoint.addAll(listDataPoint);
    }

    public List<DataPoint> getListTrainData() {
        return listTrainData;
    }

    public List<DataPoint> getListTestData() {
        return listTestData;
    }

    public DistanceAlgorithm getDistanceAlgorithm() {
        return distanceAlgorithm;
    }

    public void setDistanceAlgorithm(DistanceAlgorithm distanceAlgorithm) {
        this.distanceAlgorithm = distanceAlgorithm;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void splitData(){
        listTestData.clear();
        listTrainData.clear();
        int trainSize = (int)(listDataPoint.size() * splitRatio);
        int testSize = listDataPoint.size() - trainSize;
        Collections.shuffle(listDataPoint);
        for (int i = 0;i < trainSize; i++)
            listTrainData.add(listDataPoint.get(i));
        for (int i = 0; i < testSize; i++){
            DataPoint dataPointTest = new DataPoint(listDataPoint.get(i + trainSize));
            DataPoint dataPointValidator = new DataPoint(dataPointTest);
            dataPointTest.setCategory(Category.TEST);
            listTestData.add(dataPointTest);
            listTestValidator.add(dataPointValidator);
        }
    }

    private List<Double> calculateDistances(DataPoint point){
        List<Double> listDistance = new ArrayList<>();
        for (DataPoint dataPoint:listTrainData){
            double distance = distanceAlgorithm.calculateDistance(point.getVX(), point.getVY(),point.getVZ(),point.getSDX(), point.getSDY(),point.getSDZ(),
                    dataPoint.getVX(), dataPoint.getVY(),dataPoint.getVZ(),dataPoint.getSDX(), dataPoint.getSDY(),dataPoint.getSDZ());
            listDistance.add(distance);
        }
        return listDistance;
    }

    // NOT SURE WHATS HAPPENING
    private Category getMaxCategory(HashMap<Category, Integer> hashMap){
        Iterator<Map.Entry<Category, Integer>> iterator = hashMap.entrySet().iterator();
        int maxCategory = Integer.MIN_VALUE;
        Category category = null;
        while (iterator.hasNext()) {
            Map.Entry<Category, Integer> item = iterator.next();
            if (item.getValue() > maxCategory){
                category = item.getKey();
            }
        }
        return category;
    }


    private Category classifyDataPoint(DataPoint point){
        HashMap<Category, Integer> hashMap = new HashMap<>();
        listDistance = calculateDistances(point);
        for (int i = 0; i < K; i++){
            double min = Double.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < listDistance.size(); j++){
                if (listDistance.get(j) < min){
                    min = listDistance.get(j);
                    minIndex = j;
                }
            }
            Category category = listTrainData.get(minIndex).getCategory();
            if (hashMap.containsKey(category)){
                hashMap.put(category, hashMap.get(category) + 1);
            }else{
                hashMap.put(category, 1);
            }
            listDistance.set(minIndex, Double.MAX_VALUE);
        }
        return getMaxCategory(hashMap);
    }

    public void classify(){
        accuracy = 0;
        for (int i = 0;i < listTestData.size(); i++){
            DataPoint dataPoint = listTestData.get(i);
            Category category = classifyDataPoint(dataPoint);
            if (isCorrect(category, listTestValidator.get(i).getCategory()))
                accuracy++;
            dataPoint.setCategory(category);
        }
        accuracy /= listTestData.size();
    }

    Category predictNew(double vX, double vY, double vZ, double sdX, double sdY, double sdZ){

        DataPoint dataPoint = new DataPoint(vX,vY,vZ,sdX,sdY,sdZ,Category.values()[2]);
        dataPoint.setCategory(Category.TEST);
        Category category = classifyDataPoint(dataPoint);

        return category;
    }

    void addTrainData(){
        listTestData.clear();
        listTrainData.clear();
        int trainSize = (int)(listDataPoint.size() * 1);
        Collections.shuffle(listDataPoint);
        for (int i = 0;i < trainSize; i++){
            listTrainData.add(listDataPoint.get(i));
        }
    }


    private boolean isCorrect(Category predictedCategory, Category trueCategory){
        return predictedCategory.equals(trueCategory);
    }
    public void reset() {
        listDataPoint.clear();
        listTestData.clear();
        listTrainData.clear();
    }
}

