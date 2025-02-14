package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.roadrunner.*
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.PinpointDrive
import org.firstinspires.ftc.teamcode.actions.*
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Pivot
import org.firstinspires.ftc.teamcode.robot.VerticalSlide
import org.firstinspires.ftc.teamcode.robot.Wrist

interface PositionData {
    val startingPosition: Pose2d
    val scoringPosition: Vector2d
    val turnToBasket: Double
    val firstSample: Double
    val secondSample: Double
    val thirdSample: Double
    val firstSampleReachDistance: Int
    val secondSampleReachDistance: Int
    val thirdSampleReachDistance: Int
}

data class Field1RedPositionData(
    override val startingPosition: Pose2d = Pose2d(-35.0, -62.75, Math.toRadians(90.0)),
    override val scoringPosition: Vector2d = Vector2d(-56.5, -52.5),
    override val turnToBasket: Double = Math.toRadians(45.0),
    override val firstSample: Double = Math.toRadians(74.0),
    override val secondSample: Double = Math.toRadians(89.5),
    override val thirdSample: Double = Math.toRadians(115.0),
    override val firstSampleReachDistance: Int = -1650, // -1725,
    override val secondSampleReachDistance: Int = -1650, // -1660,
    override val thirdSampleReachDistance: Int = -1750, //-1800,
) : PositionData

data class Field1BluePositionData(
    override val startingPosition: Pose2d = Pose2d(35.0, 62.75, Math.toRadians(270.0)),
    override val scoringPosition: Vector2d = Vector2d(56.5, 52.5),
    override val turnToBasket: Double = Math.toRadians(225.0),
    override val firstSample: Double = Math.toRadians(74.0 + 180.0),
    override val secondSample: Double = Math.toRadians(89.5 + 180.0),
    override val thirdSample: Double = Math.toRadians(115.0 + 180.0),
    override val firstSampleReachDistance: Int = -1650, //-1725,
    override val secondSampleReachDistance: Int = -1650, //-1610,
    override val thirdSampleReachDistance: Int = -1750, //-1770,
) : PositionData

data class Field2RedPositionData(
    override val startingPosition: Pose2d = Pose2d(-35.0, -62.75, Math.toRadians(90.0)),
    override val scoringPosition: Vector2d = Vector2d(-56.5, -52.5),
    override val turnToBasket: Double = Math.toRadians(45.0),
    override val firstSample: Double = Math.toRadians(74.0),
    override val secondSample: Double = Math.toRadians(89.5),
    override val thirdSample: Double = Math.toRadians(115.0),
    override val firstSampleReachDistance: Int = -1650, // -1725,
    override val secondSampleReachDistance: Int = -1650, // -1660,
    override val thirdSampleReachDistance: Int = -1750, //-1800,
) : PositionData

data class Field2BluePositionData(
    override val startingPosition: Pose2d = Pose2d(35.0, 62.75, Math.toRadians(270.0)),
    override val scoringPosition: Vector2d = Vector2d(56.5, 52.5),
    override val turnToBasket: Double = Math.toRadians(225.0),
    override val firstSample: Double = Math.toRadians(74.0 + 180.0),
    override val secondSample: Double = Math.toRadians(89.5 + 180.0),
    override val thirdSample: Double = Math.toRadians(115.0 + 180.0),
    override val firstSampleReachDistance: Int = -1650, //-1725,
    override val secondSampleReachDistance: Int = -1650, //-1610,
    override val thirdSampleReachDistance: Int = -1750, //-1770,
) : PositionData

open class BaseAuto(private val positionData: PositionData) : LinearOpMode() {

    fun scoreAction(wrist: Wrist, slide: VerticalSlide, claw: Claw): Action {
        return SequentialAction(
            ParallelAction(
                WristAction(wrist, Wrist.Position.LINE_UP),
                VerticalSlideAction(slide, -2400),
            ),
            WaitUntilAction {
                slide.getRawPositions().first >= -2350
            },
            WristAction(wrist, Wrist.Position.OUT_TAKE),
            DelayAction(300),
            ClawAction(claw, Claw.Position.OPEN),
            DelayAction(200),
            WristAction(wrist, Wrist.Position.LINE_UP),
            DelayAction(100),
            VerticalSlideAction(slide, 0),
        )
    }

    fun grabSample(claw: Claw, slide: VerticalSlide, wrist: Wrist, distance: Int): Action {
        return SequentialAction(
            ClawAction(claw, Claw.Position.OPEN),
            VerticalSlideAction(slide, distance),
            DelayAction(300),
            WristAction(wrist, Wrist.Position.PUSH),
            DelayAction(650),
            WristAction(wrist, Wrist.Position.INTAKE),
            DelayAction(300),
            ClawAction(claw, Claw.Position.CLOSED),
            DelayAction(200),
            WristAction(wrist, Wrist.Position.LINE_UP),
            VerticalSlideAction(slide, 0),
            DelayAction(500),
        )
    }

    override fun runOpMode() {
        val pivot = Pivot(hardwareMap)
        val initialPivotUpAction = PivotUpAction(pivot, -3850)
        val pivotUpAction = PivotUpAction(pivot, -3850)
        val pivotDownAction = PivotDownAction(pivot)
        val slide = VerticalSlide(hardwareMap)
//        val sampleDetector = YellowSampleDetector(hardwareMap)

        waitForStart()

        val claw = Claw(hardwareMap)
        val wrist = Wrist(hardwareMap)

        var lastPose = positionData.startingPosition
        val drive = PinpointDrive(hardwareMap, lastPose)

        val strafeToScoringPositionAction = drive
            .actionBuilder(lastPose)
            .strafeToSplineHeading(
                positionData.scoringPosition,
                positionData.turnToBasket
            )
            .build()

        var performMovement = drive
            .actionBuilder(lastPose)
            .stopAndAdd(
                SequentialAction(
                    ParallelAction(
                        ClawAction(claw, Claw.Position.CLOSED),
                        WristAction(wrist, Wrist.Position.LINE_UP),
                        initialPivotUpAction,
                        strafeToScoringPositionAction,
                    ),
                )
            )

        runBlocking(performMovement.build())

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(scoreAction(wrist, slide, claw))
            )

        // score preload sample
        runBlocking(performMovement.build())

        val turnToFirstSample = drive
            .actionBuilder(drive.poseHistory.last)
            .turnTo(positionData.firstSample)
            .build()

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(
                    DelayAction(500),
                    ParallelAction(
                        turnToFirstSample,
                        pivotDownAction,
                        WristAction(wrist, Wrist.Position.INTAKE)),
                    grabSample(claw, slide, wrist, positionData.firstSampleReachDistance)
                )
            )

        runBlocking(performMovement.build())

        var turnToBasket = drive
            .actionBuilder(drive.poseHistory.last)
            .turnTo(positionData.turnToBasket)
            .build()

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(
                    ParallelAction(
                        turnToBasket, pivotUpAction
                    ),
                    scoreAction(wrist, slide, claw),
                )
            )

        // score first sample
        runBlocking(performMovement.build())

        val turnToSecondSample = drive
            .actionBuilder(drive.poseHistory.last)
            .turnTo(positionData.secondSample)
            .build()

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(
                    DelayAction(500),
                    ParallelAction(
                        turnToSecondSample,
                        pivotDownAction,
                        WristAction(wrist, Wrist.Position.INTAKE)),
                    grabSample(claw, slide, wrist, positionData.secondSampleReachDistance)
                )
            )

        runBlocking(performMovement.build())

        turnToBasket = drive
            .actionBuilder(drive.poseHistory.last)
            .turnTo(positionData.turnToBasket)
            .build()

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(
                    ParallelAction(
                        turnToBasket, pivotUpAction
                    ),
                    scoreAction(wrist, slide, claw),
                )
            )

        // score second sample
        runBlocking(performMovement.build())

        val turnToThirdSample = drive
            .actionBuilder(drive.poseHistory.last)
            .turnTo(positionData.thirdSample)
            .build()

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(
                    DelayAction(500),
                    ParallelAction(
                        turnToThirdSample,
                        pivotDownAction,
                        WristAction(wrist, Wrist.Position.INTAKE)),
                    grabSample(claw, slide, wrist, positionData.thirdSampleReachDistance)
                )
            )

        runBlocking(performMovement.build())

        turnToBasket = drive
            .actionBuilder(drive.poseHistory.last)
            .turnTo(positionData.turnToBasket)
            .build()

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(
                    ParallelAction(
                        turnToBasket, pivotUpAction
                    ),
                    scoreAction(wrist, slide, claw),
                )
            )

        // score third sample
        runBlocking(performMovement.build())

        turnToBasket = drive
            .actionBuilder(drive.poseHistory.last)
            .turnTo(positionData.turnToBasket)
            .build()

        performMovement = drive
            .actionBuilder(drive.poseHistory.last)
            .stopAndAdd(
                SequentialAction(
                    DelayAction(500),
                    ParallelAction(
                        turnToBasket,
                        pivotDownAction,
                        VerticalSlideAction(slide, 0)
                    ),
                    DelayAction(1000)
                )
            )

        runBlocking(performMovement.build())
    }
}
