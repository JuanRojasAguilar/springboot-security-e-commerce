package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.requests.CreateUserRequest;
import com.dailycodework.dreamshops.requests.UpdateUserRequest;

public interface UserService {
  UserDto getUserById(Long userId);
  UserDto createUser(CreateUserRequest request);
  UserDto updateUser(UpdateUserRequest request, Long userId);
  void deleteUser(Long userId);

  UserDto getAuthenticatedUser();
}
