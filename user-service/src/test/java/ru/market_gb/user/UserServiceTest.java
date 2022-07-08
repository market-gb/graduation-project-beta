package ru.market_gb.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.market_gb.user.entites.Role;
import ru.market_gb.user.entites.User;
import ru.market_gb.user.repositories.UserRepository;
import ru.market_gb.user.services.UserService;

import java.util.*;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final String EMAIL = "test_email";
    private static final String ROLE_NAME = "test_role";
    private static User user;
    private static SimpleGrantedAuthority authority;

    @BeforeAll
    public static void initEntities() {
        Role role = new Role();
        role.setId(1L);
        role.setName(ROLE_NAME);

        user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRoles(List.of(role));

        authority = new SimpleGrantedAuthority(ROLE_NAME);
    }

    @Test
    public void findByUsernameTest() {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findByUsername(USERNAME);
        User foundUser = userService.findByUsername(USERNAME).orElse(new User());
        Assertions.assertEquals(USERNAME, foundUser.getUsername());
        Assertions.assertEquals(PASSWORD, foundUser.getPassword());
        Assertions.assertEquals(EMAIL, foundUser.getEmail());
        Assertions.assertEquals(1, foundUser.getRoles().size());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(USERNAME);
    }

    @Test
    public void loadUserByUsername() {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findByUsername(USERNAME);
        UserDetails userDetails = userService.loadUserByUsername(USERNAME);
        Assertions.assertEquals(USERNAME, userDetails.getUsername());
        Assertions.assertEquals(PASSWORD, userDetails.getPassword());
        Assertions.assertEquals(Set.of(authority), userDetails.getAuthorities());
    }
}
