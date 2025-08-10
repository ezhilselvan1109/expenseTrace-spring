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

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String to, String userName, String token) {
        String subject = "Activate Your Account - ExpenseTrace";
        String activationLink = "http://localhost:8080/api/v1/auth/verify?token=" + token;

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
                        <p>We’re excited to have you on board! To start tracking and managing your expenses with ease, please activate your account by clicking the button below.</p>
                        
                        <div style="text-align: center; margin: 20px 0;">
                              <a href="%s" style="background-color: #007BFF; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 16px;">Activate Account</a>
                        </div>

                        <!-- Extra Info -->
                         <p style="font-size: 14px; color: #555;">
                            This activation link will expire in 24 hours. If you did not sign up for ExpenseTrace, you can safely ignore this email.
                         </p>
                    </div>
                </body>
                </html>
                """.formatted(userName, activationLink, activationLink, activationLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send activation email", e);
        }
    }

}
