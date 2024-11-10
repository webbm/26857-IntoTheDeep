package org.firstinspires.ftc.teamcode.auto

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.robot.Diffy
import org.firstinspires.ftc.teamcode.robot.LowerSlide
import java.util.concurrent.TimeUnit

@Autonomous
class MainAuto : LinearOpMode() {

    override fun runOpMode() {
        telemetry.addLine("Point the bot at the parking zone")
        telemetry.update()

        val frontLeft = Motor(hardwareMap, "left_front", Motor.GoBILDA.RPM_1150)
        val frontRight = Motor(hardwareMap, "right_front", Motor.GoBILDA.RPM_1150)
        val backLeft = Motor(hardwareMap, "left_back", Motor.GoBILDA.RPM_1150)
        val backRight = Motor(hardwareMap, "right_back", Motor.GoBILDA.RPM_1150)
        val driveTrain = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        waitForStart()

        val elapsedTime = ElapsedTime()

        while (elapsedTime.time(TimeUnit.MILLISECONDS) < 500) {
            driveTrain.driveRobotCentric(0.5, 0.0, 0.0)
        }

        driveTrain.stop()
    }
}
