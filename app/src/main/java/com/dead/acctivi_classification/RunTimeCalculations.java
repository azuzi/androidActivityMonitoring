package com.dead.acctivi_classification;
import java.util.List;

public class RunTimeCalculations {



    public float findAverage (List<Float> dataXYZ){
        float mean = 0;
        if(!dataXYZ.isEmpty()) {
            for (float xyz : dataXYZ) {
                mean += xyz;
            }
            return mean / dataXYZ.size();
        }
        return mean;
    }


    public float findVariance (List<Float> dataXYZ, float mean){
        float variance = 0;
        for (float xyz : dataXYZ) {
            variance += (float) Math.pow((double)xyz - (double) mean,2);
        }
        return variance / dataXYZ.size();
    }


    public static float findStandardDeviation (float variance){
        return (float) Math.sqrt(variance);
    }
}
