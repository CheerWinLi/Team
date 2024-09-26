package com.team.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author :Lictory
 * @date : 2024/09/26
 */
public class Question {
    static String all="";
    static int cnt=1;
    public static void save(){
        try{
            File file = new File("D:\\IDEA\\GitHubProject\\Team\\Calculator\\src\\main\\resources\\exercises.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(all);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Write(String question){
        all+=(cnt+". "+question+"\n");
        cnt++;
    }
}
