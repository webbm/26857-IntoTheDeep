package org.firstinspires.ftc.teamcode.tele

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.util.PS5Keys

@TeleOp
@Disabled
class TuneClaw : LinearOpMode() {

    override fun runOpMode() {

        val claw = Claw(hardwareMap)

        val gamepad = GamepadEx(gamepad1)

        waitForStart()

        while (opModeIsActive()) {
            gamepad.readButtons()

            if (gamepad.wasJustPressed(PS5Keys.Button.DPAD_LEFT.xboxButton)) {
                claw.setPosition(Claw.Position.CLOSED)
            }
            else if (gamepad.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                claw.setPosition(Claw.Position.OPEN)
            }
            else if (gamepad.wasJustPressed(PS5Keys.Button.DPAD_RIGHT.xboxButton)) {
                claw.setPosition(Claw.Position.SUPER_OPEN)
            }
        }
    }
}
