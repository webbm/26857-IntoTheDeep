package org.firstinspires.ftc.teamcode.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import org.firstinspires.ftc.teamcode.robot.Pivot

class PivotClearAction(
    private val pivot: Pivot,
) : Action {
    override fun run(p: TelemetryPacket): Boolean {
        val shouldRun = pivot.getRawPosition() < -3800

        if (!shouldRun) {
            pivot.setActionPower(0.0)
            return false
        }

        pivot.setActionPower(-0.025)

        return true
    }
}
