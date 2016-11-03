package com.retail.test.repository;

import com.retail.RetailShopServiceApplication;
import com.retail.domain.Shop;
import com.retail.repository.ShopRepository;
import com.retail.test.common.ShopDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@SpringBootTest(classes = { RetailShopServiceApplication.class })
@ActiveProfiles("devmock")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Test(groups = "repository", description = "Shop Repository")
public class ShopRepositoryTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ShopRepository repository;

    @Test(description = "Add shop", dataProviderClass = ShopDataProvider.class, dataProvider = "shopData")
    public void addShopTest(String name, String number, String postCode, Double lat, Double lng) {
        Shop shop = ShopDataProvider.buildShop(name, number, postCode, lat, lng);
        Shop addedShop = repository.add(shop);

        assertNotNull(addedShop);
        assertEquals(shop.getShopName(), addedShop.getShopName());
        assertEquals(shop.getShopLatitude(), addedShop.getShopLatitude());
        assertEquals(shop.getShopLongitude(), addedShop.getShopLongitude());

        assertNotNull(addedShop.getShopAddress());
        assertEquals(shop.getShopAddress().getNumber(), addedShop.getShopAddress().getNumber());
        assertEquals(shop.getShopAddress().getPostCode(), addedShop.getShopAddress().getPostCode());
    }

    @Test(description = "Find nearest shop")
    public void findNearestShopTest() {
        List<Shop> shops = ShopDataProvider.generateShops();

        shops.forEach(shop -> repository.add(shop));

        Shop nearestShop = repository.findOneNearest(-31.14, 1.15);

        assertNotNull(nearestShop);
        assertEquals(nearestShop.getShopName(), shops.get(0).getShopName());
    }
}
