package org.firstinspires.ftc.teamcode.tele

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.*
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

            // pivot
            pivot.setPower(-gamepad2.left_stick_y * 0.35)

            // vertical slide
            verticalSlide.setPower(gamepad2.right_stick_y.toDouble(), pivot.getRawPosition())

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
                    wristPosition = Wrist.Position.LINE_UP,
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
                            threshold = 200L,
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
            } else if (gamepad2.left_trigger > 0.5) {
                //scoring
                wrist.setPosition(Wrist.Position.OUT_TAKE)
                sleep(100)
                claw.setPosition(Claw.Position.OPEN)
                sleep(200)
                wrist.setPosition(Wrist.Position.LINE_UP)
                step = null
            }

            /*
            // right trigger - closes both claws
            if (gamepad2.right_trigger > 0.5) {
                claw.setPosition(Claw.Position.CLOSED)
                sleep(200)
                wrist.setPosition(Wrist.Position.OUT_TAKE)
            }

            // left trigger - opens both claws
            if (gamepad2.left_trigger > 0.5) {
                claw.setPosition(Claw.Position.OPEN)
                sleep(200)
                wrist.setPosition(Wrist.Position.LINE_UP)
            }

            // right bumper - wrist intake
            if (mc.wasJustPressed(PS5Keys.Button.RIGHT_BUMPER.xboxButton)) {
                wrist.setPosition(Wrist.Position.INTAKE)
            }
            // left bumper - wrist outtake
            else if (mc.wasJustPressed(PS5Keys.Button.LEFT_BUMPER.xboxButton)) {
                wrist.setPosition(Wrist.Position.OUT_TAKE)
            }
            // x - wrist middle
            else if (mc.wasJustPressed(PS5Keys.Button.CROSS.xboxButton)) {
                wrist.setPosition(Wrist.Position.MID)
            }
             */

            telemetry.addData("pivot position", pivot.getRawPosition())
            telemetry.addData("slide position", verticalSlide.getRawPosition())
            telemetry.addData("slide power", gamepad2.right_stick_y * 0.7)
            telemetry.addData("claw position", claw.getRawPosition())
            telemetry.addData("wrist position", wrist.getRawPosition())
            telemetry.update()
            idle()
        }
    }
}
