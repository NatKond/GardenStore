package de.telran.gardenStore.service.security;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userService.getByEmail(username);//.orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));

        return new User(
                appUser.getEmail(),
                appUser.getPasswordHash(),
                Collections.singleton(new SimpleGrantedAuthority(appUser.getRole().name())));
    }
}
