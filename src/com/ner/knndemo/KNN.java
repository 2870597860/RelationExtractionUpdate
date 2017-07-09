package com.ner.knndemo;

import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.NavigableMap;  
import java.util.TreeMap; 

public class KNN {  
	  
    int K;  
  
    public KNN(int k) {  
        this.K = k;  
    }  
  
    private static class Sample {  
        public double lable;  
        public double prelable;  
        List<Double> features;  
  
        Sample() {  
            features = new ArrayList<Double>();  
        }  
    }  
  
    private class Node {  
        Double lable;  
        Integer idx;  
  
        Node(double lable, int idx) {  
            this.lable = lable;  
            this.idx = idx;  
        }  
    }  
  
    private void readTrain(List<Sample> train, double[][] features,  
            double[] lables) {  
  
        int row = lables.length;  
        int col = features[0].length;  
        for (int i = 0; i < row; i++) {  
            Sample sample = new Sample();  
            for (int j = 0; j < col; j++) {  
                sample.lable = lables[i];  
                sample.features.add(features[i][j]);  
            }  
            train.add(sample);  
        }  
    }  
  
    private void readTest(List<Sample> test, double[][] features,  
            double[] lables) {  
  
        int cow = lables.length;  
        int col = features[0].length;  
        for (int i = 0; i < cow; i++) {  
            Sample sample = new Sample();  
            for (int j = 0; j < col; j++) {  
                sample.lable = lables[i];  
                sample.features.add(features[i][j]);  
            }  
            test.add(sample);  
        }  
    }  
  
    private double euclideanDistance(List<Double> v1, List<Double> v2) {  
        double ret = 0.0;  
        int n = v1.size();  
        for (int i = 0; i < n; i++) {  
            ret += (v1.get(i) - v2.get(i)) * (v1.get(i) - v2.get(i));  
        }  
        return Math.sqrt(ret);  
    }  
  
    private void initDistanceMatrix(List<List<Double>> dm, List<Sample> train,  
            List<Sample> test) {  
        for (int i = 0; i < test.size(); i++) {  
            List<Double> vd = new ArrayList<Double>();  
            for (int j = 0; j < train.size(); ++j) {  
                vd.add(euclideanDistance(test.get(i).features,  
                        train.get(j).features));  
            }  
            dm.add(vd);  
        }  
    }  
  
    private void knnProcess(List<Sample> test, List<Sample> train,  
            List<List<Double>> dm, int k) {  
        int idx = 0;  
        for (int i = 0; i < test.size(); i++) {  
            NavigableMap<Double, Node> dts = new TreeMap<Double, Node>(); // 保存与测试样本i距离最近的k个点  
            for (int j = 0; j < dm.get(i).size(); j++) {  
                if (dts.size() < k) // 把前面k个插入dts中  
                {  
                    // 插入时会自动排序，按dts中的double排序，最小的排在最后  
                    dts.put(dm.get(i).get(j), new Node(train.get(j).lable,  
                            idx++));  
                } else {  
                    Map.Entry<Double, Node> node = dts.pollLastEntry();  
                    if (dm.get(i).get(j) < node.getKey()) // 把当前测试样本i到当前训练样本之间的欧氏距离与dts中最小距离比较，若更小就更新dts  
                    {  
                        dts.put(dm.get(i).get(j), new Node(train.get(j).lable,  
                                idx++));  
                    } else  
                        dts.put(node.getKey(), node.getValue());  
                }  
            }  
  
            double prelable = -1;  
            Map<Double, Double> tds = new HashMap<Double, Double>();  
            double weight = 0.0;  
            // 下面for循环主要是求出与测试样本i最邻近的k个样本点中大多数属于的类别，即将其作为测试样本点i的类别  
            for (Map.Entry<Double, Node> it : dts.entrySet()) {  
                // 不考虑权重的情况，在 k 个样例中只要出现就加 1  
                // 这里是考虑距离与权重的关系，距离越大权重越小  
                Double val = tds.get(it.getValue().lable);  
                if (val == null)  
                    val = 0.0;  
                val += 1.0 / it.getKey();  
                tds.put(it.getValue().lable, val);  
                if (val > weight) {  
                    weight = val;  
                    prelable = it.getValue().lable; // 保存一下类别  
                }  
            }  
            test.get(i).prelable = prelable;  
        }  
    }  
  
    public int DoKnn(double trainfeatures[][], double[] trainlables,  
            double testfeatures[][], double[] testlables) {  
        List<Sample> train = new ArrayList<Sample>();  
        List<Sample> test = new ArrayList<Sample>();  
        readTrain(train, trainfeatures, trainlables);  
        readTest(test, testfeatures, testlables);  
        List<List<Double>> dm = new ArrayList<List<Double>>();  
        initDistanceMatrix(dm, train, test);  
        knnProcess(test, train, dm, K);  
        int yes = 0;  
        for (int i = 0; i < test.size(); i++) {  
            if (test.get(i).lable == test.get(i).prelable)  
                yes++;  
        }  
        System.out.println("Accuracy = " + yes * 100.0 / test.size() + "% ("  
                + yes + "/" + test.size() + ") (classification)");  
        return yes;  
    }  
}