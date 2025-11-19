package com.nn.spring_simple_e_commerce_rest_api.user;

import com.nn.spring_simple_e_commerce_rest_api.shared.config.JwtAuthenticationFilter;
import com.nn.spring_simple_e_commerce_rest_api.shared.config.SecurityConfig;
import com.nn.spring_simple_e_commerce_rest_api.shared.service.JwtService;
import com.nn.spring_simple_e_commerce_rest_api.user.api.request.RegisterRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.config.AuthConfig;
import com.nn.spring_simple_e_commerce_rest_api.user.controller.UserController;
import com.nn.spring_simple_e_commerce_rest_api.user.repository.UserRepository;
import com.nn.spring_simple_e_commerce_rest_api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({AuthConfig.class, JwtAuthenticationFilter.class, SecurityConfig.class})
public class UserControllerTest {
    @Autowired
    MockMvc mvc;
    @MockitoBean
    UserService userService;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    JwtService jwtService;

    @Test
    public void registerUserShouldWork() throws Exception {
        mvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "alice",
                          "password": "password"
                        }
                        """))
                .andExpect(status().isCreated());

        verify(userService).register(new RegisterRequest("alice", "password"));
    }
}
