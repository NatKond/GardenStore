package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Query(value = """
                    SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM app_users u
                    JOIN orders o ON u.user_id = o.user_id
                    AND u.user_id = :userId)
                    THEN TRUE ELSE FALSE END
            """, nativeQuery = true)
    Boolean hasOrders(Long userId);
}
