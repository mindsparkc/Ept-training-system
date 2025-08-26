package cn.jkingtools.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
    public static <E> List<E> transferArrayToList(E e){
        List<E> transferedList = new ArrayList<>();
        transferedList.add(e);
        return transferedList;
    }

    public static <E>  List<E> transferArrayToList(E[] array){
        List<E> transferedList = new ArrayList<>();
        Arrays.stream(array).forEach(arr -> transferedList.add(arr));
        return transferedList;
    }
}
