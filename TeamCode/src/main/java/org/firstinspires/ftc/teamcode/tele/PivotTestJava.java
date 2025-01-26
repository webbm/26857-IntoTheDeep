package org.firstinspires.ftc.teamcode.tele;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp
@Disabled
public class PivotTestJava extends LinearOpMode {
    // Static variables for dashboard configuration
    public static double p = 0.00015;
    public static double i = 0.00001;
    public static double d = 0.00001;
    public static double f = 0.002;
    public static double target = 2000.0;

//    private final double ticksPerDegree = 11.37;
//    private final double ticksPerDegree = 751.8 / 360.0;
    public static double ticksPerDegree = 36.0;

    @Override
    public void runOpMode() {
        DcMotor pivotBottom = hardwareMap.get(DcMotor.class, "pivot_bottom");
//        pivotBottom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        pivotBottom.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivotBottom.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDController bottomController = new PIDController(p, i, d);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        while (opModeIsActive()) {
            bottomController.setPID(p, i, d);

            double currentPosition = pivotBottom.getCurrentPosition();

            double bottomPid = bottomController.calculate(currentPosition, target);
            double ffs = Math.cos(Math.toRadians(target / ticksPerDegree)) * f;

            double bottomPower = bottomPid + ffs;

            pivotBottom.setPower(bottomPower);

            telemetry.addData("Position", currentPosition);
            telemetry.addData("PID", bottomPid);
            telemetry.addData("ffs", ffs);
            telemetry.addData("Bot Power", bottomPower);
            telemetry.update();
            idle();
        }
    }
}
