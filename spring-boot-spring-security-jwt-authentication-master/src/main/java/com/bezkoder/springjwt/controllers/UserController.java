package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.payload.request.AddRequest;
import com.bezkoder.springjwt.payload.request.UpdateRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @Operation(security = @SecurityRequirement(name = "projet"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{userId}")
    @Operation(security = @SecurityRequirement(name = "projet"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/update/{userId}")
    @Operation(security = @SecurityRequirement(name = "projet"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long id, @RequestBody UpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateRequest));
    }

    @PostMapping("/add")
    @Operation(security = @SecurityRequirement(name = "projet"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addUser(@RequestBody AddRequest addRequest){
        return  ResponseEntity.ok(userService.addUser(addRequest));
    }
    @DeleteMapping("/delete/{userId}")
    @Operation(security = @SecurityRequirement(name = "projet"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("user deleted successfully!"));

    }

    @DeleteMapping("/delete/userIds")
    @Operation(security = @SecurityRequirement(name = "projet"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteUsers(@RequestParam("ids") List<Long> userIds) {
        userService.deleteUsers(userIds);
        return ResponseEntity.ok(new MessageResponse("users deleted successfully!"));
    }
}
