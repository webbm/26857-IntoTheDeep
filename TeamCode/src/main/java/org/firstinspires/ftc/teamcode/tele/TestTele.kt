package org.firstinspires.ftc.teamcode.tele

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.pid.Pivot
import org.firstinspires.ftc.teamcode.robot.Diffy
import org.firstinspires.ftc.teamcode.robot.LowerSlide
import org.firstinspires.ftc.teamcode.robot.VerticalSlide
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.max

@TeleOp(name = "Main TeleOp")
class TestTele : LinearOpMode() {

    private lateinit var slide: DcMotor
    private lateinit var diffyIngest1: CRServo
    private lateinit var diffyIngest2: CRServo

//    private lateinit var diffyPivot1: Servo
//    private lateinit var diffyPivot2: Servo

    override fun runOpMode() {
//        val diffy = Diffy(hardwareMap)
        val pivot = Pivot(telemetry, hardwareMap)

        var pivotTarget = 0.0

        val dc = GamepadEx(gamepad1)
        val mc = GamepadEx(gamepad2)

        slide = hardwareMap.get(DcMotor:: class.java, "slide").apply {
            direction = DcMotorSimple.Direction.REVERSE
        }
        diffyIngest1 = hardwareMap.get(CRServo:: class.java, "diffyingest1")
        diffyIngest2 = hardwareMap.get(CRServo:: class.java, "diffyingest2").apply {
            direction = DcMotorSimple.Direction.REVERSE
        }
//        diffyPivot1 = hardwareMap.get(Servo:: class.java, "diffypivot1")
//        diffyPivot2 = hardwareMap.get(Servo:: class.java, "diffypivot2").apply {
//            direction = Servo.Direction.REVERSE
//        }

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

        telemetry.addData("pivot 1 pos", pivot.getPosition())
        telemetry.addData("slide pos", slide.currentPosition)
        telemetry.update()

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

            // pivot
            if (mc.wasJustPressed(GamepadKeys.Button.DPAD_UP)){
                pivotTarget = 100.0
            }else if (mc.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                pivotTarget = 10.0
            }else if (mc.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                pivotTarget = 60.0
            }

            pivot.setTarget(pivotTarget)

            // slide
            slide.power = -mc.rightY

            // diffy - ingest
            while (gamepad2.right_bumper) {
                diffyIngest1.power = 1.0
            }
            diffyIngest1.power = -1.0


            // diffy - position
//            if (mc.wasJustPressed(PS5Keys.Button.RIGHT_BUMPER.xboxButton)) {
//                diffyPivot2.position = 0.65
//                diffyPivot1.position = 0.65
//            }
//            else if (mc.wasJustPressed(PS5Keys.Button.LEFT_BUMPER.xboxButton)) {
//                diffyPivot2.position = 0.0
//                diffyPivot2.position = 0.0
//            }
//            else if (mc.wasJustPressed(PS5Keys.Button.CROSS.xboxButton)) {
//                diffyPivot2.position = 0.9
//                diffyPivot1.position = 0.9
//            }

            telemetry.addData("slide pos", slide.currentPosition)
            telemetry.addData("right trigger", gamepad2.right_trigger)
            telemetry.addData("left trigger", gamepad2.left_trigger)

            telemetry.update()
            idle()
        }
    }
}
