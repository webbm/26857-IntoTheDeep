package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.actions.DelayAction
import org.firstinspires.ftc.teamcode.actions.VerticalSlideAction
import org.firstinspires.ftc.teamcode.robot.VerticalSlide

@Autonomous
class SlideRunToPositionTest : LinearOpMode() {

    override fun runOpMode() {
        val slide = VerticalSlide(hardwareMap)
        val slideExtendAction = VerticalSlideAction(slide, -1500)
        val slideRetractAction = VerticalSlideAction(slide, 0)

        waitForStart()

        runBlocking(
            SequentialAction(
                slideExtendAction,
                DelayAction(2000),
                slideRetractAction,
                DelayAction(2000),
            )
        )
    }

}
