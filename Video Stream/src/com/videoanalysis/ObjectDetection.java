package com.videoanalysis;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class ObjectDetection {

    private CascadeClassifier faceDetector;
    private ObjectDetection objectDetection;
    
    public ObjectDetection(String cascadeFilePath) {
        faceDetector = new CascadeClassifier(cascadeFilePath);
        objectDetection = new ObjectDetection("src/resources/haarcascades/haarcascade_frontalface_default.xml");

    }

    public void detectAndDraw(Mat frame) {
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(frame, faceDetections);

        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }
    }
}

