package com.huawei.superwallpaper.engine.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.ETC1Util;
import android.opengl.GLES32;
import android.opengl.GLUtils;
import android.text.TextUtils;
import com.huawei.superwallpaper.engine.AstcBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: classes.dex */
public class Utils {
    private static final int CUBE_MAP_SIZE = 256;
    private static final String TAG = Utils.class.getSimpleName();
    private static int[][] mCubeMapOffset;

    public static int loadShader(int i, String str) {
        int iGlCreateShader = GLES32.glCreateShader(i);
        GLES32.glShaderSource(iGlCreateShader, str);
        GLES32.glCompileShader(iGlCreateShader);
        int[] iArr = new int[1];
        GLES32.glGetShaderiv(iGlCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return iGlCreateShader;
        }
        LogUtil.e(TAG, GLES32.glGetShaderInfoLog(iGlCreateShader));
        GLES32.glDeleteShader(iGlCreateShader);
        return 0;
    }

    public static int loadShader(Context context, int i, int i2) {
        return loadShader(i, FileUtil.readStringFromInputStream(context.getResources().openRawResource(i2)));
    }

    public static int loadShader(Context context, int i, String str) {
        try {
            return loadShader(i, FileUtil.readStringFromInputStream(context.getAssets().open(str)));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "load assets shader fail : ", e);
            return 0;
        }
    }

    public static void checkGlError(String str) {
        int iGlGetError = GLES32.glGetError();
        if (iGlGetError == 0) {
            return;
        }
        LogUtil.e(TAG, str + " checkGlError : " + iGlGetError);
        throw new RuntimeException(str + " : glError " + iGlGetError);
    }

    public static int createProgram(Context context, int i, int i2) {
        int iLoadShader;
        int iLoadShader2 = loadShader(context, 35633, i);
        if (iLoadShader2 == 0 || (iLoadShader = loadShader(context, 35632, i2)) == 0) {
            return 0;
        }
        return buildProgram(iLoadShader2, iLoadShader);
    }

    public static int createProgram(Context context, String str, String str2) {
        int iLoadShader;
        int iLoadShader2 = loadShader(context, 35633, str);
        if (iLoadShader2 == 0 || (iLoadShader = loadShader(context, 35632, str2)) == 0) {
            return 0;
        }
        return buildProgram(iLoadShader2, iLoadShader);
    }

    public static int buildProgram(int i, int i2) {
        int iGlCreateProgram = GLES32.glCreateProgram();
        if (iGlCreateProgram == 0) {
            return iGlCreateProgram;
        }
        GLES32.glAttachShader(iGlCreateProgram, i);
        checkGlError("attach vertex shader");
        GLES32.glAttachShader(iGlCreateProgram, i2);
        checkGlError("attach fragment shader");
        GLES32.glLinkProgram(iGlCreateProgram);
        int[] iArr = new int[1];
        GLES32.glGetProgramiv(iGlCreateProgram, 35714, iArr, 0);
        if (iArr[0] == 1) {
            return iGlCreateProgram;
        }
        LogUtil.e(TAG, "createProgram : Cannot link program");
        LogUtil.e(TAG, "createProgram glError : " + GLES32.glGetProgramInfoLog(iGlCreateProgram));
        GLES32.glDeleteProgram(iGlCreateProgram);
        return 0;
    }

    private static void doDefaultTexParameter() {
        GLES32.glTexParameterf(3553, 10241, 9729.0f);
        GLES32.glTexParameterf(3553, 10240, 9729.0f);
        GLES32.glTexParameterf(3553, 10242, 33069.0f);
        GLES32.glTexParameterf(3553, 10243, 33069.0f);
    }

    public static int texture(Context context, Uri uri, Point point) {
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        if (!TextUtils.isEmpty(uri.getPath())) {
            authority = authority + uri.getPath();
        }
        Bitmap bitmapDecodeStream = null;
        try {
            if ("file".equalsIgnoreCase(scheme)) {
                bitmapDecodeStream = BitmapFactory.decodeFile(authority);
            } else if ("assets".equalsIgnoreCase(scheme)) {
                bitmapDecodeStream = BitmapFactory.decodeStream(context.getAssets().open(authority));
            }
            if (bitmapDecodeStream != null && point != null) {
                point.set(bitmapDecodeStream.getWidth(), bitmapDecodeStream.getHeight());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "texture exception : ", e);
        }
        return texture(bitmapDecodeStream, true);
    }

    public static int texture(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return -1;
        }
        int[] iArr = new int[1];
        GLES32.glGenTextures(1, iArr, 0);
        GLES32.glBindTexture(3553, iArr[0]);
        doDefaultTexParameter();
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        if (z) {
            bitmap.recycle();
        }
        return iArr[0];
    }

    public static int texture(int i, int i2) {
        LogUtil.i(TAG, "texture width : %d, height : %d", Integer.valueOf(i), Integer.valueOf(i2));
        int[] iArr = new int[1];
        GLES32.glGenTextures(1, iArr, 0);
        GLES32.glBindTexture(3553, iArr[0]);
        doDefaultTexParameter();
        GLES32.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
        return iArr[0];
    }

    public static int surfaceTexture() {
        int[] iArr = new int[1];
        GLES32.glGenTextures(1, iArr, 0);
        GLES32.glBindTexture(36197, iArr[0]);
        GLES32.glTexParameterf(36197, 10241, 9729.0f);
        GLES32.glTexParameterf(36197, 10240, 9729.0f);
        GLES32.glTexParameteri(36197, 10242, 33071);
        GLES32.glTexParameteri(36197, 10243, 33071);
        return iArr[0];
    }

    public static int texture(Context context, String[] strArr, boolean z) {
        Bitmap bitmapDecodeFile;
        int[] iArr = {34069, 34070, 34071, 34072, 34073, 34074};
        int[] iArr2 = new int[1];
        GLES32.glGenTextures(1, iArr2, 0);
        GLES32.glBindTexture(34067, iArr2[0]);
        GLES32.glTexParameterf(34067, 10241, 9729.0f);
        GLES32.glTexParameterf(34067, 10240, 9729.0f);
        GLES32.glTexParameterf(34067, 10242, 10497.0f);
        GLES32.glTexParameterf(34067, 10243, 10497.0f);
        for (int i = 0; i < 6; i++) {
            if (z) {
                try {
                    bitmapDecodeFile = BitmapFactory.decodeFile(strArr[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "cube texture error : ", e);
                }
            } else {
                bitmapDecodeFile = BitmapFactory.decodeStream(context.getAssets().open(strArr[i]));
            }
            GLUtils.texImage2D(iArr[i], 0, bitmapDecodeFile, 0);
            bitmapDecodeFile.recycle();
        }
        return iArr2[0];
    }

    public static int cubeTexture(Context context, Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return 0;
        }
        int iCubeTexture = cubeTexture(clipBitmap(context, bitmap));
        if (z) {
            GLES32.glGenerateMipmap(34067);
        }
        return iCubeTexture;
    }

    public static int cubeTexture(Bitmap[] bitmapArr) {
        int[] iArr = {34069, 34070, 34071, 34072, 34073, 34074};
        int[] iArr2 = new int[1];
        GLES32.glGenTextures(1, iArr2, 0);
        GLES32.glBindTexture(34067, iArr2[0]);
        GLES32.glTexParameterf(34067, 10241, 9729.0f);
        GLES32.glTexParameterf(34067, 10240, 9729.0f);
        GLES32.glTexParameterf(34067, 10242, 10497.0f);
        GLES32.glTexParameterf(34067, 10243, 10497.0f);
        for (int i = 0; i < 6; i++) {
            GLUtils.texImage2D(iArr[i], 0, bitmapArr[i], 0);
            bitmapArr[i].recycle();
        }
        return iArr2[0];
    }

    public static int compressedTexture(Context context, Uri uri, String str, int i, Point point) {
        InputStream inputStreamOpen;
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        if (!TextUtils.isEmpty(uri.getPath())) {
            authority = authority + uri.getPath();
        }
        InputStream inputStream = null;
        try {
        } catch (Exception e) {
            LogUtil.e(TAG, "input stream exception : ", e);
        }
        if ("file".equalsIgnoreCase(scheme)) {
            inputStreamOpen = new FileInputStream(new File(authority));
        } else {
            if ("assets".equalsIgnoreCase(scheme)) {
                inputStreamOpen = context.getAssets().open(authority);
            }
            return compressedTexture(inputStream, str, i, point);
        }
        inputStream = inputStreamOpen;
        return compressedTexture(inputStream, str, i, point);
    }

    public static int compressedTexture(Context context, Uri uri, String str, Point point) {
        int i;
        if (str.equals("etc2")) {
            i = 37496;
        } else {
            i = str.equals("astc") ? 37808 : 0;
        }
        return compressedTexture(context, uri, str, i, point);
    }

    public static int compressedTexture(InputStream inputStream, String str, int i, Point point) {
        if (inputStream == null) {
            return 0;
        }
        int[] iArr = new int[1];
        GLES32.glGenTextures(1, iArr, 0);
        int i2 = iArr[0];
        GLES32.glBindTexture(3553, i2);
        doDefaultTexParameter();
        if ("etc1".equalsIgnoreCase(str)) {
            try {
                try {
                    ETC1Util.loadTexture(3553, 0, 0, 6407, i, inputStream);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException unused) {
                    }
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "load etc1 exception : ", e);
            }
        } else if ("etc2".equalsIgnoreCase(str) || "astc".equalsIgnoreCase(str)) {
            byte[] bArr = null;
            try {
                bArr = new byte[inputStream.available()];
                inputStream.read(bArr);
                inputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (bArr == null) {
                return -1;
            }
            ByteBuffer byteBufferOrder = ByteBuffer.allocateDirect(bArr.length).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.put(bArr).position(16);
            ByteBuffer byteBufferOrder2 = ByteBuffer.allocateDirect(16).order(ByteOrder.BIG_ENDIAN);
            byteBufferOrder2.put(bArr, 0, 16).position(0);
            if ("etc2".equalsIgnoreCase(str)) {
                short s = byteBufferOrder2.getShort(12);
                short s2 = byteBufferOrder2.getShort(14);
                if (point != null) {
                    point.set(s, s2);
                }
                GLES32.glCompressedTexImage2D(3553, 0, i, s, s2, 0, bArr.length - 16, byteBufferOrder);
            } else if ("astc".equalsIgnoreCase(str)) {
                int i3 = (byteBufferOrder2.get(7) & 255) + ((byteBufferOrder2.get(8) & 255) << 8) + ((byteBufferOrder2.get(9) & 255) << 16);
                int i4 = (byteBufferOrder2.get(10) & 255) + ((byteBufferOrder2.get(11) & 255) << 8) + ((byteBufferOrder2.get(12) & 255) << 16);
                int i5 = (byteBufferOrder2.get(13) & 255) + ((byteBufferOrder2.get(14) & 255) << 8) + ((byteBufferOrder2.get(15) & 255) << 16);
                int i6 = byteBufferOrder2.get(4) & 255;
                int i7 = byteBufferOrder2.get(5) & 255;
                int i8 = byteBufferOrder2.get(6) & 255;
                int i9 = (((((i3 + i6) - 1) / i6) * (((i4 + i7) - 1) / i7)) * (((i5 + i8) - 1) / i8)) << 4;
                if (point != null) {
                    point.set(i3, i4);
                }
                GLES32.glCompressedTexImage2D(3553, 0, i, i3, i4, 0, i9, byteBufferOrder);
            }
        }
        return i2;
    }

    public static int compressedASTCTexture(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i) {
        int[] iArr = new int[1];
        GLES32.glGenTextures(1, iArr, 0);
        int i2 = iArr[0];
        GLES32.glBindTexture(3553, i2);
        doDefaultTexParameter();
        int i3 = (byteBuffer2.get(7) & 255) + ((byteBuffer2.get(8) & 255) << 8) + ((byteBuffer2.get(9) & 255) << 16);
        int i4 = (byteBuffer2.get(10) & 255) + ((byteBuffer2.get(11) & 255) << 8) + ((byteBuffer2.get(12) & 255) << 16);
        int i5 = (byteBuffer2.get(13) & 255) + ((byteBuffer2.get(14) & 255) << 8) + ((byteBuffer2.get(15) & 255) << 16);
        int i6 = byteBuffer2.get(4) & 255;
        int i7 = byteBuffer2.get(5) & 255;
        int i8 = byteBuffer2.get(6) & 255;
        GLES32.glCompressedTexImage2D(3553, 0, i, i3, i4, 0, (((((i3 + i6) - 1) / i6) * (((i4 + i7) - 1) / i7)) * (((i5 + i8) - 1) / i8)) << 4, byteBuffer);
        return i2;
    }

    public static Bitmap[] clipBitmap(Context context, Bitmap bitmap) {
        LogUtil.i(TAG, "clip bitmap start", new Object[0]);
        if (mCubeMapOffset == null) {
            mCubeMapOffset = new int[][]{new int[]{512, 256}, new int[]{0, 256}, new int[]{256, 0}, new int[]{256, 512}, new int[]{256, 256}, new int[]{768, 256}};
        }
        Bitmap[] bitmapArr = new Bitmap[mCubeMapOffset.length];
        int i = 0;
        while (true) {
            int[][] iArr = mCubeMapOffset;
            if (i < iArr.length) {
                bitmapArr[i] = Bitmap.createBitmap(bitmap, iArr[i][0], iArr[i][1], 256, 256);
                LogUtil.i(TAG, "%d, bitmap width %d, height %d", Integer.valueOf(i), Integer.valueOf(bitmapArr[i].getWidth()), Integer.valueOf(bitmapArr[i].getHeight()));
                i++;
            } else {
                LogUtil.i(TAG, "clip bitmap end", new Object[0]);
                return bitmapArr;
            }
        }
    }

    public static AstcBuffer getCompressedAstcData(Context context, Uri uri) {
        InputStream inputStreamOpen;
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        if (!TextUtils.isEmpty(uri.getPath())) {
            authority = authority + uri.getPath();
        }
        InputStream inputStream = null;
        try {
        } catch (Exception e) {
            LogUtil.e(TAG, "input stream exception : ", e);
        }
        if ("file".equalsIgnoreCase(scheme)) {
            inputStreamOpen = new FileInputStream(new File(authority));
        } else {
            if ("assets".equalsIgnoreCase(scheme)) {
                inputStreamOpen = context.getAssets().open(authority);
            }
            return getCompressedAstcData(inputStream);
        }
        inputStream = inputStreamOpen;
        return getCompressedAstcData(inputStream);
    }

    public static AstcBuffer getCompressedAstcData(InputStream inputStream) {
        byte[] bArr = null;
        if (inputStream == null) {
            return null;
        }
        AstcBuffer astcBuffer = new AstcBuffer();
        try {
            bArr = new byte[inputStream.available()];
            inputStream.read(bArr);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocateDirect(bArr.length).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.put(bArr).position(16);
        ByteBuffer byteBufferOrder2 = ByteBuffer.allocateDirect(16).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder2.put(bArr, 0, 16).position(0);
        astcBuffer.mBuffer = byteBufferOrder;
        astcBuffer.mInternalFormat = 37808;
        int i = (byteBufferOrder2.get(7) & 255) + ((byteBufferOrder2.get(8) & 255) << 8) + ((byteBufferOrder2.get(9) & 255) << 16);
        int i2 = (byteBufferOrder2.get(10) & 255) + ((byteBufferOrder2.get(11) & 255) << 8) + ((byteBufferOrder2.get(12) & 255) << 16);
        int i3 = (byteBufferOrder2.get(13) & 255) + ((byteBufferOrder2.get(14) & 255) << 8) + ((byteBufferOrder2.get(15) & 255) << 16);
        astcBuffer.mImageSize = (((((i + r3) - 1) / (byteBufferOrder2.get(4) & 255)) * (((i2 + r6) - 1) / (byteBufferOrder2.get(5) & 255))) * (((i3 + r2) - 1) / (byteBufferOrder2.get(6) & 255))) << 4;
        astcBuffer.mXSize = i;
        astcBuffer.mYSize = i2;
        return astcBuffer;
    }

    public static int compressedAstcTexture(AstcBuffer astcBuffer) {
        if (astcBuffer == null) {
            return -1;
        }
        int[] iArr = new int[1];
        GLES32.glGenTextures(1, iArr, 0);
        int i = iArr[0];
        GLES32.glBindTexture(3553, i);
        doDefaultTexParameter();
        GLES32.glCompressedTexImage2D(3553, 0, astcBuffer.mInternalFormat, astcBuffer.mXSize, astcBuffer.mYSize, 0, astcBuffer.mImageSize, astcBuffer.mBuffer);
        return i;
    }

    public static FloatBuffer floatArray2FloatBuffer(float[] fArr) {
        FloatBuffer floatBufferAsFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArr).position(0);
        return floatBufferAsFloatBuffer;
    }

    public static ShortBuffer shortArray2ShortBuffer(short[] sArr) {
        ShortBuffer shortBufferAsShortBuffer = ByteBuffer.allocateDirect(sArr.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        shortBufferAsShortBuffer.put(sArr).position(0);
        return shortBufferAsShortBuffer;
    }
}
