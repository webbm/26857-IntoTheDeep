package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Disabled
@Autonomous
class LimeLightRoadRunnerTest : LinearOpMode() {

    override fun runOpMode() {
        val limelight = hardwareMap.get(Limelight3A::class.java, "limelight")
        limelight.setPollRateHz(100)

        limelight.start()
        limelight.pipelineSwitch(0)

        waitForStart()

        while (opModeIsActive()) {
            val aprilTag = limelight.latestResult.fiducialResults.firstOrNull() ?: continue
            val xMeters = aprilTag.robotPoseFieldSpace.position.x
            val xInches = xMeters * 39.3701
            val yMeters = aprilTag.robotPoseFieldSpace.position.y
            val yInches = yMeters * 39.3701
            val degrees = aprilTag.robotPoseFieldSpace.orientation.yaw

            telemetry.addData("X", xInches)
            telemetry.addData("Y", yInches)
            telemetry.addData("Degrees", degrees)
            telemetry.update()

            idle()
        }
    }
}
