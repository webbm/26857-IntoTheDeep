package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
@Disabled
class PivotTest : LinearOpMode() {

    override fun runOpMode() {
        val pivotBottom = hardwareMap.get(DcMotor::class.java, "pivot_bottom").apply {
//            mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//            mode = DcMotor.RunMode.RUN_USING_ENCODER
            direction = DcMotorSimple.Direction.REVERSE
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        waitForStart()

        while (opModeIsActive()) {
            val stickPower = -gamepad1.right_stick_y.toDouble() * 0.7
            pivotBottom.power = stickPower

            telemetry.addData("stick power", stickPower)
            telemetry.addData("pivot position", pivotBottom.currentPosition)
            telemetry.update()

            idle()
        }
    }
}
