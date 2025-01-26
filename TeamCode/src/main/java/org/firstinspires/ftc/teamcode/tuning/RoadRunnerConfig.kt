package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.hardware.sparkfun.SparkFunOTOS.Pose2D

interface RoadRunnerConfig {
    val otosConfig: OtosConfig
    val mecanumConfig: MecanumConfig
}

data class PrimaryOtosConfig(
    override val angularScalar: Double = 0.995,
    override val linearScalar: Double = 1.03,
    override val otosOffset: Pose2D = Pose2D(-0.204, 0.408, Math.toRadians(-91.5635)),
) : OtosConfig

data class HectorOtosConfig(
    override val angularScalar: Double = 0.995,
    override val linearScalar: Double = 1.03,
    override val otosOffset: Pose2D = Pose2D(-0.204, 0.408, Math.toRadians(-91.5635)),
) : OtosConfig

interface OtosConfig {
    val angularScalar: Double
    val linearScalar: Double
    val otosOffset: Pose2D
}

interface MecanumConfig
