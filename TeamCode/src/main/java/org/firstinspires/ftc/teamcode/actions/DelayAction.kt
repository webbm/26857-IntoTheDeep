package org.firstinspires.ftc.teamcode.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

class DelayAction(private val delayMs: Long) : Action {

    private var startTime: Long? = null

    override fun run(p: TelemetryPacket): Boolean {
        if (startTime == null) {
            startTime = System.currentTimeMillis()
            return true
        }
        else {
            return System.currentTimeMillis() - startTime!! < delayMs
        }
    }

}
