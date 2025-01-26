package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class Pivot(hardwareMap: HardwareMap) {
//    var targetPosition: Int
    private val pivot = hardwareMap.dcMotor.get("pivot_bottom").apply {
        direction = DcMotorSimple.Direction.REVERSE
        mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        mode = DcMotor.RunMode.RUN_USING_ENCODER
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun getRawPosition(): Int {
        return pivot.currentPosition
    }

    /**
     * if the current position is less than -4000, don't allow postive power
     * if the current position is greater than 0, don't allow negative power
     */
    fun setPower(power: Double) {
        if (pivot.currentPosition < -4000 && power > 0) {
            pivot.power = 0.0
        } else if (pivot.currentPosition > 0 && power < 0) {
            pivot.power = 0.0
        } else {
            pivot.power = power
        }
    }

    fun setActionPower(power: Double) {
        pivot.power = power
    }

    fun runToPosition(position: Int) {
        pivot.targetPosition = position
        pivot.mode = DcMotor.RunMode.RUN_TO_POSITION
        pivot.power = 0.25
    }

    fun manualLiftToPosition(position: Int) {
        while (pivot.currentPosition > position) {
            pivot.power = 0.3
        }
        pivot.power = 0.00
    }

    fun manualLowerToFloor() {
        while (pivot.currentPosition < -100) {
            pivot.power = -0.3
        }
        pivot.power = 0.00
    }
}
