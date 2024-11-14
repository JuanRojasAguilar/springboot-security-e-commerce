package com.dailycodework.dreamshops.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.requests.CreateUserRequest;
import com.dailycodework.dreamshops.requests.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public UserDto getUserById(Long userId) {
    return userRepository.findById(userId)
        .map(this::convertToDto)
        .orElseThrow(
            () -> new ResourceNotFoundException("User Not Found"));
  }

  @Override
  public UserDto createUser(CreateUserRequest request) {
    return Optional
        .of(request)
        .filter(user -> !userRepository.existsByEmail(request.getEmail()))
        .map(req -> {
          User user = new User();
          user.setEmail(req.getEmail());
          user.setFirstName(req.getFirstName());
          user.setLastName(req.getLastName());
          user.setPassword(req.getPassword());
          userRepository.save(user);
          return convertToDto(user);
        })
        .orElseThrow(() -> {
          throw new AlreadyExistsException(request.getEmail() + " already registered");
        });
  }

  @Override
  public UserDto updateUser(UpdateUserRequest request, Long userId) {
    return userRepository
        .findById(userId)
        .map(userInstance -> {
          userInstance.setFirstName(request.getFirstName());
          userInstance.setLastName(request.getLastName());
          User user = userRepository.save(userInstance);
          return convertToDto(user);
        })
        .orElseThrow(() -> {
          throw new ResourceNotFoundException("User Not found");
        });
  }

  @Override
  public void deleteUser(Long userId) {
    userRepository
        .findById(userId)
        .ifPresentOrElse(
            userRepository::delete,
            () -> {
              throw new ResourceNotFoundException("User Not Found");
            });
  }

  private UserDto convertToDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }
}
