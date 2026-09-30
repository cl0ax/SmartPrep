package com.BTA.SmartPrep.repository;
import com.BTA.SmartPrep.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, UUID> {
    Optional<User> findByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO Users (user_ID, username, email, pass_Hash)
            VALUES (UUID(), :username, :email, :passHash)
            """, nativeQuery = true)
    void insertUser(
            @Param("username") String username,
            @Param("email") String email,
            @Param("passHash") String passHash
    );
    User findByEmail(String email);
}
