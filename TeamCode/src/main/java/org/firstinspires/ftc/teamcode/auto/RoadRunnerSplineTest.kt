package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.SparkFunOTOSDrive

@Autonomous
@Disabled
class RoadRunnerSplineTest : LinearOpMode() {

    override fun runOpMode() {
        val initialPose = Pose2d(48.0, 0.0, Math.toRadians(90.0))
        val drive = SparkFunOTOSDrive(hardwareMap, initialPose)
        val route = drive
            .actionBuilder(initialPose)
            .splineTo(Vector2d(x = 0.0, y = 48.0), Math.toRadians(180.0))
//            .waitSeconds(0.5)
            .splineTo(Vector2d(x = -48.0, y = 0.0), Math.toRadians(-90.0))
//            .waitSeconds(0.5)
            .splineTo(Vector2d(x = 0.0, y = -48.0), Math.toRadians(0.0))
//            .waitSeconds(0.5)
            .splineTo(Vector2d(x = 48.0, y = 0.0), Math.toRadians(90.0))
//            .waitSeconds(0.5)


        waitForStart()

        runBlocking(route.build())
    }
}
