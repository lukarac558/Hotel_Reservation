package com.example.db.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SerializableBitmap implements Serializable{

    private static final long serialVersionUID = -6298516694275121291L;

    transient Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public SerializableBitmap(){}

    public SerializableBitmap(Bitmap b){
        bitmap = b;
    }

    private void writeObject(ObjectOutputStream oos){
        try {
            oos.defaultWriteObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap!=null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if(success){
                try {
                    oos.writeObject(byteStream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readObject(ObjectInputStream ois){
        try {
            ois.defaultReadObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        byte[] image = new byte[0];
        try {
            image = (byte[]) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        if(image != null && image.length > 0){
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }

}
