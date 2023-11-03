package com.service.login.auth.jwt;
import com.service.login.auth.domain.User;
import com.service.login.auth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        try {
            user =  userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("" + username));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword());
    }

}
