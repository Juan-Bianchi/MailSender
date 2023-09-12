package com.sirius.mailSender.mailProviders.classes;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sirius.mailSender.mailProviders.interfaces.MailProvider;
import com.sirius.mailSender.models.MailEntity;

import java.io.IOException;


public class SendGridProvider implements MailProvider {

    public SendGridProvider() {
    }

    @Override
    public boolean sendMail(MailEntity mailEntity) {
        Email from = new Email(mailEntity.getSender().getEmail());
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(mailEntity.getSubject());
        Personalization personalization = new Personalization();
        for(String recipient: mailEntity.getRecipients()){
            personalization.addTo(new Email(recipient));
        };
        mail.addPersonalization(personalization);
        Content content = new Content("text/plain", mailEntity.getMessage());
        mail.addContent(content);
        System.out.println(System.getenv("SENDGRID_API_KEY"));
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        return completeRequest(request, mail, sg);
    }

    private boolean completeRequest(Request request, Mail mail, SendGrid sendGrid) {
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }
}
