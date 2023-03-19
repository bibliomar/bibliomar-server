package bibliomar.bibliomarserver.services;

import jakarta.mail.MessagingException;
import bibliomar.bibliomarserver.models.user.User;

public interface MailService {

    public void sendRecoveryMail(User recipient, String tokenValue) throws MessagingException;
    public void sendVerificationMail(User recipient, String token) throws MessagingException;
    public void sendMail(String to, String subject, String body) throws MessagingException;
}
