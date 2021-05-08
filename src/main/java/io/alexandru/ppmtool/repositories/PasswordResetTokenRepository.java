package io.alexandru.ppmtool.repositories;

import io.alexandru.ppmtool.payload.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository <PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);


    void deleteAllByUserId(Long userId);

}
