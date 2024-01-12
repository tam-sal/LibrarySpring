package Egg.EggLibrary.repositories;

import Egg.EggLibrary.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u where u.email = :email")
    public UserEntity findByEmail(@Param("email") String email);
}
