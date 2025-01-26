package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.SparkFunOTOSDrive
import org.firstinspires.ftc.teamcode.actions.PivotDownAction
import org.firstinspires.ftc.teamcode.actions.PivotUpAction
import org.firstinspires.ftc.teamcode.robot.Pivot

@Autonomous
@Disabled
class RrActionTest : LinearOpMode() {

    override fun runOpMode() {
//        val pivot = Pivot(hardwareMap)
//        val pivotUpAction = PivotUpAction(pivot, -3900)
//        val pivotDownAction = PivotDownAction(pivot)

        val initialPose = Pose2d(24.0, 48.0, Math.toRadians(270.0))

        val drive = SparkFunOTOSDrive(hardwareMap, initialPose)

        waitForStart()

        val turnToZero = drive
            .actionBuilder(initialPose)
            .turnTo(Math.toRadians(0.0))
            .build()

        val turnToNinety = drive
            .actionBuilder(Pose2d(24.0, 48.0, Math.toRadians(270.0)))
            .turnTo(Math.toRadians(90.0))
            .build()

        val whatever = drive
            .actionBuilder(initialPose)
            .stopAndAdd(
                SequentialAction(
//                    ParallelAction(turnToZero),
                    ParallelAction(turnToNinety),
                    SleepAction(5.000),
                )
            )


        runBlocking(whatever.build())
    }

}
