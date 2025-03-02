package com.codershubham.cms.cms.repository.AuthModules;

import com.codershubham.cms.cms.model.AuthModel.PasswordResetTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenModel, Long> {
    Optional<PasswordResetTokenModel> findByToken(String token);
}