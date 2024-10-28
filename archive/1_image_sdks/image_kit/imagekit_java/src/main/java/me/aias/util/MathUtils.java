package me.aias.util;

import org.bytedeco.javacpp.indexer.DoubleRawIndexer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * 一些数学计算工具类
 */
public class MathUtils {

    /**
     * 把opencv的灰度图Mat转化为List<List<Double>> , 即数组对象
     * List<Double>是每一列的灰度值
     * @param gray
     * @return
     */
    public static List<List<Double>> MatPixelToList(Mat gray){
        if(gray == null){
            throw new RuntimeException("不能传入空对象");
        }
        return MatPixelToList(gray , true);
    }

    /**
     * 把opencv的灰度图Mat转化为List<List<Double>> , 即数组对象
     * @param gray
     * @param b true 表示List<Double>是每一列的灰度值，否则List<Double>是每一行的灰度值
     * @return
     */
    public static List<List<Double>> MatPixelToList(Mat gray , boolean b){
        if(gray == null){
            throw new RuntimeException("不能传入空对象");
        }
        List<List<Double>> result = new ArrayList<>();
        int x , y;
        int i , j;
        double value;
        if(b){
            //List<Double>是每一列的灰度值
            x = gray.cols();
            y = gray.rows();
        }else{
            //List<Double>是每一行的灰度值
            x = gray.rows();
            y = gray.cols();
        }

        UByteRawIndexer ldIdx = gray.createIndexer();
        for(i = 0 ; i < x ; i++){
            List<Double> oneLine = new ArrayList<>();
            for(j = 0 ; j < y ; j++){
                if(b){
                    value = ldIdx.get(j, i);
                }else{
                    value = ldIdx.get(i , j);
                }

                oneLine.add(value);
            }
            result.add(oneLine);
        }
        ldIdx.release();
        return result;
    }

    /**
     * 返回Mat每一列的平均值
     * @param grayMat
     * @return
     */
    public static List<Double> avgColMat(Mat grayMat){
        List<List<Double>> data = MatPixelToList(grayMat);

        List<Double> result = new ArrayList<>();

        for(List<Double> col : data) {
            double sum = getSumInList(col);
            result.add(sum / col.size());
        }
        return result;
    }


    /**
     * 返回Mat每一行的平均值
     * @param grayMat
     * @return
     */
    public static List<Double> avgRowMat(Mat grayMat){
        List<List<Double>> data = MatPixelToList(grayMat , false);

        List<Double> result = new ArrayList<>();

        for(List<Double> row : data) {
            double sum = getSumInList(row);
            result.add(sum / row.size());
        }
        return result;
    }



    /**
     * 返回list集合中最大的那个数
     * @param list
     * @return
     */
    public static double getMaxInList(List<Double> list){
        if(list == null){
            throw new RuntimeException("不能传入空对象");
        }
        double max = list.get(0);
        for(double d : list){
            if(d > max){
                max = d;
            }
        }
        return max;
    }

    /**
     * 返回list集合中最大的那个数
     * @param list
     * @return
     */
    public static double getMinInList(List<Double> list){
        if(list == null){
            throw new RuntimeException("不能传入空对象");
        }
        double min = list.get(0);
        for(double d : list){
            if(d < min){
                min = d;
            }
        }
        return min;
    }

    /**
     * 返回list集合中总和
     * @param list
     * @return
     */
    public static double getSumInList(List<Double> list){
        if(list == null){
            throw new RuntimeException("不能传入空对象");
        }
        double sum = 0;
        for(double d : list){
            sum += d;
        }
        return sum;
    }

    /**
     * 寻找第k大的元素，快速排序实现
     * @param nums
     * @param k
     * @return
     */
    public static double findKthLargest(List<Double> nums , int k){
        int len = nums.size();
        Double[] doubles1 = new Double[len];
        for(int i = 0 ; i < len ; i++){
            doubles1[i] = (Double)nums.get(i);
        }

        return findKthLargest(doubles1 , k);
    }

    /**
     * 寻找第k大的元素，快速排序实现
     * @param nums
     * @param k
     * @return
     */
    public static double findKthLargest(Double[] nums, int k) {
        if (k < 1 || nums == null || k > nums.length) {
            return -1;
        }
        return getKth(nums.length - k + 1, nums, 0, nums.length - 1);
    }

    public static double getKth(int k, Double[] nums, int start, int end) {
        double pivot = nums[end];
        int left = start;
        int right = end;
        while (true) {
            while (nums[left] < pivot && left < right) {
                left++;
            }
            while (nums[right] >= pivot && right > left) {
                right--;
            }
            if (left == right) {
                break;
            }
            swap(left, right , nums);
        }
        swap(left, end , nums);
        if (k == left + 1) { return pivot; }
        else if (k < left + 1) { return getKth(k, nums, start, left - 1); }
        else { return getKth(k, nums, left + 1, end); }
    }


    /**
     * 交换数组中l1和l2的数据
     * @param l1
     * @param l2
     * @param arr
     * @return
     */
    private static void swap(int l1 , int l2 , Double[] arr){
        double temp = arr[l1];
        arr[l1] = arr[l2];
        arr[l2] = temp;
    }


    /**
     * 计算指定范围的List的和
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static int sumList(List<Integer> data, int start , int end){
        if(start < 0 || start >= data.size() || end < start || end > data.size()){
            throw new RuntimeException("参数不合法");
        }
        int sum = 0;

        for(int i = start ; i < end ; i++){
            sum += data.get(i);
        }

        return sum;
    }

    /**
     * 计算指定范围List的最大值
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static int maxList(List<Integer> data, int start , int end){
        if(start < 0 || start >= data.size() || end < start || end > data.size()){
            throw new RuntimeException("参数不合法");
        }
        int max = data.get(start);

        for(int i = start + 1; i < end ; i++){
            int value = data.get(i);
            if(max < value){
                max = value;
            }
        }

        return max;
    }

    /**
     * 计算指定范围List的最小值
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static int minList(List<Integer> data, int start , int end){
        if(start < 0 || start >= data.size() || end < start || end > data.size()){
            throw new RuntimeException("参数不合法");
        }
        int min = data.get(start);

        for(int i = start + 1; i < end ; i++){
            int value = data.get(i);
            if(min > value){
                min = value;
            }
        }

        return min;
    }

    //。。。。。。。。。。。。。。。。。。。。。。。。TODO

    /**
     * 返回切割片段中最小的片段的起始下标
     * @param cutPoint 图像上的切割点
     * @param width 图像宽度
     * @return
     */
    public static int minChoicedPart(List<Integer> cutPoint , int width){

        int preValue = cutPoint.get(0);
        int curValue = -1;
        int minPart = Integer.MAX_VALUE;
        int preIndex = -1;
        int partWidth = -1;
        for(int i = 1 ; i <= cutPoint.size() ; i++){
            if(i == cutPoint.size()){
                curValue = width;
            }else{
                curValue = cutPoint.get(i);
            }
            partWidth = curValue - preValue;
            preValue = curValue;
            if(partWidth < minPart){
                minPart = partWidth;
                preIndex = i - 1;
            }
        }
        return preIndex;
    }

    /**
     * 返回切割片段中最大的片段的起始下标
     * @param cutPoint  图像上的切割点
     * @param width 图像的宽度
     * @return
     */
    public static int maxChoicedPart(List<Integer> cutPoint , int width){
        int preValue = cutPoint.get(0);
        int curValue = -1;
        int maxPart = Integer.MIN_VALUE;
        int preIndex = -1;
        int partWidth = -1;
        for(int i = 1 ; i <= cutPoint.size() ; i++){
            if(i == cutPoint.size()){
                curValue = width;
            }else{
                curValue = cutPoint.get(i);
            }
            partWidth = curValue - preValue;
            preValue = curValue;
            if(partWidth > maxPart){
                maxPart = partWidth;
                preIndex = i - 1;
            }
        }
        return preIndex;
    }


    /**
     * 计算指定范围的List中大于avg的值的个数
     * @param data
     * @param start
     * @param end
     * @param avg
     * @return
     */
    public static int countLargeThanAvgInList(List<Integer> data, int start , int end , int avg){
        if(start < 0 || start >= data.size() || end < start || end > data.size()){
            throw new RuntimeException("参数不合法");
        }
        int count = 0;
        for(int i = start ; i < end ; i++){
            if(data.get(i) > avg){
                count++;
            }
        }
        return count;
    }



}