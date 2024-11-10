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
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@Config
@TeleOp
public class SlidePivotTuner extends OpMode {

    private PIDController controller1;
    private PIDController controller2;

    public static double p1 = 0.0, i1 = 0.0, d1 = 0.0;
//    public static double p2 = 0.0, i2 = 0.0, d2 = 0.0;
                //then tune these^
    public static double f1 = 0.0; // tune this first use this video as a reminder youtube.com/watch?v=E6H6Nqe6qJo
    // to tune this^ pick up the arm and see if it resist the force of gravity

    public static double target = 0;

    private final double ticksPerDegree = 751.8 / 360;

    private DcMotorEx pivot1;
    private DcMotorEx pivot2;

    @Override
    public void init() {

        pivot1 = hardwareMap.get(DcMotorEx.class, "pivot1");
        pivot2 = hardwareMap.get(DcMotorEx.class, "pivot2");

        pivot1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        pivot1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        pivot2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        pivot2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        controller1 = new PIDController(p1, i1, d1);
//        controller2 = new PIDController(p2, i2, d2);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

//        pivot2.setDirection(DcMotorSimple.Direction.REVERSE);

        int pivot1Pos = pivot1.getCurrentPosition();
        int pivot2Pos = pivot2.getCurrentPosition();

        telemetry.addData("pivot1 position", pivot1Pos);
        telemetry.addData("pivot2 position", pivot2Pos);
        telemetry.addData("target", target);
        telemetry.update();
    }

    @Override
    public void loop() {

        controller1.setPID(p1, i1 ,d1);
//        controller2.setPID(p2, i2 ,d2);

        int pivot1Pos = pivot1.getCurrentPosition();
        int pivot2Pos = pivot2.getCurrentPosition();

        double pid1 = controller1.calculate(pivot1Pos, target);
        double ff1 = Math.cos(Math.toRadians(target / ticksPerDegree)) * f1;

//        double pid2 = controller2.calculate(pivot2Pos, target);
//        double ff2 = Math.cos(Math.toRadians(target / ticksPerDegree)) * f2;

        double power1 = pid1 + ff1;
//        double power2 = pid2 + ff2;

        pivot1.setPower(power1);
        pivot2.setPower(power1);

        telemetry.addData("pivot1 position", pivot1Pos);
        telemetry.addData("pivot2 position", pivot2Pos);
        telemetry.addData("target", target);
        telemetry.update();
    }
}
