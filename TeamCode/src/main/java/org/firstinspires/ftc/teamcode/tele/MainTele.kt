package org.firstinspires.ftc.teamcode.tele

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Diffy
import org.firstinspires.ftc.teamcode.robot.LowerSlide
import org.firstinspires.ftc.teamcode.robot.VerticalSlide
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.max

@Disabled
@TeleOp
class MainTele : LinearOpMode() {

    override fun runOpMode() {
        val verticalSlide = VerticalSlide(hardwareMap)
        val lowerSlide = LowerSlide(hardwareMap)
        val diffy = Diffy(hardwareMap)

        val dc = GamepadEx(gamepad1)
        val mc = GamepadEx(gamepad2)

        val frontLeft = Motor(hardwareMap, "left_front", Motor.GoBILDA.RPM_1150)
        val frontRight = Motor(hardwareMap, "right_front", Motor.GoBILDA.RPM_1150)
        val backLeft = Motor(hardwareMap, "left_back", Motor.GoBILDA.RPM_1150)
        val backRight = Motor(hardwareMap, "right_back", Motor.GoBILDA.RPM_1150)
        val driveTrain = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        lowerSlide.setPosition(0.0)
//        diffy.setDiffyPosition(Diffy.DiffyPosition.PARKED)

        waitForStart()

        while (opModeIsActive()) {
            dc.readButtons()
            mc.readButtons()
            val rightTrigger = mc.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)
            val leftTrigger = mc.getTrigger(PS5Keys.Trigger.LEFT_TRIGGER.xboxTrigger)
            val driveSpeedMultiplier = max(0.5, (1.0 - dc.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)))

            // drivetrain
            driveTrain.driveRobotCentric(
                -dc.leftX * driveSpeedMultiplier,
                -dc.leftY * driveSpeedMultiplier,
                -dc.rightX * driveSpeedMultiplier
            )

            // vertical slide
            val verticalSlidePower = -mc.rightY
            verticalSlide.setPower(verticalSlidePower)

            val actualPosition = lowerSlide.getPosition()
            telemetry.addData("Vertical Slide Power", verticalSlidePower)
            telemetry.addData("Vertical Slide Position", verticalSlide.getPosition())
            telemetry.addData("Diffy Left Position", diffy.getLeftPosition())
            telemetry.addData("Diffy Right Position", diffy.getRightPosition())
            telemetry.addData("Slide Actual Position", actualPosition)
            telemetry.addData("Right Trigger", rightTrigger)
            telemetry.addData("Left Trigger", leftTrigger)
            telemetry.addData("Drive Speed Multiplier", driveSpeedMultiplier)

            // lower slide
            if (mc.wasJustPressed(PS5Keys.Button.DPAD_UP.xboxButton)) {
                lowerSlide.setPosition(0.3)
            }
            else if (mc.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                lowerSlide.setPosition(0.0)
            }

            // diffy - ingest
            if (rightTrigger > 0.5) {
                diffy.ingestForward()
            }
            else if (leftTrigger > 0.5) {
                diffy.ingestReverse()
            }
            else {
                diffy.stopIngestion()
            }

            // diffy - position
            if (mc.wasJustPressed(PS5Keys.Button.RIGHT_BUMPER.xboxButton)) {
                diffy.setDiffyPosition(Diffy.DiffyPosition.INGEST)
            }
            else if (mc.wasJustPressed(PS5Keys.Button.LEFT_BUMPER.xboxButton)) {
                diffy.setDiffyPosition(Diffy.DiffyPosition.CRUISE)
            }
            else if (mc.wasJustPressed(PS5Keys.Button.TRIANGLE.xboxButton)) {
//                diffy.setDiffyPosition(Diffy.DiffyPosition.PARKED)
            }
            else if (mc.wasJustPressed(PS5Keys.Button.CROSS.xboxButton)) {
                diffy.setDiffyPosition(Diffy.DiffyPosition.SCORE)
            }

            /*
            if (mc.wasJustPressed(PS5Keys.Button.DPAD_UP.xboxButton)) {
                diffy.lift()
            }
            else if (mc.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                diffy.lower()
            }
             */

            telemetry.update()
            idle()
        }
    }
}
