package org.firstinspires.ftc.teamcode.auto

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Wrist
import java.util.concurrent.TimeUnit

@Autonomous
@Disabled
class LeftAuto : LinearOpMode() {

    override fun runOpMode() {
        val frontLeft = Motor(hardwareMap, "left_front", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val frontRight = Motor(hardwareMap, "right_front", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val backLeft = Motor(hardwareMap, "left_back", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val backRight = Motor(hardwareMap, "right_back", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val driveTrain = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        waitForStart()

        val claw = Claw(hardwareMap)
        val wrist = Wrist(hardwareMap)

        val timer = ElapsedTime()
        while (timer.time(TimeUnit.MILLISECONDS) < 3500) {
            driveTrain.driveRobotCentric(0.0, -0.3, 0.0)
        }
        driveTrain.stop()
    }
}
