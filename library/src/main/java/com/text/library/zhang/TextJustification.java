package com.text.library.zhang;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 使用字符串间添加空格实现左右对齐（分散对齐）
 */
public class TextJustification {

    public static void justify(TextView textView) {
        float contentWidth = textView.getWidth() - textView.getPaddingRight() - textView.getPaddingLeft();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        StringBuilder resultText = new StringBuilder();

        String[] paraArray = paraBreak(text, textView);

        for (int i = 0; i < paraArray.length; i++) {
            if (TextUtils.isEmpty(paraArray[i])) {
                resultText.append("\n");
                continue;
            }
            ArrayList<String> lineList = lineBreak(paraArray[i].trim(), paint, contentWidth);
            String paraText = TextUtils.join("\n", lineList);
            resultText.append(paraText);
            if (i != paraArray.length - 1) {
                resultText.append("\n");
            }
        }

        textView.setText(resultText.toString());
    }

    //分开每个段落
    private static String[] paraBreak(String text, TextView textview) {
        return text.split("\\n");
    }

    //分开每一行，使每一行填入最多的单词数
    private static ArrayList<String> lineBreak(String text, Paint paint, float contentWidth) {
        String[] wordArray = text.split("\\s");

        ArrayList<String> lineList = new ArrayList<>();
        StringBuilder myText = new StringBuilder();


        for (String word : wordArray) {
            if (paint.measureText(myText + " " + word) <= contentWidth) {
                if (myText.length() >= 1)
                    myText.append(" ");
                myText.append(word);
            } else {
                int totalSpacesToInsert = (int) ((contentWidth - paint.measureText(myText.toString())) / paint.measureText(" "));
                lineList.add(justifyLine(myText.toString(), totalSpacesToInsert));
                myText.delete(0, myText.length());
                myText.append(word);
            }
        }
        lineList.add(myText.toString());
        return lineList;
    }

    //已填入最多单词数的一行，插入对应的空格数直到该行满
    private static String justifyLine(String text, int totalSpacesToInsert) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        String[] wordArray = text.split("\\s");
        StringBuilder stringBuilder = new StringBuilder();

        int nullCount = 0;
        for (String word : wordArray) {
            if (TextUtils.isEmpty(word)) {
                nullCount++;
            }
        }

        int realLength = wordArray.length - nullCount;
        int spaceTime = 0, reminderNum = totalSpacesToInsert;

        if (0 != realLength - 1) {
            spaceTime = totalSpacesToInsert / (realLength - 1);//每个单词间隔几个空格
            reminderNum = totalSpacesToInsert % (realLength - 1);//余数
        }
        stringBuilder.append(wordArray[0]);

        int nullIndex = 0;
        for (int i = 1; i < wordArray.length; i++) {
            if (TextUtils.isEmpty(wordArray[i])) {
                stringBuilder.append(wordArray[i]);
                nullIndex++;
                continue;
            }
            for (int i1 = 0; i1 < spaceTime; i1++) {
                stringBuilder.append(" ");//先添加spaceTime个空格
            }
            if (reminderNum >= i - nullIndex) {
                stringBuilder.append("  ");//两个空格
                stringBuilder.append(wordArray[i]);
            } else {
                stringBuilder.append(" ");//一个空格
                stringBuilder.append(wordArray[i]);
            }
        }

        return stringBuilder.toString();
    }
}