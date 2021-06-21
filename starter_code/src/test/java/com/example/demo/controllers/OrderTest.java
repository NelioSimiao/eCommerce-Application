package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OrderTest {
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setPrice(BigDecimal.TEN);

        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();

        user.setId(0);
        user.setUsername("nmuchisse");
        user.setPassword("125Password");

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.TEN);
        user.setCart(cart);


        when(userRepo.findByUsername("nmuchisse")).thenReturn(user);
    }

    @Test
    public void submitOrderInvalidUser() {
        ResponseEntity<UserOrder> response = orderController.submit("invalidUser");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void getOrdersForUserInvalidUser() {
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("invalidUser");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    public void submitOrder() {
        ResponseEntity<UserOrder> response = orderController.submit("nmuchisse");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void getOrdersForUser() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("nmuchisse");
        Assert.assertNotNull(ordersForUser);
        Assert.assertEquals(200, ordersForUser.getStatusCodeValue());
        List<UserOrder> orders = ordersForUser.getBody();
        Assert.assertNotNull(orders);

    }


}
