package com.sirius.mailSender.mailProviders.interfaces;

import com.sirius.mailSender.models.MailEntity;


public interface MailProvider {

    boolean sendMail(MailEntity mailEntity);

}
