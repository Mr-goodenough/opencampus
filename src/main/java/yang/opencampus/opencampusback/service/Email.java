package yang.opencampus.opencampusback.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.mail.MessagingException;

@Service
public class Email {
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    
    @Value("${spring.mail.username}")
    private String sendMailer;
    public boolean sendEmail(String toWho,int text) {
        try {
            if(validateEmail(toWho)){
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(),true);
            mimeMessageHelper.setFrom(sendMailer);
            mimeMessageHelper.setTo(toWho);
            mimeMessageHelper.setSubject("opencampus验证码");
            mimeMessageHelper.setText("您的验证码为"+text);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            return true ;
            }else{
                return false;
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败："+e.getMessage());
            return false;
        }
    }
    public static  boolean validateEmail(String email) {

        String emailRegex = "^[A-Za-z0-9+_.-]+@(?:qq\\.com|136\\.com|gmail\\.com|outlook\\.com)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() && email.length() > 10;
    }

}
