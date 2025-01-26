package org.firstinspires.ftc.teamcode.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import org.firstinspires.ftc.teamcode.robot.Pivot

class PivotUpAction(
    private val pivot: Pivot,
    private val desiredPosition: Int,
) : Action {
    override fun run(p: TelemetryPacket): Boolean {
        val shouldRun = pivot.getRawPosition() > desiredPosition

        if (!shouldRun) {
            pivot.setActionPower(0.0)
            return false
        }

        pivot.setActionPower(0.3)

        return true
    }
}
