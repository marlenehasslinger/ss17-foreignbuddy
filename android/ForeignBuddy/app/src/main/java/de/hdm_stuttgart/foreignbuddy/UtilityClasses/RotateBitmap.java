package de.hdm_stuttgart.foreignbuddy.UtilityClasses;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Marlene on 25.04.17.
 */

public class RotateBitmap {

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



}
