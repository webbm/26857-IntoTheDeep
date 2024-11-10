package org.firstinspires.ftc.teamcode.samples

import com.acmerobotics.dashboard.message.redux.StopOpMode
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

@Disabled
@Autonomous
class CurrentSense : LinearOpMode() {

    private lateinit var motor: DcMotorEx

    override fun runOpMode() {

        motor = hardwareMap.get(DcMotorEx:: class.java, "motor")

        val currentLimit = 2.0

        waitForStart()

        while (opModeIsActive()) {
            fun getMotorCurrentDraw(): Double {
                // Assuming motor is connected to port 0 on the REV hub
                val current = motor.getCurrent(CurrentUnit.AMPS)
                return current
            }

            if (getMotorCurrentDraw() >= currentLimit) {
                telemetry.addData("Warning", "Motor drawing too much current!")
                telemetry.update()

                StopOpMode()

//                motor.direction = DcMotorSimple.Direction.REVERSE
//                motor.power = 0.5

            }


            telemetry.addData("Motor Current (A)", getMotorCurrentDraw())
            telemetry.update()

        }
    }

}