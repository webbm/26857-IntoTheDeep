package org.firstinspires.ftc.teamcode.samplepiplines

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.vision.BlueDetectionPipeline
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import kotlin.math.abs

@TeleOp
@Disabled
class YellowDetectionTest : LinearOpMode() {
    private lateinit var camera: OpenCvCamera
    private lateinit var pipeline: YellowDetectionPipeline
    private lateinit var servo: Servo

    // Constants
    private val SERVO_MIN_ANGLE = 0.0
    private val SERVO_MAX_ANGLE = 180.0
    private val ANGLE_TOLERANCE = 7.0  // Degrees of acceptable difference

    override fun runOpMode() {
        // Initialize servo
        servo = hardwareMap.get(Servo::class.java, "servo")

        // Initialize camera
        val cameraMonitorViewId = hardwareMap.appContext
            .resources.getIdentifier(
                "cameraMonitorViewId",
                "id",
                hardwareMap.appContext.packageName
            )

        camera = OpenCvCameraFactory.getInstance().createWebcam(
            hardwareMap.get(WebcamName::class.java, "Webcam 1"),
            cameraMonitorViewId
        )

        pipeline = YellowDetectionPipeline()
        camera.setPipeline(pipeline)

        // Start camera stream
        camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {
                telemetry.addData("Camera Error", errorCode)
                telemetry.update()
            }
        })

        servo.scaleRange(0.0, 0.7265)

        // Wait for the driver to press start
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        waitForStart()

        servo.position = 0.53

        while (opModeIsActive()) {
            if (pipeline.isSampleVisible()) {
                // Get the detected angle
                val detectedAngle = normalizeAngle(pipeline.getSampleHeading())

                // Convert angle to servo position (0.0 to 1.0)
                val servoPosition = angleToServoPosition(detectedAngle)

                // Set servo position
                servo.position = servoPosition

                // Display telemetry
                telemetry.addData("Detected Angle", "%.0f", detectedAngle)
                telemetry.addData("Servo Position", "%.0f", servoPosition)
                telemetry.addData("Status", "Tracking")
            } else {
                telemetry.addData("Status", "No yellow objects detected")
            }

            telemetry.update()
        }

        // Clean up
        camera.stopStreaming()
    }

    /**
     * Normalizes the angle to be between 0 and 180 degrees
     */
    private fun normalizeAngle(angle: Double): Double {
        return when {
            angle > 180.0 -> angle - 180.0
            angle < 0.0 -> angle + 180.0
            else -> angle
        }
    }

    /**
     * Converts an angle (in degrees) to a servo position (0.0 to 1.0)
     */
    private fun angleToServoPosition(angle: Double): Double {
        // Ensure angle is within valid range
        val clampedAngle = angle.coerceIn(SERVO_MIN_ANGLE, SERVO_MAX_ANGLE)

        // Convert to servo position (0.0 to 1.0)
        return clampedAngle / SERVO_MAX_ANGLE
    }
}
