package com.bezkoder.springjwt.services;


import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AddRequest;
import com.bezkoder.springjwt.payload.request.UpdateRequest;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;


    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> updateUser(Long id, UpdateRequest updateRequest) {
        /*if (userRepo.existsByUsername(updateRequest.username())) {
            ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
            return;
        }*/

        if (userRepo.existsByEmail(updateRequest.email()) && !id.equals(userRepo.findByEmail(updateRequest.email()).getId())) {
            throw new RuntimeException("Error: Email is already taken!");
        }
        Set<String> strRoles = updateRequest.roles();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }


        User user = userRepo.getById(id);
        user.setRoles(roles);
        //user.setUsername(updateRequest.username());
        user.setEmail(updateRequest.email());
        user.setPassword(encoder.encode(updateRequest.password()));
        userRepo.save(user);
        return userRepo.findById(id);
    }


    public Optional<User> addUser(AddRequest addRequest){
        if (userRepo.existsByUsername(addRequest.username())) {
            /*return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));*/
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepo.existsByEmail(addRequest.email())) {
           /* return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));*/
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account

        User user = new User(addRequest.username(),
                addRequest.email(),
                encoder.encode(addRequest.password()));

        Set<String> strRoles = addRequest.roles();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepo.save(user);
        return userRepo.findByUsername(addRequest.username());
    }
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public void deleteUsers(List<Long> userIds) {
        userRepo.deleteAllById(userIds);
    }
}
