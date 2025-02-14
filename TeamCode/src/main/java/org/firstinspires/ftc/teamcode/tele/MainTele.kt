package org.firstinspires.ftc.teamcode.tele

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Pivot
import org.firstinspires.ftc.teamcode.robot.VerticalSlide
import org.firstinspires.ftc.teamcode.robot.Wrist
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.max

@TeleOp
class MainTele : LinearOpMode() {

    private data class PickupStep(
        val wristPosition: Wrist.Position?,
        val clawPosition: Claw.Position?,
        val threshold: Long,
        val startTime: Long?,
        val nextStep: PickupStep?
    ) {
        fun isComplete(): Boolean {
            return System.currentTimeMillis() - (startTime ?: 0) > threshold
        }
    }

    override fun runOpMode() {
        val dc = GamepadEx(gamepad1)
        val mc = GamepadEx(gamepad2)

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

        var step: PickupStep? = null

        waitForStart()

        val pivot = Pivot(hardwareMap)
        val claw = Claw(hardwareMap)
//        val specClaw = SpecClaw(hardwareMap)
        val wrist = Wrist(hardwareMap)
        val verticalSlide = VerticalSlide(hardwareMap)
        var manualOverride = false
        var resetEncoders = false

        while (opModeIsActive()) {
            dc.readButtons()
            mc.readButtons()
            val driveSpeedMultiplier = max(0.5, (1.0 - dc.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)))

            // drivetrain
            driveTrain.driveRobotCentric(
                -dc.leftX * driveSpeedMultiplier,
                -dc.leftY * driveSpeedMultiplier,
                -dc.rightX * driveSpeedMultiplier
            )

            val driverDpadDown = dc.getGamepadButton(PS5Keys.Button.DPAD_DOWN.xboxButton)
            val driverDpadUp = dc.getGamepadButton(PS5Keys.Button.DPAD_UP.xboxButton)

            val driverCrossButton = dc.getGamepadButton(PS5Keys.Button.CROSS.xboxButton)
            val driverTriangleButton = dc.getGamepadButton(PS5Keys.Button.TRIANGLE.xboxButton)

            if (!manualOverride && driverDpadDown.and(driverCrossButton).get()) {
                manualOverride = true
            }

            if (manualOverride && driverDpadUp.and(driverTriangleButton).get()) {
                resetEncoders = true
            }

            if (resetEncoders) {
                pivot.resetEncoder()
                verticalSlide.resetEncoders()
                resetEncoders = false
                manualOverride = false
            }

            if (manualOverride) {
                gamepad1.rumble(0.5, 1.0, 500)
                gamepad2.rumble(0.5, 1.0, 500)
            }
            else {
                gamepad1.stopRumble()
                gamepad2.stopRumble()
            }

            // pivot
            pivot.setPower(-gamepad2.left_stick_y * 0.35, manualOverride)

            // vertical slide
            verticalSlide.setPower(gamepad2.right_stick_y.toDouble(), pivot.getRawPosition(), manualOverride)

            if (step != null) {
                if (step.isComplete()) {
                    step = step.nextStep?.copy(startTime = System.currentTimeMillis())
                    step?.wristPosition?.let { wrist.setPosition(it) }
                    step?.clawPosition?.let { claw.setPosition(it) }
                }
            }

            if (gamepad2.right_trigger > 0.5 && step == null) {
                //picking up
                step = PickupStep(
                    wristPosition = Wrist.Position.MID,
                    clawPosition = Claw.Position.OPEN,
                    threshold = 200L,
                    startTime = System.currentTimeMillis(),
                    nextStep = PickupStep(
                        wristPosition = Wrist.Position.INTAKE,
                        clawPosition = null,
                        threshold = 200L,
                        startTime = null,
                        nextStep = PickupStep(
                            wristPosition = null,
                            clawPosition = Claw.Position.CLOSED,
                            threshold = 150L,
                            startTime = null,
                            nextStep = PickupStep(
                                wristPosition = Wrist.Position.MID,
                                clawPosition = null,
                                threshold = 0L,
                                startTime = null,
                                nextStep = null,
                            )
                        )
                    )
                )

                wrist.setPosition(step.wristPosition!!)
                claw.setPosition(step.clawPosition!!)
            }
            else if (gamepad2.left_trigger > 0.5) {
                //scoring
                wrist.setPosition(Wrist.Position.OUT_TAKE)
                sleep(100)
                claw.setPosition(Claw.Position.OPEN)
                sleep(200)
                wrist.setPosition(Wrist.Position.MID)
                step = null
            }

            val (slide1Pos, slide2Pos) = verticalSlide.getRawPositions()
            telemetry.addData("pivot position", pivot.getRawPosition())
            telemetry.addData("slide1 position", slide1Pos)
            telemetry.addData("slide2 position", slide2Pos)
            telemetry.addData("slide power", gamepad2.right_stick_y * 0.7)
            telemetry.addData("claw position", claw.getRawPosition())
            telemetry.addData("wrist position", wrist.getRawPosition())
            telemetry.addData("manual override", manualOverride)
            telemetry.addData("reset encoders", resetEncoders)
            telemetry.update()
            idle()
        }
    }
}
