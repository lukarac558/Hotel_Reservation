package com.example.db.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.ByteArrayOutputStream;
import java.util.List;

public class Formatter {

    public static String getCorrectDecimalPrecision(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.')
            str = "0" + str;
        int max = str.length();

        StringBuilder rFinal = new StringBuilder();
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && !after) {
                up++;
                if (up > MAX_BEFORE_POINT) return rFinal.toString();
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL)
                    return rFinal.toString();
            }
            rFinal.append(t);
            i++;
        }
        return rFinal.toString();
    }

    public static String getFormattedDate(int year, int month, int day) {
        month++;

        String stringDate = year + "-";
        char zero = '0';

        if (month < 10) {
            stringDate += zero;
        }

        stringDate += month + "-";

        if (day < 10) {
            stringDate += zero;
        }

        stringDate += day;

        return stringDate;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        return baos.toByteArray();
    }

    public static void setRecyclerView(RecyclerView recyclerView, Context context) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static <T> void setAdapter(Spinner spinner, Context context, List<T> list){
        ArrayAdapter<T> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}
