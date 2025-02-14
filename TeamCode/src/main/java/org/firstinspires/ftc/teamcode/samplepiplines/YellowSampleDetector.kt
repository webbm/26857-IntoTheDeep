package org.firstinspires.ftc.teamcode.samplepiplines

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

class YellowSampleDetector(hardwareMap: HardwareMap) {
    private lateinit var camera: OpenCvCamera
    private val pipeline = YellowDetectionPipeline()

    init {
        initializeCamera(hardwareMap)
    }

    private fun initializeCamera(hardwareMap: HardwareMap) {
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardwareMap.appContext.packageName
        )

        camera = OpenCvCameraFactory.getInstance().createWebcam(
            hardwareMap.get(WebcamName::class.java, "Webcam 1"),
            cameraMonitorViewId
        )

        camera.setPipeline(pipeline)

        camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {
                // Handle error if needed
            }
        })
    }

    // Public functions to access detection results
    fun isSampleVisible(): Boolean = pipeline.isSampleVisible()
    fun getSampleHeading(): Double = pipeline.getSampleHeading()

    fun stopCamera() {
        camera.stopStreaming()
    }
}
