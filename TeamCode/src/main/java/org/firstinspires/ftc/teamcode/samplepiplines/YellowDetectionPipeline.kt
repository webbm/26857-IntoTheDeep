package org.firstinspires.ftc.teamcode.vision

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline

class YellowDetectionPipeline : OpenCvPipeline() {
    // Output data
    var angle: Double = 0.0
    var hasDetection: Boolean = false

    // Processing constants
    private val YELLOW_LOWER = Scalar(20.0, 100.0, 100.0)  // Lower bound
    private val YELLOW_UPPER = Scalar(40.0, 255.0, 255.0)  // Upper bound // HSV upper bounds
    private val MIN_AREA = 500  // Minimum pixel area to be considered a valid detection

    // Processing mats
    private val hsvMat = Mat()
    private val maskMat = Mat()
    private val hierarchyMat = Mat()

    override fun processFrame(input: Mat): Mat {
        // Clear previous detection
        hasDetection = false
        angle = 0.0

        // Convert to HSV colorspace
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV)

        // Create mask for blue colors
        Core.inRange(hsvMat, YELLOW_LOWER, YELLOW_UPPER, maskMat)

        // Find contours in the mask
        val contours = ArrayList<MatOfPoint>()
        Imgproc.findContours(
            maskMat,
            contours,
            hierarchyMat,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        // Find the largest blue contour
        var maxArea = 0.0
        var largestContour: MatOfPoint? = null

        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > maxArea && area > MIN_AREA) {
                maxArea = area
                largestContour = contour
            }
        }

        // If we found a valid contour, calculate its angle
        largestContour?.let { contour ->
            // Fit an ellipse to the contour to find its orientation
            if (contour.toArray().size >= 5) {
                val points2f = MatOfPoint2f()
                contour.convertTo(points2f, CvType.CV_32FC2)
                val rotatedRect = Imgproc.fitEllipse(points2f)

                // Update detection data
                angle = rotatedRect.angle
                hasDetection = true

                // Draw the detection on the output image
                Imgproc.ellipse(input, rotatedRect, Scalar(40.0, 255.0, 255.0), 2)

                // Draw angle text
                Imgproc.putText(
                    input,
                    "Angle: %.0f".format(angle),
                    Point(10.0, 30.0),
                    Imgproc.FONT_ITALIC,
                    1.0,
                    Scalar(0.0, 0.0, 0.0),
                    2
                )
            }
        }

        return input
    }

    fun hasValidDetection(): Boolean = hasDetection
}