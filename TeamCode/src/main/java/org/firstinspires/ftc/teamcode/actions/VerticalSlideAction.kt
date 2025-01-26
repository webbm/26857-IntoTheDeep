package org.firstinspires.ftc.teamcode.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import org.firstinspires.ftc.teamcode.robot.VerticalSlide

class VerticalSlideAction(
    private val verticalSlide: VerticalSlide,
    private val position: Int,
) : Action {

    override fun run(p: TelemetryPacket): Boolean {
        verticalSlide.setPosition(position)
        return false
    }
}
