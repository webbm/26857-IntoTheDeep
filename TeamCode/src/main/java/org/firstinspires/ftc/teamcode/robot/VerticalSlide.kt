package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class VerticalSlide(hardwareMap: HardwareMap) {
    companion object {
        const val MAX_TICKS = 1000
        const val MIN_TICKS = 0
    }

    private val right: DcMotor = hardwareMap.dcMotor.get("vertical_slide_right").apply {
        direction = DcMotorSimple.Direction.REVERSE
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    /**
     * set power, but only when the vertical slide is within bounds
     */
    fun setPower(power: Double) {
            right.power = power
    }

    fun getPosition(): Int {
        return right.currentPosition
    }

}
