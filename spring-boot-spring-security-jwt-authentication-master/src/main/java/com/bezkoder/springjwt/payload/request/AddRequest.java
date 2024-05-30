package com.bezkoder.springjwt.payload.request;

import com.bezkoder.springjwt.models.Role;

import java.util.HashSet;
import java.util.Set;

public record AddRequest(String username,String email, String password, Set<String> roles) {
}
