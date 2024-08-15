package com.videoanalysis;

import org.opencv.core.Core;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCaptureManager captureManager = new VideoCaptureManager();
        captureManager.startCapture();
        
    }
}