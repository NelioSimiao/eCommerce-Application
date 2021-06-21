package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ItemTest{

        private ItemController itemController;
        private ItemRepository itemRepo = mock(ItemRepository.class);

        @Before
        public void setUp(){
            itemController = new ItemController();
            TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

            Item item = new Item();
            item.setId(5L);
            item.setName("item");
            item.setPrice(BigDecimal.ZERO);
            item.setDescription("item description");


            when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
            when(itemRepo.findById(5L)).thenReturn(java.util.Optional.of(item));
            when(itemRepo.findByName("item")).thenReturn(Collections.singletonList(item));

        }

        @Test
        public void findAllItems() {
            ResponseEntity<List<Item>> response = itemController.getItems();
            Assert.assertNotNull(response);
            Assert.assertEquals(200, response.getStatusCodeValue());
            List<Item> items = response.getBody();
            Assert.assertNotNull(items);
            Assert. assertTrue(!items.isEmpty());
            Assert. assertEquals(1, items.size());
        }

        @Test
        public void findItemById() {
            ResponseEntity<Item> response = itemController.getItemById(5L);
            Assert.assertNotNull(response);
            Assert.assertEquals(200, response.getStatusCodeValue());
            Assert.assertNotNull(response.getBody());
            Assert.assertEquals("item",response.getBody().getName());

        }

        @Test
        public void findItemsByName() {
            ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
            Assert.assertNotNull(response);
            Assert.assertEquals(200, response.getStatusCodeValue());
            Assert.assertNotNull(response.getBody());
            Assert.assertEquals(1, response.getBody().size());
        }
}
