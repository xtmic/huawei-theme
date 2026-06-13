package com.huawei.superwallpaper.engine.util;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static String readStringFromInputStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                try {
                    try {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                        sb.append("\n");
                    } catch (Throwable th) {
                        try {
                            bufferedReader.close();
                            inputStreamReader.close();
                            inputStream.close();
                        } catch (Exception e) {
                            LogUtil.e(TAG, "input stream close exception : ", e);
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    LogUtil.e(TAG, "input stream close exception : ", e2);
                }
            } catch (IOException e3) {
                LogUtil.e(TAG, "readStringFromInputStream exception : ", e3);
                e3.printStackTrace();
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            }
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        return sb.toString();
    }

    public static String readStringFromAssets(Context context, String str) {
        try {
            return readStringFromInputStream(context.getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveBitmap(Bitmap bitmap, String str) throws Throwable {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                try {
                    fileOutputStream = new FileOutputStream(new File(str));
                } catch (IOException e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e2) {
                e = e2;
                fileOutputStream2 = fileOutputStream;
                e.printStackTrace();
                LogUtil.e(TAG, "saveBitmap exception : ", e);
                if (fileOutputStream2 == null) {
                } else {
                    fileOutputStream2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream2 = fileOutputStream;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    public static void saveInputStream(InputStream inputStream, String str) throws Throwable {
        Throwable th;
        FileOutputStream fileOutputStream;
        IOException e;
        byte[] bArr;
        try {
            try {
                fileOutputStream = new FileOutputStream(new File(str));
                try {
                    try {
                        bArr = new byte[1024];
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        LogUtil.e(TAG, "saveInputStream exception : ", e);
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                            return;
                        }
                        return;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                return;
            }
        } catch (IOException e4) {
            e = e4;
            fileOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
        }
        while (true) {
            int i = inputStream.read(bArr);
            if (i < 0) {
                break;
            } else {
                fileOutputStream.write(bArr, 0, i);
            }
            th = th2;
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
