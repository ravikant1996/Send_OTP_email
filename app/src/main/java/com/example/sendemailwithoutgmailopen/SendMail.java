package com.example.sendemailwithoutgmailopen;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.example.sendemailwithoutgmailopen.SendOTP.VERIFIED;

public class SendMail extends AsyncTask<Void,Void,Void> {
    private Context context;
    private Session session;
    private String email;
    private String subject;
    private String message;
    EditText otp;
    TextView otphead;
    Button buttonSend;
    TextView timer;
    private ProgressDialog progressDialog;
    private boolean status;

    public SendMail(Context context, String email, String subject, String message, EditText otp, TextView otphead, Button buttonSend, TextView timer){
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.otp = otp;
        this.otphead = otphead;
        this.buttonSend = buttonSend;
        this.timer = timer;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        otp.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        otphead.setVisibility(View.VISIBLE);
        buttonSend.setTag(VERIFIED);
        buttonSend.setText("verify");
        status=true;
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(Config.EMAIL));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);
            Transport.send(mm);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setTimer() {
        return status;
    }
}
