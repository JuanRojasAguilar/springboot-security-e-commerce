package com.dailycodework.dreamshops.data;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
  private final UserRepository userRepository;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent e) {
    createDefaultUserIfNotExits();
  }

  private void createDefaultUserIfNotExits() {
    for(int i = 1; i <= 5; ++i) {
      String defaultEmail = "user" + i + "email.com";
      if (userRepository.existsByEmail(defaultEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("User");
      user.setLastName("Number" + i);
      user.setEmail(defaultEmail);
      user.setPassword("123456");
      userRepository.save(user);
      System.out.println("User " + i + " Created");
    }
  }
}
