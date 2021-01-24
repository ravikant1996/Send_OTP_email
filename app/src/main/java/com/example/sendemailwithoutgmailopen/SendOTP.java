package com.example.sendemailwithoutgmailopen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

public class SendOTP extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail, otp;
    TextView otphead, timer;
    private Button buttonSend;
    int code;
    public static final String VERIFIED = "verify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        otp = (EditText) findViewById(R.id.otp);
        otphead = (TextView) findViewById(R.id.abc);
        timer = (TextView) findViewById(R.id.timer);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setTag("send");
        buttonSend.setOnClickListener(this);
    }

    private void sendEmail() {

        Random rand = new Random();
        code = rand.nextInt(99999) + 10000;
        editTextEmail.setEnabled(false);
        String email = editTextEmail.getText().toString().trim();
        String subject = "OTP Verification";
        String message = "Hi, your One Time Password: " + code;
        SendMail sm = new SendMail(this, email, subject, message, otp, otphead, buttonSend, timer);
        sm.execute();
        boolean status = sm.setTimer();
        if (status) {
            settimer();
        }
    }

    @Override
    public void onClick(View v) {
        if (buttonSend.getTag().toString().equals(VERIFIED)) {
            if (otp.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
            } else if (otp.getText().toString().trim().equals(code + "")) {
                Toast.makeText(this, "Verify", Toast.LENGTH_SHORT).show();
                otp.setVisibility(View.GONE);
                otphead.setVisibility(View.GONE);
                timer.setVisibility(View.GONE);
                buttonSend.setText("Send");
                buttonSend.setTag("send");
                editTextEmail.setEnabled(true);
            } else {
                Toast.makeText(this, "otp is wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            sendEmail();
//            if (buttonSend.getTag().toString().equals(VERIFIED)) {
//                settimer();
//            }
        }
    }

    public void settimer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                code = 0;
                timer.setText("expired!");
            }

        }.start();
    }
}