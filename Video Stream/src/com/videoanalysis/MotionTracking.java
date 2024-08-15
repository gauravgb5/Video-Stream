package com.videoanalysis;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

public class MotionTracking {

    private BackgroundSubtractorMOG2 subtractor;

    public MotionTracking() {
        subtractor = Video.createBackgroundSubtractorMOG2();
    }

    public void trackMotion(Mat frame) {
        Mat fgMask = new Mat();
        subtractor.apply(frame, fgMask);

        Imgproc.GaussianBlur(fgMask, fgMask, new Size(15, 15), 0);
        Imgproc.threshold(fgMask, fgMask, 25, 255, Imgproc.THRESH_BINARY);

        Imgproc.cvtColor(fgMask, fgMask, Imgproc.COLOR_GRAY2BGR);  
        Core.addWeighted(frame, 1.0, fgMask, 0.5, 0, frame);  
    }
}