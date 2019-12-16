package com.zxj.needle_compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GaFileWriter {
    private FileWriter fileWriter;

    public GaFileWriter(File file) {
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write(String line){
        try {
            fileWriter.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close(){
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
