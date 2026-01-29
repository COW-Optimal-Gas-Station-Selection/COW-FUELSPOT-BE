package com.cow.fuelspot.domain.auth.repository;

import com.cow.fuelspot.domain.auth.entity.EmailVerification;
import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {

}
