package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Claw(hardwareMap: HardwareMap) {
    private val claw = hardwareMap.servo.get("claw").apply {
        direction = Servo.Direction.FORWARD
    }
    private var position: Position = Position.CLOSED

    init {
        setPosition(position)
    }

    enum class Position(val position: Double) {
        OPEN(0.9),
        CLOSED(0.8),
        SUPER_OPEN(1.0)
    }

    fun setPosition(position: Position) {
        this.position = position
        claw.position = position.position
    }

    fun getRawPosition(): Double {
        return claw.position
    }

    fun setRawPosition(position: Double) {
        claw.position = position
    }
}
