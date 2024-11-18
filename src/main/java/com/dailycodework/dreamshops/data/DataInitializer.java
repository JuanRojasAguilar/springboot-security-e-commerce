package com.dailycodework.dreamshops.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dailycodework.dreamshops.model.Role;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent e) {
    Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_CUSTOMER");
    createDefaultUserIfNotExits();
    createDefaultRolesIfNotExists(defaultRoles);
    createDefaultAminIfNotExists();
  }

  private void createDefaultUserIfNotExits() {
    Role userRole = roleRepository.findByName("ROlE_CUSTOMER").get();
    for (int i = 1; i <= 5; ++i) {
      String defaultEmail = "user" + i + "email.com";
      if (userRepository.existsByEmail(defaultEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("User");
      user.setLastName("Number" + i);
      user.setEmail(defaultEmail);
      user.setPassword(passwordEncoder.encode("123456"));
      user.setRoles(Set.of(userRole));
      userRepository.save(user);
      System.out.println("User " + i + " Created");
    }
  }

  private void createDefaultAminIfNotExists() {
    Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
    String adminEmail = "admin@email.com";
    if (userRepository.existsByEmail(adminEmail)) {
      return;
    }
    User user = new User();
    user.setFirstName("The User");
    user.setLastName("AdminUser");
    user.setEmail(adminEmail);
    user.setPassword(passwordEncoder.encode("123456admin"));
    user.setRoles(Set.of(adminRole));
    userRepository.save(user);
    System.out.println("Default admin created");
  }

  private void createDefaultRolesIfNotExists(Set<String> roles) {
    roles.stream()
        .filter(role -> roleRepository.findByName(role).isEmpty())
        .map(Role::new)
        .forEach(roleRepository::save);
  }
}
