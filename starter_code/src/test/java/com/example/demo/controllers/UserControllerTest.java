package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPassword = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {

        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPassword);
        when(bCryptPassword.encode("testePassWord")).thenReturn("thisIs");
        when(userRepo.findByUsername("user1")).thenReturn(TestUtils.createUser());
        when(userRepo.findById(1L)).thenReturn(Optional.of(TestUtils.createUser()));


    }

    @Test
    public void createUserHappyPath() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("teste");
        createUserRequest.setPassword("testePassWord");
        createUserRequest.setConfirmPassword("testePassWord");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(0, user.getId());
        Assert.assertEquals("teste", user.getUsername());
        Assert.assertEquals("thisIs", user.getPassword());
    }

    @Test
    public void createUserWithSmallLengthPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("teste");
        createUserRequest.setPassword("teste");
        createUserRequest.setConfirmPassword("teste");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }
    @Test
    public  void  findByUserName(){
        User u = userRepo.findByUsername("user1");
        Assert.assertNotNull(u);
        Assert.assertEquals("user1",u.getUsername());
        Cart cart = u.getCart();
        Assert.assertEquals( Optional.of(1L), java.util.Optional.ofNullable(cart.getId()));
        Assert.assertNull(cart.getItems());


    }

    @Test
    public void findUserById(){
        ResponseEntity<User> user = userController.findById(1L);
        Assert.assertNotNull(user);
        Assert.assertEquals(200, user.getStatusCodeValue());
        User u = user.getBody();
        Assert.assertNotNull(u);
        Assert.assertNull(u.getCart().getItems());
        Assert.assertEquals("user1", u.getUsername());
    }

    @Test
    public  void  findByNotExistingUserName(){
        ResponseEntity<User> user2 = userController.findByUserName("user2");
        Assert.assertNotNull(user2);
        Assert.assertEquals(404,user2.getStatusCodeValue());
    }


}
