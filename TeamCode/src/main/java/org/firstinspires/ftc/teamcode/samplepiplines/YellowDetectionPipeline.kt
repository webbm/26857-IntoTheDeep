package org.firstinspires.ftc.teamcode.samplepiplines

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline

class YellowDetectionPipeline : OpenCvPipeline() {
    companion object {
        private val YELLOW_LOWER = Scalar(100.0, 100.0, 100.0)
        private val YELLOW_UPPER = Scalar(200.0, 255.0, 255.0)
        private const val FRAME_CENTER_X = 320.0 // Assuming 640x480 resolution
        private const val HORIZONTAL_FOV = 60.0  // Camera's horizontal field of view in degrees
    }

    private val hsvMat = Mat()
    private val maskMat = Mat()
    private val hierarchyMat = Mat()

    private var isSampleVisible = false
    private var sampleHeading = 0.0

    override fun processFrame(input: Mat): Mat {
        isSampleVisible = false
        sampleHeading = 0.0

        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_BGR2HSV)
        Core.inRange(hsvMat, YELLOW_LOWER, YELLOW_UPPER, maskMat)

        val contours = ArrayList<MatOfPoint>()
        Imgproc.findContours(
            maskMat,
            contours,
            hierarchyMat,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        if (contours.isNotEmpty()) {
            val largestContour = contours.maxByOrNull { Imgproc.contourArea(it) }
            if (largestContour != null && Imgproc.contourArea(largestContour) > 100) {
                isSampleVisible = true
                val boundingRect = Imgproc.boundingRect(largestContour)
                val sampleCenterX = boundingRect.x + (boundingRect.width / 2.0)

                // Calculate heading angle relative to robot
                val pixelOffset = sampleCenterX - FRAME_CENTER_X
                sampleHeading = (pixelOffset / FRAME_CENTER_X) * (HORIZONTAL_FOV / 2.0)

                // Draw visual feedback
                Imgproc.rectangle(input, boundingRect, Scalar(0.0, 255.0, 0.0), 2)
                // Draw center crosshair
                Imgproc.line(
                    input,
                    Point(FRAME_CENTER_X, 0.0),
                    Point(FRAME_CENTER_X, input.rows().toDouble()),
                    Scalar(255.0, 0.0, 0.0),
                    1
                )
                // Draw heading information
                Imgproc.putText(
                    input,
                    "Heading: %.1f°".format(sampleHeading),
                    Point(10.0, 30.0),
                    Imgproc.FONT_HERSHEY_SIMPLEX,
                    1.0,
                    Scalar(0.0, 255.0, 0.0),
                    2
                )
            }
        }

        return input
    }

    fun isSampleVisible(): Boolean = isSampleVisible
    fun getSampleHeading(): Double = sampleHeading
}
