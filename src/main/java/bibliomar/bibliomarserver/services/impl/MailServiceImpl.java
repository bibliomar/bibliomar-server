package bibliomar.bibliomarserver.services.impl;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.services.MailService;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${bibliomar.client.url}")
    private String clientUrl;

    private String getFrom() {
        return String.format("Bibliomar Team <%s>", this.clientUrl);
    }

    public String getRecoverUrl() {
        return "https://" + clientUrl + "/user/recover";
    }

    public String getVerificationUrl() {
        return "https://" + clientUrl + "/user/verify";
    }

    @Override
    public void sendRecoveryMail(User recipient, String token) throws MessagingException {

        String recoverAccountLink = this.getRecoverUrl() + "?token=" + token;

        Context context = new Context();
        context.setVariable("recoverAccountLink", recoverAccountLink);
        context.setVariable("userEmail", recipient.getEmail());

        String body = templateEngine.process("account_recover.html", context);
        try {
            sendMail(recipient.getEmail(), "Bibliomar Account Recovery", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a verification email to the user
     * @param recipient
     * @param token
     * @throws MessagingException
     */
    @Override
    public void sendVerificationMail(User recipient, String token) throws MessagingException {
        
        String verifyAccountAnchor = this.getVerificationUrl() + "?token=" + token;

        Context context = new Context();
        context.setVariable("verifyAccountLink", verifyAccountAnchor);
        context.setVariable("userEmail", recipient.getEmail());
        String body = templateEngine.process("account_verification.html", context);
       
        sendMail(recipient.getEmail(), "Account Verification Confirmation", body);

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
