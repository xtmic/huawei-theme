package com.huawei.superwallpaper.engine;

import android.content.res.Resources;
import android.opengl.GLES32;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public class ShaderUtil {
    public static int loadShader(int i, String str) {
        int iGlCreateShader = GLES32.glCreateShader(i);
        if (iGlCreateShader == 0) {
            return iGlCreateShader;
        }
        GLES32.glShaderSource(iGlCreateShader, str);
        GLES32.glCompileShader(iGlCreateShader);
        int[] iArr = new int[1];
        GLES32.glGetShaderiv(iGlCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return iGlCreateShader;
        }
        Log.e("ES20_ERROR", "Could not compile shader " + i + ":");
        Log.e("ES20_ERROR", GLES32.glGetShaderInfoLog(iGlCreateShader));
        GLES32.glDeleteShader(iGlCreateShader);
        return 0;
    }

    public static int createProgram(String str, String str2) {
        int iLoadShader;
        int iLoadShader2 = loadShader(35633, str);
        if (iLoadShader2 == 0 || (iLoadShader = loadShader(35632, str2)) == 0) {
            return 0;
        }
        int iGlCreateProgram = GLES32.glCreateProgram();
        if (iGlCreateProgram != 0) {
            GLES32.glAttachShader(iGlCreateProgram, iLoadShader2);
            checkGlError("glAttachShader");
            GLES32.glAttachShader(iGlCreateProgram, iLoadShader);
            checkGlError("glAttachShader");
            GLES32.glLinkProgram(iGlCreateProgram);
            int[] iArr = new int[1];
            GLES32.glGetProgramiv(iGlCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 1) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES32.glGetProgramInfoLog(iGlCreateProgram));
                GLES32.glDeleteProgram(iGlCreateProgram);
                return 0;
            }
        }
        return iGlCreateProgram;
    }

    public static void checkGlError(String str) {
        int iGlGetError = GLES32.glGetError();
        if (iGlGetError == 0) {
            return;
        }
        Log.e("ES20_ERROR", str + ": glError " + iGlGetError);
        throw new RuntimeException(str + ": glError " + iGlGetError);
    }

    public static String loadFromAssetsFile(String str, Resources resources) {
        InputStream inputStreamOpen;
        ByteArrayOutputStream byteArrayOutputStream;
        String str2 = null;
        try {
            inputStreamOpen = resources.getAssets().open(str);
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            e = e;
        }
        while (true) {
            int i = inputStreamOpen.read();
            if (i != -1) {
                byteArrayOutputStream.write(i);
            } else {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                inputStreamOpen.close();
                String str3 = new String(byteArray, StandardCharsets.UTF_8);
                try {
                    return str3.replaceAll("\\r\\n", "\n");
                } catch (Exception e2) {
                    str2 = str3;
                    e = e2;
                }
            }
            e.printStackTrace();
            return str2;
        }
    }
}
