package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class SpecClaw(hardwareMap: HardwareMap) {
    private val sclaw = hardwareMap.servo.get("specclaw").apply {
        direction = Servo.Direction.FORWARD
    }
    private var position: Position = Position.OPEN

    init {
        setPosition(position)
    }

    enum class Position(val position: Double) {
        OPEN(0.6),
        CLOSED(0.375),
    }

    fun setPosition(position: Position) {
        this.position = position
        sclaw.position = position.position
    }

    fun getRawPosition(): Double {
        return sclaw.position
    }

    fun setRawPosition(position: Double) {
        sclaw.position = position
    }
}
