package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.SparkFunOTOSDrive

@Autonomous
@Disabled
@Config
class RoadRunnerStraightTest : LinearOpMode() {

    override fun runOpMode() {
        val initialPose = Pose2d(48.0, -24.0, Math.toRadians(90.0))
        val drive = SparkFunOTOSDrive(hardwareMap, initialPose)
        val route = drive
            .actionBuilder(initialPose)
            .lineToY(48.0)
            .waitSeconds(5.0)

        waitForStart()

        runBlocking(route.build())
    }
}
