package com.writeitup.wiu_user_service.service.impl;

import com.writeitup.wiu_user_service.domain.User;
import com.writeitup.wiu_user_service.dto.UserDTO;
import com.writeitup.wiu_user_service.exception.UserNotFoundException;
import com.writeitup.wiu_user_service.repository.UserRepository;
import com.writeitup.wiu_user_service.service.UserService;
import com.writeitup.wiu_user_service.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.writeitup.wiu_user_service.util.JwtUtil.getJwtClaim;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO getCurrentUser() {
        log.debug("Retrieving current user");
        final String email = getJwtClaim("email");
        final RealmResource realmResource = keycloak.realm(realm);
        final List<UserRepresentation> userRepresentations = realmResource.users().searchByEmail(email, true);
        if (userRepresentations.isEmpty()) {
            log.warn("There's no user found for email=[{}]", email);
            throw new UserNotFoundException(String.format("User not found for email: %s", email));
        }

        final UserRepresentation currentUser = userRepresentations.getFirst();
        final Optional<User> currentUserFromDb = userRepository.findById(UUID.fromString(currentUser.getId()));
        log.debug("Successfully retrieved current user");
        return createCombinedUserDTO(currentUserFromDb, currentUser);
    }

    @Override
    public UserDTO update(final UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found id: %s", userDTO.getId())));
        userMapper.updateUser(userDTO, user);
        user = userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    private User create(final UserRepresentation currentUser) {
        final User user = User.builder()
                .id(UUID.fromString(currentUser.getId()))
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .imageData(new byte[]{})
                .build();
        return userRepository.save(user);
    }

    private UserDTO createCombinedUserDTO(final Optional<User> currentUserFromDb, final UserRepresentation currentUser) {
        if (currentUserFromDb.isEmpty()) {
            return userMapper.toUserDTO(create(currentUser));
        } else {
            User user = currentUserFromDb.get();
            return userMapper.toUserDTO(user);
        }
    }
}
