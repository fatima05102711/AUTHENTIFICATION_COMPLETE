package com.ogn.orange.domain.Service.Impl;
import com.ogn.orange.domain.Dto.ApiResponse;
import com.ogn.orange.domain.Dto.UserDto;
import com.ogn.orange.domain.Entity.Users;
import com.ogn.orange.domain.Exception.EntityNotFoundException;
import com.ogn.orange.domain.Exception.ErreurCodes;
import com.ogn.orange.domain.Exception.InvalidEntityException;
import com.ogn.orange.domain.Repository.UserRepository;
import com.ogn.orange.security.JwtService;
import com.ogn.orange.domain.Service.UserService;
import com.ogn.orange.utils.Helper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final Helper helper;
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder, Helper helper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.helper = helper;
    }
    @Override
    public List<Users> getAllUsers() {
        List<Users> users = userRepository.findUsersByIsDeletedFalse();
        Collections.reverse(users);
        return  users;
    }
    @Override
    public Optional<Users> getUserById(Long id) throws InvalidEntityException {
        Optional<Users> optionalUsers = userRepository.findById(id);
        if(optionalUsers.isPresent())
        {
           if(!optionalUsers.get().isDeleted())
               return optionalUsers;
        }
        throw  new InvalidEntityException("Cet id n'existe pas",String.valueOf(ErreurCodes.USER_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
    }
    @Override
    public ResponseEntity<ApiResponse<UserDto>> createUsers(UserDto userDto) {
        String email = userDto.getEmail();
        String username = userDto.getUsername();
        if(userRepository.existsByUsername(username))
        {
            throw  new InvalidEntityException("Ce nom utilisateur  existe deja",String.valueOf(ErreurCodes.USER_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
        }
        if (userRepository.existsByEmail(email))

        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    false,
                    null,
                    "L'adresse e-mail est déjà utilisée.",
                    HttpStatus.BAD_REQUEST.value()));
        }
        try {

            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    UserDto.fromEntity(userRepository.save(UserDto.toEntity(userDto))),
                    "User enregistré avec succès",
                    HttpStatus.OK.value()));
        } catch (Exception e) {
            // Gérer l'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    false,
                    null,
                    "Erreur lors de l'enregistrement du user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    @Override
    public ResponseEntity<ApiResponse<UserDto>> updateUsers(UserDto userDto, Long id)
    {
        Optional<Users> optionalUsers = userRepository.findById(id);
        if(optionalUsers.isPresent())
        {
            Users existUser = optionalUsers.get();
            existUser.setLastname(userDto.getLastname());
            existUser.setFirstname(userDto.getFirstname());
            existUser.setUsername(userDto.getUsername());

            existUser.setRole(userDto.getRole());

            existUser.setPhoneNumber(userDto.getPhoneNumber());
            existUser.setEmail(userDto.getEmail());

            existUser.setActiveAccount(userDto.isActiveAccount());
            existUser.setDateModification(LocalDateTime.now());
             userRepository.save(existUser);

            ApiResponse<UserDto> response = new ApiResponse<>(
                    true,
                    UserDto.fromEntity(existUser),
                    "Utilisateur  mis à jour avec succès",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        }
        else
        {
            throw  new InvalidEntityException("Cet id n'existe pas",String.valueOf(ErreurCodes.USER_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
        }
    }
    @Override
    public void deleteUsersById(Long id) throws InvalidEntityException {
        Optional <Users> optionalUsers = userRepository.findById(id);

        if(optionalUsers.isPresent())
        {
            optionalUsers.get().setDeleted(true);
            userRepository.save(optionalUsers.get());
        }
         else
        {
            throw new InvalidEntityException("impossible de supprimer un id qui n'existe pas",String.valueOf(ErreurCodes.USER_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
        }
    }
      @Override
      public ResponseEntity<ApiResponse<UserDto>> authenticate(UserDto userDto) {


        Users users =userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(()  ->new EntityNotFoundException("Cet utilisateur n'existe pas"));

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        users.setPassword(encodedPassword);
        userRepository.save(users);

        if(!helper.ldapAuthenticate(userDto.getUsername(),userDto.getPassword()))
        {
            throw new InvalidEntityException(" Username ou  mot de passe incorrecte ",String.valueOf(ErreurCodes.CLIENT_NOT_FOUND),String.valueOf(HttpStatus.BAD_REQUEST));
        }
         Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

            var token = jwtService.generateToken(users);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        UserDto userDtoResponse = UserDto.builder()
                    .id(users.getId())
                    .username(users.getUsername())
                    .firstname(users.getFirstname())
                    .lastname(users.getLastname())
                    .role(users.getRole())
                    .token(token)
                    .build();

        return ResponseEntity.ok(new ApiResponse<>
                (true,
                        userDtoResponse,
                        " Authentification reussie",
                        HttpStatus.OK.value()));
    }

}
