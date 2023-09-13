package com.sirius.mailSender.services.implementations;

import com.mailjet.client.ClientOptions;
import com.sirius.mailSender.dtos.MailSentDTO;
import com.sirius.mailSender.mailProviders.classes.MailJetProvider;
import com.sirius.mailSender.mailProviders.classes.SendGridProvider;
import com.sirius.mailSender.models.MailEntity;
import com.sirius.mailSender.models.UserEntity;
import com.sirius.mailSender.repositories.MailRepository;
import com.sirius.mailSender.repositories.UserEntityRepository;
import com.sirius.mailSender.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MailServiceImplementation implements MailService {

    private final MailRepository mailRepository;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public MailServiceImplementation (MailRepository mailRepository, UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.mailRepository = mailRepository;
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
    public void sendMail(MailSentDTO mailSentDTO) {
        if(thereIsANullField(mailSentDTO.getSubject(), mailSentDTO.getMessage(), mailSentDTO.getSender(), mailSentDTO.getRecipients())) {
            throw new RuntimeException(verifyNullFields(mailSentDTO.getSubject(), mailSentDTO.getMessage(), mailSentDTO.getSender(), mailSentDTO.getRecipients()));
        }
        UserEntity user = userEntityRepository.findByUserName(mailSentDTO.getSender()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User does not exists.");
        }
        int totalMails = mailSentDTO.getRecipients().size() + user.getSentEmails();
        if(totalMails > 1000) {
            throw new RuntimeException("This user has already reached the 1000 daily messages limit.");
        }

        MailEntity mailEntity = new MailEntity(mailSentDTO.getSubject(), mailSentDTO.getMessage(), mailSentDTO.getRecipients(), LocalDateTime.now());

        user.addAnEMail(mailEntity);
        doSendMail(mailEntity);
        user.setSentEmails(totalMails);
        userEntityRepository.save(user);
        mailRepository.save(mailEntity);
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

    private void doSendMail(MailEntity mailEntity){
        ClientOptions options = ClientOptions.builder()
                .apiKey(System.getenv("API_KEY_PUBLIC_MAILJET"))
                .apiSecretKey(System.getenv("API_KEY_PRIVATE_MAILJET"))
                .build();

        MailJetProvider client = new MailJetProvider(options);
        boolean mailJetHasSentTheMail = client.sendMail(mailEntity);
        if(!mailJetHasSentTheMail) {
            SendGridProvider sendGridProvider = new SendGridProvider();
            boolean sendGridHasSentTheMail = sendGridProvider.sendMail(mailEntity);
            if(!sendGridHasSentTheMail){
                throw new RuntimeException("The mail could not be sent.");
            }
        }

    }

}
