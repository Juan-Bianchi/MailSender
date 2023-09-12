package com.sirius.mailSender.mailProviders.classes;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import com.sirius.mailSender.mailProviders.interfaces.MailProvider;
import com.sirius.mailSender.models.MailEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public class MailJetProvider extends MailjetClient implements MailProvider {

    public MailJetProvider(ClientOptions clientOptions) {
        super(clientOptions);
    }

    @Override
    public boolean sendMail(MailEntity mailEntity) {

        JSONArray recipientsArray = new JSONArray();
        for (String recipient : mailEntity.getRecipients()) {
            recipientsArray.put(new JSONObject()
                    .put("Email", recipient));
        }

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                        .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", mailEntity.getSender().getEmail()))
                                .put(Emailv31.Message.TO, recipientsArray)
                                .put(Emailv31.Message.SUBJECT, mailEntity.getSubject())
                                .put(Emailv31.Message.TEXTPART, mailEntity.getMessage())));
        try {
            MailjetResponse response = getResponse(request);
            int status = response.getStatus();
            return status == 200 || status == 201;
        }
        catch (MailjetException e) {
            throw new RuntimeException(e);
        }
    }

    MailjetResponse getResponse(MailjetRequest request) throws MailjetException {
        return this.post(request);
    }

}
