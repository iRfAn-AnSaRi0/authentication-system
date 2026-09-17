package com.authentication.authentication_system.services.serviceImplement;

import com.authentication.authentication_system.dto.*;
import com.authentication.authentication_system.entity.UserEntity;
import com.authentication.authentication_system.repository.UserRepository;
import com.authentication.authentication_system.security.JwtService;
import com.authentication.authentication_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
   public Response register(RegisterRequest request){

        if(request.getEmail().isEmpty()){
            throw new IllegalArgumentException("Email is required");
        }

        if(request.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password is required");
        }

        if(userRepository.existsByEmail(request.getEmail())){
             throw new IllegalArgumentException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName());
         user.setEmail(request.getEmail());

         String hashPassword = passwordEncoder.encode(request.getPassword());

         user.setPassword(hashPassword);

          userRepository.save(user);

          Response response = new Response();
           response.setMessage("Registration Successful");


          return response;

    }

    @Override
    public AuthTokens login(LoginRequest request){

        if(request.getEmail().isEmpty()){
            throw new IllegalArgumentException("Email is required");
        }
        if(request.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password is required");
        }

           Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());
                    if(user.isEmpty()){
                        throw new IllegalArgumentException("Invalid email or password");
                    }

                    if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
                        throw new IllegalArgumentException("Invalid email or password");
                    }

                    String accessToken = jwtService.generateAccessToken(user.get().getId(), user.get().getEmail());
                    String refreshToken = jwtService.generateRefreshToken(user.get().getId());

        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public UserResponse getMe(UUID id){

        Optional<UserEntity> user = userRepository.findById(id);

        if(user.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }

        UserResponse response = new UserResponse();
            response.setId(user.get().getId());
            response.setName(user.get().getName());
            response.setEmail(user.get().getEmail());


        return response;
    }

    @Override
    public String refreshAccessToken(String refreshToken){
        if(!jwtService.validateToken(refreshToken)){
            throw new IllegalArgumentException("Invalid refresh token");
        }

        UUID userId = jwtService.extractUserId(refreshToken);
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }

        return jwtService.generateAccessToken(
                user.get().getId(),
                user.get().getEmail()
        );
    }

}
