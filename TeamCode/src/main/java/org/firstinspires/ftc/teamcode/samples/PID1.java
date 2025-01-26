package org.firstinspires.ftc.teamcode.samples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@Config
@TeleOp
@Disabled
public class PID1 extends OpMode {

    private PIDController controller;

    public static double p = 0.015, i = 0.003, d = 0.001;
                //then tune these^
    public static double f = 0.2; // tune this first use this video as a reminder youtube.com/watch?v=E6H6Nqe6qJo
    // to tune this^ pick up the arm and see if it resist the force of gravity

    public static double target = 0;

    private final double ticksindegree = 751.8 / 360;

    private DcMotorEx pivot1;
    private DcMotorEx pivot2;

    @Override
    public void init() {
        controller = new PIDController(p , i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        pivot1 = hardwareMap.get(DcMotorEx.class, "pivot1");
        pivot2 = hardwareMap.get(DcMotorEx.class, "pivot2");

//        pivot1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        pivot1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        pivot2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

        controller.setPID(p, i ,d);
        int pivot1Pos = pivot1.getCurrentPosition();


        double pid1 = controller.calculate(pivot1Pos, target);

        double ff = Math.cos(Math.toRadians(target / ticksindegree)) * f;

        double power = pid1 + ff;

        pivot1.setPower(power);
        pivot2.setPower(-power);

        telemetry.addData("pivot1 position", pivot1Pos);

        telemetry.addData("target", target);
        telemetry.update();
    }
}
