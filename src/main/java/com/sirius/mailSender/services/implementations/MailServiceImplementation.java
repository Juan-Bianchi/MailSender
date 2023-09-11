package com.sirius.mailSender.services.implementations;

import com.sirius.mailSender.dtos.MailSentDTO;
import com.sirius.mailSender.dtos.UserEntityDTO;
import com.sirius.mailSender.models.Mail;
import com.sirius.mailSender.models.UserEntity;
import com.sirius.mailSender.repositories.MailRepository;
import com.sirius.mailSender.repositories.UserEntityRepository;
import com.sirius.mailSender.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MailServiceImplementation implements MailService {

    private final MailRepository mailRepository;
    private final UserEntityRepository userEntityRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImplementation (MailRepository mailRepository, UserEntityRepository userEntityRepository, JavaMailSender mailSender) {
        this.userEntityRepository = userEntityRepository;
        this.mailRepository = mailRepository;
        this.mailSender = mailSender;
    }

    @Override
    public List<MailSentDTO> findAll() {
        return null;
    }

    @Override
    public List<MailSentDTO> findMailsByDate() {
        return null;
    }

    @Transactional
    @Override
    public void sendMail(MailSentDTO mailSentDTO) throws MessagingException {
        if(thereIsANullField(mailSentDTO.getSubject(), mailSentDTO.getMessage(), mailSentDTO.getSender(), mailSentDTO.getRecipients())) {
            throw new RuntimeException(verifyNullFields(mailSentDTO.getSubject(), mailSentDTO.getMessage(), mailSentDTO.getSender(), mailSentDTO.getRecipients()));
        }
        UserEntity user = userEntityRepository.findByUserName(mailSentDTO.getSender()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User does not exists.");
        }
        int totalMails = mailSentDTO.getBcc().size() + mailSentDTO.getCc().size() + mailSentDTO.getRecipients().size() + user.getSentEmails();
        if(totalMails > 1000) {
            throw new RuntimeException("This user has already reached the 1000 daily messages limit.");
        }

        MimeMessage message = createMessage(mailSentDTO);
        Mail mail = new Mail(mailSentDTO.getSubject(), mailSentDTO.getMessage(), mailSentDTO.getRecipients(),
                             LocalDateTime.now(), mailSentDTO.getCc(), mailSentDTO.getBcc());

        user.addAnEMail(mail);
        mailSender.send(message);
        user.setSentEmails(totalMails);
        userEntityRepository.save(user);
        mailRepository.save(mail);
    }


    //AUXILIARY METHODS
    private boolean thereIsANullField(String subject, String message, String sender, List<String> recipients){
        return subject.isEmpty()  || message.isEmpty() || sender.isEmpty() || recipients.isEmpty();
    }

    private String verifyNullFields(String subject, String message, String sender, List<String> recipients){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("⋆ Please, fill the following fields: ");
        boolean moreThanOne = false;

        if (subject.isEmpty()){
            stringBuilder.append("subject");
            moreThanOne = true;
        }
        if (message.isEmpty()){
            if(moreThanOne){
                stringBuilder.append(", ");
            }
            else {
                moreThanOne = true;
            }
            stringBuilder.append("message");
        }
        if (sender == null){
            if(moreThanOne) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("sender");
        }
        if (recipients.isEmpty()){
            if(moreThanOne) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("recipients");
        }
        stringBuilder.append(".");

        return stringBuilder.toString();
    }

    private MimeMessage createMessage (MailSentDTO mailSentDTO) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setSubject(mailSentDTO.getSubject());
        mimeMessageHelper.setText(mailSentDTO.getMessage(), true); // configures content as HTMLif necessary

        // sender
        mimeMessageHelper.setFrom(mailSentDTO.getSender());
        // recipients
        mimeMessageHelper.setTo(mailSentDTO.getRecipients().toArray(new String[0]));
        // CC
        if (mailSentDTO.getCc() != null && !mailSentDTO.getCc().isEmpty()) {
            mimeMessageHelper.setCc(mailSentDTO.getCc().toArray(new String[0]));
        }
        // CCO
        if (mailSentDTO.getBcc() != null && !(mailSentDTO.getBcc().isEmpty())) {
            mimeMessageHelper.setBcc(mailSentDTO.getBcc().toArray(new String[0]));
        }

        return message;
    }
}
