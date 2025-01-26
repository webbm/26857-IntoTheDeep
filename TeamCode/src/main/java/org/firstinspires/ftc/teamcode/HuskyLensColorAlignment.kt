package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import com.qualcomm.hardware.dfrobot.HuskyLens
import com.qualcomm.robotcore.hardware.DcMotorSimple

class HuskyLensColorAlignment(
    hardwareMap: HardwareMap,
    private val telemetry: Telemetry
) {
    private val huskyLens: HuskyLens
    private val frontLeft: DcMotor
    private val frontRight: DcMotor
    private val backLeft: DcMotor
    private val backRight: DcMotor

    init {
        // Initialize HuskyLens
        huskyLens = hardwareMap.get(HuskyLens::class.java, "huskylens")

        // Initialize Motors
        frontLeft = hardwareMap.get(DcMotor::class.java, "left_front")
        frontRight = hardwareMap.get(DcMotor::class.java, "right_front")
        backLeft = hardwareMap.get(DcMotor::class.java, "left_back")
        backRight = hardwareMap.get(DcMotor::class.java, "right_back")

        // Set motor directions
        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        backLeft.direction = DcMotorSimple.Direction.REVERSE
        frontRight.direction = DcMotorSimple.Direction.FORWARD
        backRight.direction = DcMotorSimple.Direction.FORWARD

        // Set zero power behavior
        listOf(frontLeft, frontRight, backLeft, backRight).forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }
    }

    fun alignWithColorTarget(
        targetColorId: Int = 1,
        centerThreshold: Int = 20,
        power: Double = 0.3
    ): Boolean {
        huskyLens.blocks()?.firstOrNull { it.id == targetColorId }?.let { block ->
            val screenCenter = block.width / 2
            val blockCenter = block.x
            val offset = blockCenter - screenCenter

            telemetry.addData("Color ID", block.id)
            telemetry.addData("Offset", offset)
            telemetry.update()

            when {
                offset > centerThreshold -> {
                    // Move right
                    setMecanumPower(0.0, 0.0, power)
                }
                offset < -centerThreshold -> {
                    // Move left
                    setMecanumPower(0.0, 0.0, -power)
                }
                else -> {
                    // Centered - stop motors
                    stopMotors()
                    return true
                }
            }
        } ?: run {
            telemetry.addData("Status", "No color target found")
            telemetry.update()
            stopMotors()
        }
        return false
    }

    private fun setMecanumPower(drive: Double, strafe: Double, turn: Double) {
        val powers = listOf(
            drive + strafe + turn,  // Front Left
            drive - strafe - turn,  // Front Right
            drive - strafe + turn,  // Back Left
            drive + strafe - turn   // Back Right
        )

        val maxPower = powers.map { kotlin.math.abs(it) }.maxOrNull() ?: 1.0
        val scale = if (maxPower > 1.0) 1.0 / maxPower else 1.0

        frontLeft.power = powers[0] * scale
        frontRight.power = powers[1] * scale
        backLeft.power = powers[2] * scale
        backRight.power = powers[3] * scale
    }

    private fun stopMotors() {
        listOf(frontLeft, frontRight, backLeft, backRight).forEach {
            it.power = 0.0
        }
    }
}
