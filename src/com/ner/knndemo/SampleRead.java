package com.ner.knndemo;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileReader;  
import java.io.IOException;  
import java.util.ArrayList;  
import java.util.List;  
  
public class SampleRead {  
  
    private double[][] fetures;  
    private double[] lables;  
  
    private String inputfile;  
  
    public SampleRead(String inputfile) {  
        this.inputfile = inputfile;  
        svmread();  
    }  
  
    public void svmread() {  
        File file = new File(inputfile);  
        List<String> readers = new ArrayList<String>();  
        BufferedReader reader = null;  
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            while ((tempString = reader.readLine()) != null) {  
                readers.add(tempString);  
            }  
            int rows = readers.size();  
            lables = new double[rows];  
            fetures = new double[rows][];  
            for (int idx = 0; idx < rows; idx++) {  
                String[] dir = readers.get(idx).split(" ");  
                lables[idx] = Double.parseDouble(dir[0]);  
                fetures[idx] = new double[dir.length - 1];  
                for (int i = 1; i < dir.length; i++) {  
                    fetures[idx][i - 1] = Double  
                            .parseDouble(dir[i].split(":")[1]);  
                }  
            }  
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
    }  
  
    public double[][] getFetures() {  
        return fetures;  
    }  
  
    public void setFetures(double[][] fetures) {  
        this.fetures = fetures;  
    }  
  
    public double[] getLables() {  
        return lables;  
    }  
  
    public void setLables(double[] lables) {  
        this.lables = lables;  
    }  
  
}  