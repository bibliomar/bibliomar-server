package bibliomar.bibliomarserver.service.mail;

import bibliomar.bibliomarserver.model.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${bibliomar.client.url}")
    private String clientUrl;

    private String getFrom() {
        return String.format("Bibliomar Team <%s>", this.clientUrl);
    }

    public String getRecoverUrl() {
        return "https://" + clientUrl + "/user/recover";
    }

    public void sendRecoveryMail(User recipient, String token) {
        String recoverTokenUrl = this.getRecoverUrl() + "?token=" + token;
        String recoverAnchor = String.format("<a href=\"%s\">RECOVER ACCOUNT</a>", recoverTokenUrl);
        String subject = "Bibliomar Account Recovery";
        // TODO: Update to an actual HTML document
        String body = "Hello " + recipient.getUsername() + ",\n\n" +
                "You have requested to recover your account. Please click the link below to reset your password.\n\n" +
                recoverAnchor + "\n\n" +
                "Alternatively, if you can't click on the above link: \n" +
                recoverTokenUrl + "\n\n" +
                "PS: Do NOT share this email. \n" +
                "It contains an URL that can be used to access your personal Bibliomar account. \n" +
                "If you did not request to recover your password, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Bibliomar Team";
        sendMail(recipient.getEmail(), subject, body);
    }


    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.getFrom());
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);

        this.mailSender.send(message);
    }
}
