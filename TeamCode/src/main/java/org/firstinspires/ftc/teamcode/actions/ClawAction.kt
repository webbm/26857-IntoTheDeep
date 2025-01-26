package org.firstinspires.ftc.teamcode.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import org.firstinspires.ftc.teamcode.robot.Claw

class ClawAction(
    private val claw: Claw,
    private val clawPosition: Claw.Position,
) : Action {

    override fun run(p: TelemetryPacket): Boolean {
        claw.setPosition(clawPosition)
        return false
    }

}
