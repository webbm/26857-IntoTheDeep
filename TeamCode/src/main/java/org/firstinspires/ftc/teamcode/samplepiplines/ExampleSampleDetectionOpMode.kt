package org.firstinspires.ftc.teamcode.samplepiplines

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Wrist

@TeleOp(name = "Sample Heading Example")
class ExampleSampleDetectionOpMode : LinearOpMode() {
    override fun runOpMode() {
        val sampleDetector = YellowSampleDetector(hardwareMap)
        val wrist = Wrist(hardwareMap)
        wrist.setPosition(Wrist.Position.LINE_UP)

        waitForStart()

        telemetry.addData("Sample Visible", sampleDetector.isSampleVisible())
        telemetry.addData("Sample Heading", "%.1f°", sampleDetector.getSampleHeading())
        telemetry.update()

        while (opModeIsActive()) {
            telemetry.addData("Sample Visible", sampleDetector.isSampleVisible())
            telemetry.addData("Sample Heading", "%.1f°", sampleDetector.getSampleHeading())
            telemetry.update()
        }

        sampleDetector.stopCamera()
    }
}
