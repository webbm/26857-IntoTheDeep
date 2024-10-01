package org.firstinspires.ftc.teamcode.auto

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.robot.Diffy
import org.firstinspires.ftc.teamcode.robot.LowerSlide

@Autonomous
class MainAuto : LinearOpMode() {

    override fun runOpMode() {
        telemetry.addLine("Point the bot at the parking zone")
        telemetry.update()

        val lowerSlide = LowerSlide(hardwareMap)
        val diffy = Diffy(hardwareMap)

        val frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_1150)
        val frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_1150)
        val backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_1150)
        val backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_1150)
        val driveTrain = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        lowerSlide.setPosition(0.0)
        diffy.setDiffyPosition(Diffy.DiffyPosition.PARKED)
        //run stop ingestion


        waitForStart()

        diffy.stopIngestion()

        lowerSlide.setPosition(0.18)

        sleep(5000)
        // run reverse ingestion

    }
}
