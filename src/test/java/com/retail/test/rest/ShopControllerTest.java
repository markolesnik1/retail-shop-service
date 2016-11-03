package com.retail.test.rest;

import com.retail.RetailShopServiceApplication;
import com.retail.api.ShopApi;
import com.retail.domain.Shop;
import com.retail.rest.ShopController;
import com.retail.service.ShopService;
import com.retail.test.common.ShopDataProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@SpringBootTest(classes = { RetailShopServiceApplication.class })
@ActiveProfiles("devmock")
@Test(groups = "controller", description = "Shop controller")
public class ShopControllerTest extends AbstractTestNGSpringContextTests {
    private ShopApi controller;
    private ShopService service;

    @BeforeMethod
    public void beforeScenario() {
        service = mock(ShopService.class);
        controller = new ShopController(service);
    }

    @Test(description = "Add shop", dataProviderClass = ShopDataProvider.class, dataProvider = "shopData")
    public void addShopTest(String name, String number, String postCode, Double lat, Double lng) {
        Shop shop = ShopDataProvider.buildShop(name, number, postCode, lat, lng);

        when(service.add(shop)).thenReturn(shop);

        ResponseEntity<Shop> response = controller.add(shop);
        verify(service).add(shop);
        assertNotNull(response);

        HttpStatus status = response.getStatusCode();
        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.CREATED, status);
        assertNotNull(headers.getLocation());
    }

    @Test(description = "Find nearest shop", dataProviderClass = ShopDataProvider.class, dataProvider = "shopData")
    public void findNearestShopTest(String name, String number, String postCode, Double lat, Double lng) {
        Shop shop = ShopDataProvider.buildShop(name, number, postCode, lat, lng);

        when(service.getNearest(lng, lat)).thenReturn(shop);

        ResponseEntity<Shop> response = controller.getNearest(lng, lat);
        verify(service).getNearest(lng, lat);
        assertNotNull(response);

        HttpStatus status = response.getStatusCode();
        assertEquals(HttpStatus.OK, status);

        Shop nearestShop = response.getBody();
        assertNotNull(nearestShop);
        assertEquals(shop.getShopName(), nearestShop.getShopName());
    }
}
