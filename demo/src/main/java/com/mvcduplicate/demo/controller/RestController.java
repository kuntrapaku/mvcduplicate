package com.mvcduplicate.demo.controller;

import com.mvcduplicate.demo.dto.ApiResponse;
import com.mvcduplicate.demo.model.User;
import com.mvcduplicate.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
public class RestController {

@Autowired
private AuthService authService;
@PostMapping("/auth/register")
public ResponseEntity<?> registerUser(@RequestBody User user)
{
    try{
        String message= authService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }catch (Exception e)
    {
    return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));

    }

}
}
