package ru.practicum.ewm.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
