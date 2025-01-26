package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap

class Wrist(hardwareMap: HardwareMap) {
    private val wrist = hardwareMap.servo.get("wrist")

    private var position: Position = Position.OUT_TAKE

    init {
        setPosition(position)
    }

    enum class Position(val position: Double) {
        INTAKE(0.0),
        LINE_UP(0.15),
        MID(0.2),
        OUT_TAKE(0.35),
    }

    fun setPosition(position: Position) {
        this.position = position
        wrist.position = position.position
    }

    fun setRawPosition(position: Double) {
        wrist.position = position
    }

    fun getRawPosition(): Double {
        return wrist.position
    }

    fun getPosition(): Position {
        return position
    }
}
