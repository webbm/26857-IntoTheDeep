package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.roadrunner.ftc.LazyImu
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class DriveTrainTest : LinearOpMode() {
    private var driveMode = DriveMode.ROBOT_CENTRIC

    enum class DriveMode {
        FIELD_CENTRIC,
        ROBOT_CENTRIC;

        fun toggle(): DriveMode {
            return when (this) {
                FIELD_CENTRIC -> ROBOT_CENTRIC
                ROBOT_CENTRIC -> FIELD_CENTRIC
            }
        }
    }

    private fun toggleDriveMode() {
        this.driveMode = this.driveMode.toggle()
    }

    override fun runOpMode() {
        //init
        val dc = GamepadEx(gamepad1)
        val frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_1150)
        val frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_1150)
        val backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_1150)
        val backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_1150)

        val imu = LazyImu(
            hardwareMap, "imu", RevHubOrientationOnRobot(
                org.firstinspires.ftc.teamcode.MecanumDrive.PARAMS.logoFacingDirection,
                org.firstinspires.ftc.teamcode.MecanumDrive.PARAMS.usbFacingDirection
            )
        ).get()

        val drive = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        waitForStart()

        while (opModeIsActive()) {
            val yaw = imu.robotYawPitchRollAngles.yaw

            if (dc.wasJustPressed(GamepadKeys.Button.Y)) {
                toggleDriveMode()
            }

            telemetry.addData("DriveMode", driveMode)
            telemetry.addData("Yaw", yaw)
            //loop
            when (driveMode) {
                DriveMode.FIELD_CENTRIC -> {
                    drive.driveFieldCentric(-dc.leftX, -dc.leftY, -dc.rightX, yaw)
                }
                DriveMode.ROBOT_CENTRIC -> {
                    drive.driveRobotCentric(-dc.leftX, -dc.leftY, -dc.rightX)
                }
            }

            telemetry.update()
            idle() // give it a lil rest
        }
    }

}
