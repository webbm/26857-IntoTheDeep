package org.firstinspires.ftc.teamcode.tele

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Wrist
import org.firstinspires.ftc.teamcode.util.PS5Keys

@TeleOp
class TuneWrist : LinearOpMode() {

    override fun runOpMode() {

        val wrist = Wrist(hardwareMap)

        val gamepad = GamepadEx(gamepad1)

        waitForStart()

        while (opModeIsActive()) {
            gamepad.readButtons()

            if (gamepad.wasJustPressed(PS5Keys.Button.DPAD_LEFT.xboxButton)) {
                wrist.setPosition(Wrist.Position.INTAKE)
            }
            else if (gamepad.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                wrist.setPosition(Wrist.Position.LINE_UP)
            }
            else if (gamepad.wasJustPressed(PS5Keys.Button.DPAD_RIGHT.xboxButton)) {
                wrist.setPosition(Wrist.Position.MID)
            }
            else if (gamepad.wasJustPressed(PS5Keys.Button.DPAD_UP.xboxButton)) {
                wrist.setPosition(Wrist.Position.OUT_TAKE)
            }
            else if (gamepad.wasJustPressed(PS5Keys.Button.CROSS.xboxButton)) {
                wrist.setPosition(Wrist.Position.PUSH)
            }
        }
    }
}
