package org.firstinspires.ftc.teamcode.samples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@Config
@TeleOp
public class PID2 extends OpMode {

    private PIDController controller;

    public static double p = 0.04, i = 0.05, d = 0.0016;
                //then tune these^
    public static double f = 0.2; // tune this first use this video as a reminder youtube.com/watch?v=E6H6Nqe6qJo
    // to tune this^ pick up the arm and see if it resist the force of gravity

    public static double target = 0;

    private final double ticksindegree = 751.8 / 360;

    private DcMotorEx pivot2;

    @Override
    public void init() {
        controller = new PIDController(p , i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        pivot2 = hardwareMap.get(DcMotorEx.class, "pivot2");

        pivot2.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void loop() {

        controller.setPID(p, i ,d);
        int pivot2Pos = pivot2.getCurrentPosition();

        double pid2 = controller.calculate(pivot2Pos, target);

        double ff = Math.cos(Math.toRadians(target / ticksindegree)) * f;

        double power = pid2 + ff;

        pivot2.setPower(power);

        telemetry.addData("pivot2 position", pivot2Pos);
        telemetry.addData("target", target);
        telemetry.update();
    }
}
