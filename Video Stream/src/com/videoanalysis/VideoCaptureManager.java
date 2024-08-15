package com.videoanalysis;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideoCaptureManager {
    private VideoCapture capture;
    private CascadeClassifier faceCascade;

    public VideoCaptureManager() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Failed to open camera.");
        } else {
            System.out.println("Camera opened successfully.");
        }

        URL resource = getClass().getClassLoader().getResource("haarcascades/haarcascade_frontalface_default.xml");
        if (resource != null) {
            String cascadePath = resource.getPath();
            faceCascade = new CascadeClassifier(cascadePath);
        } 
    }

    public void startCapture() {
        if (!capture.isOpened()) {
            System.out.println("Camera not found or cannot be opened!");
            return;
        }

        Mat frame = new Mat();
        Mat grayFrame = new Mat();
        Mat previousFrame = new Mat();
        Mat diffFrame = new Mat();
        Mat dilatedFrame = new Mat();

        System.out.println("Entering capture loop...");

        while (true) {
            if (!capture.read(frame)) {
                System.out.println("Failed to capture frame.");
                break;
            }

            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(grayFrame, grayFrame, new Size(21, 21), 0);

            if (!previousFrame.empty()) {
                Core.absdiff(previousFrame, grayFrame, diffFrame);
                Imgproc.threshold(diffFrame, diffFrame, 25, 255, Imgproc.THRESH_BINARY);
                Imgproc.dilate(diffFrame, dilatedFrame, new Mat(), new org.opencv.core.Point(-1, -1), 2);

                List<MatOfPoint> contours = new ArrayList<>();
                Imgproc.findContours(dilatedFrame, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

                for (MatOfPoint contour : contours) {
                    Rect boundingRect = Imgproc.boundingRect(contour);
                    if (Imgproc.contourArea(contour) > 500) { 
                        Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 0), 2);
                    }
                }
            }

            grayFrame.copyTo(previousFrame);

            HighGui.imshow("Video Stream with Motion Tracking", frame);

            int key = HighGui.waitKey(30);

            if (key == 81) { 
                System.out.println("Exiting capture loop.");
                break;
            } else if (key == 67) { 
                System.out.println("Key 'c' pressed. Capturing and saving image...");

                String filename = "images/captured_image_" + System.currentTimeMillis() + ".jpg";
                if (!frame.empty()) {
                    boolean result = Imgcodecs.imwrite(filename, frame);
                    if (result) {
                        System.out.println("Image successfully saved as " + filename);
                    } else {
                        System.out.println("Failed to save the image at " + filename);
                    }
                } else {
                    System.out.println("Captured frame is empty, cannot save the image.");
                }
            }
        }

        capture.release();
        HighGui.destroyAllWindows();
    }
}

