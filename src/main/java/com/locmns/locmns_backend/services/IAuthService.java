package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface IAuthService {

    ResponseEntity<String> login(LoginRequestDto request);
}
