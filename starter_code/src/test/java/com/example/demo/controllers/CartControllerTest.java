package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepo=mock(UserRepository.class);
    private CartRepository cartRepo=mock(CartRepository.class);
    private ItemRepository itemRepo=mock(ItemRepository.class);

    @Before
    public void setUp() {

        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        User user= new User();
        user.setUsername("nmuchisse");
        user.setPassword("PasswordUser2");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart( cart);

        Item item= new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.TEN);
        cart.addItem(item);

        when(userRepo.findByUsername("nmuchisse")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        
    }

    @Test
    public  void  addCartWithInvalidUser(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setUsername("invalidUser");
        ResponseEntity<Cart> cart = cartController.addTocart(request);
        Assert.assertNotNull(cart);
        Assert.assertEquals(404,cart.getStatusCodeValue());

    }

    @Test
    public  void  addCartWithInvalidItemId(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setUsername("nmuchisse");
        ResponseEntity<Cart> cart = cartController.addTocart(request);
        Assert.assertNotNull(cart);
    }

    @Test
    public void addTocartAndRemove() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setUsername("nmuchisse");
        ResponseEntity<Cart> cart = cartController.addTocart(request);
        Assert.assertNotNull(cart);
        Assert.assertEquals("nmuchisse",cart.getBody().getUser().getUsername());
        List<Item> items = cart.getBody().getItems();
        Assert.assertTrue(!items.isEmpty());

        ResponseEntity<Cart> c= cartController.removeFromcart(request);
        Assert.assertNotNull(c);
    }

   @Test
    public void removeFromcartWithIdUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(5L);
        request.setUsername("nmuchisse5");
        ResponseEntity<Cart> cart = cartController.removeFromcart(request);
        Assert.assertNotNull(cart);
        Assert.assertEquals(404,cart.getStatusCodeValue());
    }

    @Test
    public void removeFromcartWithIdItem() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(100L);
        request.setUsername("nmuchisse");
        ResponseEntity<Cart> cart = cartController.removeFromcart(request);
        Assert.assertNotNull(cart);
        Assert.assertEquals(404,cart.getStatusCodeValue());
    }
}
