package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class VerticalSlide(hardwareMap: HardwareMap) {
    private val slide = hardwareMap.dcMotor.get("slide").apply {
        direction = DcMotorSimple.Direction.REVERSE
        mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        mode = DcMotor.RunMode.RUN_USING_ENCODER
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun getRawPosition(): Int {
        return slide.currentPosition
    }

    fun setPower(power: Double, pivotPosition: Int) {
        if (pivotPosition > -2000) {
//             pivot is lower than 45 degrees, limit extension
            if (slide.currentPosition < -1901 && power < 0) {
                slide.power = 0.0
            } else if (slide.currentPosition > 0 && power > 0) {
                slide.power = 0.0
            } else {
                slide.power = power
            }
        }
        else {
            // just let it go
            slide.power = power
        }
    }

    fun setPosition(position: Int) {
        slide.targetPosition = position
        slide.mode = DcMotor.RunMode.RUN_TO_POSITION
        slide.power = 1.0
    }

}
