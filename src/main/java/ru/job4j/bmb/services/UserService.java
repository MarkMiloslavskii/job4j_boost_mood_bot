package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(Long clientId, Long chatId) {
        User user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
    }
}