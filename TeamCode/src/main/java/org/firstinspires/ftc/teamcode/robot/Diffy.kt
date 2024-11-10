package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Diffy(hardwareMap: HardwareMap) {

    private val pivotLeft: Servo = hardwareMap.servo.get("diffypivot2").apply {
        direction = Servo.Direction.FORWARD
    }
    private val pivotRight: Servo = hardwareMap.servo.get("diffypivot1").apply {
        direction = Servo.Direction.REVERSE
    }
    private val ingest1: CRServo = hardwareMap.crservo.get("diffyingest1").apply {
        direction = DcMotorSimple.Direction.FORWARD
    }
    private val ingest2: CRServo = hardwareMap.crservo.get("diffyingest2").apply {
        direction = DcMotorSimple.Direction.REVERSE
    }

    enum class DiffyPosition {
        INGEST,
        CRUISE,
        SCORE
    }

    fun getLeftPosition(): Double {
        return pivotLeft.position
    }

    fun getRightPosition(): Double {
        return pivotRight.position
    }

    fun setDiffyPosition(position: DiffyPosition) {
        when (position) {
            // right trigger
            DiffyPosition.INGEST -> {
                pivotLeft.position = 0.65
                pivotRight.position = 0.65
            }
            // left trigger
            DiffyPosition.CRUISE -> {
                pivotLeft.position = 0.9
                pivotRight.position = 0.9
            }
            DiffyPosition.SCORE -> {
                pivotLeft.position = 0.0
                pivotRight.position = 0.0
            }
        }
    }

    /**
     * right trigger
     */
    fun ingestForward() {
        ingest1.power = 1.0
        ingest2.power = 1.0
    }

    fun stopIngestion() {
        ingest1.power = 0.05
        ingest2.power = 0.05
    }

    /**
     * left trigger
     */
    fun ingestReverse() {
        ingest1.power = -1.0
        ingest2.power = -1.0
    }
}
