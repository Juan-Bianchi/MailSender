package com.sirius.mailSender.services.implementations;

import com.sirius.mailSender.dtos.LoginDTO;
import com.sirius.mailSender.dtos.RegisterDTO;
import com.sirius.mailSender.models.Role;
import com.sirius.mailSender.models.UserEntity;
import com.sirius.mailSender.repositories.RoleRepository;
import com.sirius.mailSender.repositories.UserEntityRepository;
import com.sirius.mailSender.security.JWTGenerator;
import com.sirius.mailSender.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImplementation implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    public AuthServiceImplementation(AuthenticationManager authenticationManager, UserEntityRepository userEntityRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userEntityRepository = userEntityRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }


    @Override
    public void register(RegisterDTO registerDTO) {
        if(thereIsANullField(registerDTO.getUserName(), registerDTO.getEmail(), registerDTO.getPassword())) {
            throw new RuntimeException(verifyNullFields(registerDTO.getUserName(), registerDTO.getEmail(), registerDTO.getPassword()));
        }
        if( !emailIsCorrect(registerDTO.getEmail()) ) {
            throw new RuntimeException("The email given is not correct. Please, provide a different one");
        }
        if( !isValidPassword(registerDTO.getPassword()) ) {
            throw new RuntimeException("Password requires 1 uppercase, 1 lowercase, 1 digit, 1 special character, min. 8 characters.");
        }
        if (this.userEntityRepository.existsByUserName(registerDTO.getUserName())) {
            throw new RuntimeException("User is already in use. Register with another user name.");
        }

        UserEntity user = new UserEntity(registerDTO.getEmail(), registerDTO.getUserName(), passwordEncoder.encode(registerDTO.getPassword()));

        Role roles = registerDTO.getEmail().contains("admin") && registerDTO.getUserName().contains("admin")
                ? roleRepository.findByName("ADMIN").get()
                : roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        this.userEntityRepository.save(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtGenerator.generateToken(authentication);
    }


    //AUXILIARY METHODS
    private boolean thereIsANullField(String userName, String email, String password){
        return userName.isEmpty()  || email.isEmpty() || password.isEmpty();
    }

    private String verifyNullFields(String userName, String email, String password){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("⋆ Please, fill the following fields: ");
        boolean moreThanOne = false;

        if (userName.isEmpty()){
            stringBuilder.append("user name");
            moreThanOne = true;
        }
        if (email.isEmpty()){
            if(moreThanOne){
                stringBuilder.append(", ");
            }
            else {
                moreThanOne = true;
            }
            stringBuilder.append("email");
        }
        if (password.isEmpty()){
            if(moreThanOne) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("password");
        }
        stringBuilder.append(".");

        return stringBuilder.toString();
    }

    /*validates that a given text adheres to the valid format of an email address, ensuring it contains a valid
     username, "@" symbol, a valid domain, and a valid domain extension with at least two alphabetical characters.
     */
    private boolean emailIsCorrect(String email) {
        String emailRegexPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegexPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /*enforces the following validations for a password:
        It must contain at least one lowercase letter (a-z).
        It must contain at least one uppercase letter (A-Z).
        It must contain at least one digit (0-9).
        It must contain at least one special character from the set [@$!%*?&].
        It must consist of a combination of uppercase letters, lowercase letters, digits, and the specified special characters.
        It must be at least 8 characters in length.*/
    public static boolean isValidPassword(String password) {
        String passwordRegexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegexPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
