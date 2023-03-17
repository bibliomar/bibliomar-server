package bibliomar.bibliomarserver.services.impl;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.services.MailService;

@Service
public class MailServiceImpl implements MailService {

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

    @Override
    public void sendRecoveryMail(User recipient, String token) throws MessagingException {
        String recoverTokenUrl = this.getRecoverUrl() + "?token=" + token;
        String recoverAnchor = String.format("<a href=\"%s\">RECOVER ACCOUNT</a>", recoverTokenUrl);
        String subject = "Bibliomar Account Recovery";
        // TODO: Update to an actual HTML document
        String body = "Hello " + recipient.getUsername() + ",\n\n" +
                "You have requested to recover your account. Please click the link below to reset your password. <br><br>" +
                recoverAnchor + "<br><br>" +
                "Alternatively, if you can't click on the above link: <br>" +
                recoverTokenUrl + "<br>" +
                "This link will expire in 1 hour. <br><br>" +
                "PS: Do NOT share this email. <br>" +
                "It contains an URL that can be used to access your personal Bibliomar account. <br>" +
                "If you did not request to recover your password, please ignore this email. <br><br>" +
                "Best regards, <br>" +
                "Bibliomar Team";
        sendMail(recipient.getEmail(), subject, body);
    }

    @Override
    public void sendMail(String to, String subject, String body) throws MessagingException {
        MimeMessage helper = this.mailSender.createMimeMessage();
        Address sendTo = new InternetAddress(to);

        helper.setText(body, "UTF-8", "html");
        helper.setFrom(this.getFrom());
        helper.setSubject(subject);
        helper.setRecipient(MimeMessage.RecipientType.TO, sendTo);

        this.mailSender.send(helper);
    }
    
}
