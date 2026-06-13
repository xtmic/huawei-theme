package com.huawei.superwallpaper.engine.util;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/* JADX INFO: loaded from: classes.dex */
public class P3EglSurfaceFactory implements GLSurfaceView.EGLWindowSurfaceFactory {
    private static final int EGL_GL_COLORSPACE_DISPLAY_P3_PASSTHROUGH_EXT = 13456;
    private static final int EGL_GL_COLORSPACE_KHR = 12445;
    private static final String TAG = P3EglSurfaceFactory.class.getSimpleName();

    @Override // android.opengl.GLSurfaceView.EGLWindowSurfaceFactory
    public EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj) {
        try {
            return egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, new int[]{EGL_GL_COLORSPACE_KHR, EGL_GL_COLORSPACE_DISPLAY_P3_PASSTHROUGH_EXT, 12344});
        } catch (IllegalArgumentException e) {
            LogUtil.e(TAG, "eglCreateWindowSurface", e);
            return null;
        }
    }

    @Override // android.opengl.GLSurfaceView.EGLWindowSurfaceFactory
    public void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
        egl10.eglDestroySurface(eGLDisplay, eGLSurface);
    }
}
