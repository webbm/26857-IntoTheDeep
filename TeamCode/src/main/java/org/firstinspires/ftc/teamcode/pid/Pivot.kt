package org.firstinspires.ftc.teamcode.pid

import com.arcrobotics.ftclib.controller.PIDController
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.cos

class Pivot(var telemetry: Telemetry, val hardwareMap: HardwareMap) {

    private var controller: PIDController = PIDController(p, i, d)
    private var pivot1: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "pivot1")
    private var pivot2: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "pivot2")
    var pivot1Pos = 0
    fun setTarget(target: Double) {
        controller.setPID(p, i, d)
        pivot1Pos = pivot1.currentPosition

        val pid1 = controller.calculate(pivot1Pos.toDouble(), target)

        val ff = cos(Math.toRadians(target / TICKS_PER_DEGREE)) * f

        val power = pid1 + ff

        pivot1.power = power
        pivot2.power = -power

        telemetry.addData("pivot1 position", pivot1Pos)

        telemetry.addData("target", target)
        telemetry.update()
    }

    fun getPosition(): Int {
        return pivot1Pos
    }

    companion object {
        val TICKS_PER_DEGREE = 751.8 / 360
        var p: Double = 0.015
        var i: Double = 0.003
        var d: Double = 0.001

        //then tune these^
        var f: Double = 0.2

        // to tune this^ pick up the arm and see if it resist the force of gravity
        var target: Double = 0.0
    }
}
