package com.sirius.mailSender.repositories;

import com.sirius.mailSender.models.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MailRepository extends JpaRepository<MailEntity, Long> {
}
