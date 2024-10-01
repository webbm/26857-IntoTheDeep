package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class LowerSlide(hardwareMap: HardwareMap) {

    companion object {
        const val INCREMENT = 0.001
        const val MAX_POSITION = 0.35
        const val MIN_POSITION = 0.0
    }

    private val left: Servo = hardwareMap.servo.get("servo").apply {
        direction = Servo.Direction.REVERSE
    }

    fun setPosition(position: Double) {
        this.left.position = position
    }

    fun getPosition(): Double {
        return left.position
    }

    fun reach() {
        left.position += 0.1
    }

    fun retract() {
        left.position -= 0.1
    }
}
