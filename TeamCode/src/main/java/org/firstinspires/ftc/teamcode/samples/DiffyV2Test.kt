package org.firstinspires.ftc.teamcode.samples

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.robot.Diffy
import org.firstinspires.ftc.teamcode.util.PS5Keys


@Config
@TeleOp
@Disabled
class DiffyV2Test: LinearOpMode() {

    // 1 is right and 2 is left
//    private lateinit var diffyIngest1: CRServo
//    private lateinit var diffyIngest2: CRServo

    // 1 is right and 2 is left
    private lateinit var servo: Servo
//    private lateinit var diffyPivot2: Servo

    override fun runOpMode() {

        val dc = GamepadEx(gamepad1)
        val mc = GamepadEx(gamepad2)

        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

//        diffyIngest1 = hardwareMap.get(CRServo:: class.java, "diffyingest1")
//        diffyIngest2 = hardwareMap.get(CRServo:: class.java, "diffyingest2").apply {
//            direction = DcMotorSimple.Direction.REVERSE
//        }

        servo = hardwareMap.get(Servo:: class.java, "servo")
//        diffyPivot2 = hardwareMap.get(Servo:: class.java, "diffypivot2").apply {
//            direction = Servo.Direction.REVERSE
//        }

        servo.scaleRange(0.0, 0.7265)

        waitForStart()

        servo.position = 0.0

        while (opModeIsActive()) {

//            dc.readButtons()
//            mc.readButtons()
//            val rightTrigger = mc.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)
//            val leftTrigger = mc.getTrigger(PS5Keys.Trigger.LEFT_TRIGGER.xboxTrigger)

//            if (rightTrigger > 0.5) {
//                diffyIngest1.power = 1.0
//                diffyIngest2.power = 1.0
//            }else if (leftTrigger > 0.5){
//                diffyIngest1.power = -1.0
//                diffyIngest2.power = -1.0
//            }
////            else {
////                diffyIngest1.power = 0.05
////                diffyIngest2.power = 0.05
////            }

//            if(gamepad2.a){
//                diffyIngest1.power = 1.0
//            }else if(gamepad2.b){
//                diffyIngest2.power = 1.0
//            }else if(gamepad2.x){
//                servo.position = 1.0
//            }else if(gamepad2.y){
//                diffyPivot2.position = 1.0
//            }
//
//            if(gamepad2.dpad_down){
//                diffyIngest1.power = -1.0
//            }else if(gamepad2.dpad_right){
//                diffyIngest2.power = -1.0
//            }else if(gamepad2.dpad_left){
//                servo.position = 0.0
//            }else if(gamepad2.dpad_up){
//                diffyPivot2.position = 0.0
//            }

            servo.position = -mc.rightY

            telemetry.addData("servo pos", servo.position)
            telemetry.update()
        }
    }


}
