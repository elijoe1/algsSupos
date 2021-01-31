package uk.ac.cam.ej349.supo1;

import java.util.Comparator;

public class BinarySearch {

    public static <T> int search(T[] increasingArray, Comparator<T> comparator, T value) {
        int start = 0;
        int end = increasingArray.length - 1;
        while(start < end && increasingArray[(start+end)/2] != value) {
            if (comparator.compare(increasingArray[(start+end)/2], value) > 0) {
                end =(start+end)/2;
            } else {
                start = (start+end)/2 + 1;
            }
        }
        if (start == end) {
            if (comparator.compare(increasingArray[start], value) != 0) {
                return -1;
            }
        }
        return (start+end)/2;
    }

    public static void main(String[] args) {
        Integer[] array = new Integer[]{1,5,5,46,312};
        System.out.println(BinarySearch.search(array, Integer::compareTo, 1));
    }
}
