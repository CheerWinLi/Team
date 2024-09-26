package com.team.util;

import java.io.*;

/**
 * @author :Lictory
 * @date : 2024/09/26
 */
public class CheckUtil {

    /**
     * 判断题目对错，并输出结果
     */
    public static void getCheckResult() {
        //用于记录题号
        int count = 0;
        //用于计算正确的题目数
        int countCorrect = 0;
        //用于计算错误的题目数
        int countWrong = 0;
        File textFile = new File("D:\\IDEA\\GitHubProject\\Team\\Calculator\\src\\main\\resources\\exercises.txt");
        File answerFile = new File("D:\\IDEA\\GitHubProject\\Team\\Calculator\\src\\main\\resources\\answer.txt");
        File gradeFile = new File("D:\\IDEA\\GitHubProject\\Team\\Calculator\\src\\main\\resources\\grade.txt");

        StringBuilder correctBuilder = new StringBuilder();
        StringBuilder wrongBuilder = new StringBuilder();
        correctBuilder.append("(");
        wrongBuilder.append("(");
        try {
            //文件读取准备
            FileReader textFileReader = null;
            FileReader answerFileReader = null;
            textFileReader = new FileReader(textFile);
            BufferedReader textBufferedReader = new BufferedReader(textFileReader);
            answerFileReader = new FileReader(answerFile);
            BufferedReader answerBufferedReader = new BufferedReader(answerFileReader);
            String textLine = null;
            String answerLine = null;
            while ((textLine = textBufferedReader.readLine()) != null && (answerLine = answerBufferedReader.readLine()) != null) {
                if (textLine.equals(answerLine)) {
                    countCorrect++;
                    count++;//记录题号
                    correctBuilder.append(count + ",");
                } else {
                    countWrong++;
                    count++;
                    wrongBuilder.append(count + ",");
                }
            }
            //对输出字符串做一些修饰处理
            correctBuilder.deleteCharAt(correctBuilder.length() - 1);
            wrongBuilder.deleteCharAt(wrongBuilder.length() - 1);
            correctBuilder.append(")");
            wrongBuilder.append(")");
            //得到批改情况
            String correct = "Correct:" + countCorrect + correctBuilder.toString();
            String wrong = "Wrong:" + countWrong + wrongBuilder.toString();

            //现在将成绩写入Grade文件
            //创建一个字符写入流对象
            FileWriter fileWriter = new FileWriter(gradeFile);
            //使用缓冲技术
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(correct + "\n" + wrong);
            //需要将关闭BufferedWriter对象，写入才能生效
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
