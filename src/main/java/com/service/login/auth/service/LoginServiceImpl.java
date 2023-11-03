package com.service.login.auth.service;
import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.domain.User;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.exception.InvalidResponseException;
import com.service.login.auth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserRepository userRepository;


    @Override
    public ResponseDTO<User> sinUp(SignUpCO signUpRequest) throws InvalidResponseException {
        ResponseDTO<User> responseDTO = new ResponseDTO<>();
        try {
            Optional<User> optional = userRepository.findByEmail(signUpRequest.email);
            if (optional.isPresent()) {
                responseDTO.setFailureResponse("EMAIL_EXIST");
            } else {
                User user = new User();
                user.setEmail(signUpRequest.getEmail());
                user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
                userRepository.save(user);
                responseDTO.setSuccessResponse(user, "CREATE_SUCCESS");
            }
        } catch (Exception e) {
            responseDTO.setErrorResponse(e, null);
            throw new InvalidResponseException(e.getMessage());
        }
        return responseDTO;
    }
}
