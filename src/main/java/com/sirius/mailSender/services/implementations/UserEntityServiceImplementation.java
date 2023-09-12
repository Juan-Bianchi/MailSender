package com.sirius.mailSender.services.implementations;

import com.sirius.mailSender.dtos.UserEntityDTO;
import com.sirius.mailSender.models.UserEntity;
import com.sirius.mailSender.repositories.UserEntityRepository;
import com.sirius.mailSender.repositories.MailRepository;
import com.sirius.mailSender.services.UserEntityService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserEntityServiceImplementation implements UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final MailRepository mailRepository;

    public UserEntityServiceImplementation(UserEntityRepository userEntityRepository, MailRepository mailRepository) {
        this.userEntityRepository = userEntityRepository;
        this.mailRepository = mailRepository;
    }

    @Override
    public Set<UserEntityDTO> findAll() {
        return userEntityRepository.findAll().stream().map(UserEntityDTO::new).collect(Collectors.toSet());
    }

    @Override
    public void register(String email, String userName, String password) {
        if(thereIsANullField(email, userName, password)) {
            throw new RuntimeException(verifyNullFields(email, userName, password));
        }
        if( !emailIsCorrect(email) ) {
            throw new RuntimeException("The email given is not correct. Please, provide a different one");
        }
        if( !isValidPassword(password) ) {
            throw new RuntimeException("Password requires 1 uppercase, 1 lowercase, 1 digit, 1 special character, min. 8 characters.");
        }
        if (this.findUserEntityByUserName(email) !=  null) {
            throw new RuntimeException("Email is already in use. Register with another email.");
        }

        this.userEntityRepository.save(new UserEntity(email, userName, password));
    }

    @Override
    public UserEntity findUserEntityByUserName(String email) {
        return userEntityRepository.findByUserName(email).orElse(null);
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userEntityRepository.existsByUserName(userName);
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
            stringBuilder.append("first name");
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
