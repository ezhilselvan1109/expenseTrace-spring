package com.expensetrace.app.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${api.prefix}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String to, String userName, String token) {
        String subject = "Activate Your Account - ExpenseTrace";
        String activationLink = baseUrl + "/auth/verify?token=" + token;

        String content = """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
                        <div style="text-align: center; padding: 10px 0;">
                            <a href="http://expensetrace.vercel.app" style="text-decoration: none;">
                                <span style="font-size: 24px; font-weight: 800; color: #4f46e5;">Expense
                                    <span style="font-size: 20px; font-weight: 700; color: #111827;">Trace</span>
                                </span>
                            </a>
                        </div>
                        <p>Hi <b>%s</b>,</p>
                        <p>We’re excited to have you on board! Please activate your account by clicking the button below.</p>
                        
                        <div style="text-align: center; margin: 20px 0;">
                              <a href="%s" style="background-color: #4f46e5; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 16px;">Activate Account</a>
                        </div>

                        <p style="font-size: 14px; color: #555;">
                            This activation link will expire in 24 hours. If you did not sign up for ExpenseTrace, you can safely ignore this email.
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(userName, activationLink);

        sendHtmlEmail(to, subject, content);
    }

    public void sendOtpEmail(String to, String name, String otp) {
        String subject = "Password Reset OTP - ExpenseTrace";
        String content = """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <p>Hi <b>%s</b>,</p>
                    <p>Your OTP for password reset is: <b>%s</b></p>
                    <p>This OTP is valid for 5 minutes.</p>
                </body>
                </html>
                """.formatted(name, otp);

        sendHtmlEmail(to, subject, content);
    }

    private void sendHtmlEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}