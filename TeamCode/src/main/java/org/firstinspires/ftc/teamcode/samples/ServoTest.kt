package org.firstinspires.ftc.teamcode.samples

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

@Disabled
@TeleOp
class ServoTest : LinearOpMode() {

    private lateinit var slide: DcMotor
    private lateinit var pivot1: DcMotorEx
    private lateinit var pivot2: DcMotorEx


    override fun runOpMode() {

        val dc = GamepadEx(gamepad1)
        val mc = GamepadEx(gamepad2)

        slide = hardwareMap.get(DcMotor:: class.java, "slide").apply {
            direction = DcMotorSimple.Direction.REVERSE
        }
        pivot1 = hardwareMap.get(DcMotorEx:: class.java, "pivot1")
        pivot2 = hardwareMap.get(DcMotorEx:: class.java, "pivot2")

        waitForStart()

        while (opModeIsActive()) {
            fun getMotorCurrent1Draw(): Double {
                // Assuming motor is connected to port 0 on the REV hub
                val current1 = pivot1.getCurrent(CurrentUnit.AMPS)
                return current1
            }
            fun getMotorCurrent2Draw(): Double {
                // Assuming motor is connected to port 0 on the REV hub
                val current2 = pivot2.getCurrent(CurrentUnit.AMPS)
                return current2
            }

            mc.readButtons()
            dc.readButtons()

            pivot1.power = mc.rightY
            pivot2.power = -mc.rightY

            telemetry.addData("Pivot 1 power", pivot1.power)
            telemetry.addData("Pivot 2 power", pivot2.power)

            telemetry.addData("Pivot 1 pos", pivot1.currentPosition)
            telemetry.addData("Pivot 2 pos", pivot2.currentPosition)

            telemetry.addData("Motor Current pivot 1", getMotorCurrent1Draw())
            telemetry.addData("Motor Current pivot 2", getMotorCurrent2Draw())
            telemetry.update()

        }

        telemetry.update()

    }
}