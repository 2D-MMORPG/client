package com.jukusoft.mmo.utils;

/**
* Utility class for mathematics operations
*/
public class MathUtils {

    /**
    * check, if ranges are overlapping
     *
     * @param minA minimum value of range A
     * @param maxA maximum value of range A
     * @param minB minimum value of range B
     * @param maxB maximum value of range B
     *
     * @return true, if ranges are overlaping
    */
    public static boolean overlapping(float minA, float maxA, float minB, float maxB) {
        if (maxA < minA) {
            // swap values
            float a = maxA;
            maxA = minA;
            minA = a;
        }

        if (maxB < minB) {
            // swap values
            float b = minB;
            maxB = minB;
            minB = b;
        }

        // check if values are overlapping
        return minB <= maxA && minA <= maxB;
    }

}
