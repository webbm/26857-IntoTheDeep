package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Diffy(hardwareMap: HardwareMap) {
    private var polarity = Polarity.REGULAR
    private val left: Servo = hardwareMap.servo.get("diffy_left").apply {
        direction = Servo.Direction.FORWARD
    }
    private val right: Servo = hardwareMap.servo.get("diffy_right").apply {
        direction = Servo.Direction.REVERSE
    }
    private val ingest: CRServo = hardwareMap.crservo.get("diffy_ingest")

    enum class DiffyPosition {
        INGEST,
        CRUISE,
        PARKED,
        SCORE
    }

    enum class Polarity {
        REGULAR,
        IDENTICAL,
        INVERTED;

        fun next(): Polarity {
            return when (this) {
                REGULAR -> IDENTICAL
                IDENTICAL -> INVERTED
                INVERTED -> REGULAR
            }
        }
    }

    fun getPolarity(): Polarity {
        return polarity
    }

    fun togglePolarity() {
        polarity = polarity.next()
        when (polarity) {
            Polarity.REGULAR -> {
                left.direction = Servo.Direction.REVERSE
                right.direction = Servo.Direction.FORWARD
            }

            Polarity.IDENTICAL -> {
                left.direction = Servo.Direction.FORWARD
                right.direction = Servo.Direction.FORWARD
            }

            Polarity.INVERTED -> {
                left.direction = Servo.Direction.FORWARD
                right.direction = Servo.Direction.REVERSE
            }
        }
    }

    fun getLeftPosition(): Double {
        return left.position
    }

    fun getRightPosition(): Double {
        return right.position
    }

    fun setPositionManually(leftPosition: Double, rightPosition: Double = leftPosition) {
        left.position = leftPosition
        right.position = rightPosition
    }

    fun lift() {
        val increment = 0.05
        left.position += increment
        right.position += increment
    }

    fun lower() {
        val increment = 0.05
        left.position -= increment
        right.position -= increment
    }

    fun setDiffyPosition(position: DiffyPosition) {
        when (position) {
            // right trigger
            DiffyPosition.INGEST -> {
                left.position = 0.05
                right.position = 0.05
            }
            // left trigger
            DiffyPosition.CRUISE -> {
                left.position = 0.35
                right.position = 0.35
            }
            // triangle, toggle with cruise
            DiffyPosition.PARKED -> {
                left.position = 0.55
                right.position = 0.55
            }
            DiffyPosition.SCORE -> {
                left.position = 0.7
                right.position = 0.0
            }
        }
    }

    /**
     * right trigger
     */
    fun ingestForward() {
        ingest.power = 1.0
    }

    fun stopIngestion() {
        ingest.power = 0.05
    }

    /**
     * left trigger
     */
    fun ingestReverse() {
        ingest.power = -1.0
    }
}
