package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.HuskyLensColorAlignment

@Disabled
@Autonomous
class HuskyTest : LinearOpMode() {
    override fun runOpMode() {
        val huskyLensColorAlignment = HuskyLensColorAlignment(hardwareMap, telemetry)

        waitForStart()

        huskyLensColorAlignment.alignWithColorTarget()

        sleep(5000)
    }
}
