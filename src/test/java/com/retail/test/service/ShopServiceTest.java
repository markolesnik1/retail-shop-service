package com.retail.test.service;

import com.google.maps.model.LatLng;
import com.retail.RetailShopServiceApplication;
import com.retail.domain.Shop;
import com.retail.domain.ShopAddress;
import com.retail.repository.ShopRepository;
import com.retail.service.GeocodeService;
import com.retail.service.ShopService;
import com.retail.service.ShopServiceImpl;
import com.retail.test.common.ShopDataProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;

@SpringBootTest(classes = { RetailShopServiceApplication.class })
@ActiveProfiles("devmock")
@Test(groups = "service", description = "Shop Service")
public class ShopServiceTest extends AbstractTestNGSpringContextTests {
    private ShopService service;
    private ShopRepository repository;
    private GeocodeService geocodeService;

    @BeforeMethod
    public void beforeScenario(){
        repository = mock(ShopRepository.class);
        geocodeService = mock(GeocodeService.class);
        service = new ShopServiceImpl(repository, geocodeService);
    }

    @Test(description = "Add shop", dataProviderClass = ShopDataProvider.class, dataProvider = "shopData")
    public void addShopTest(String name, String number, String postCode, Double lat, Double lng) throws Exception {
        Shop shop = ShopDataProvider.buildShop(name, number, postCode, lat, lng);
        ShopAddress shopAddress = shop.getShopAddress();
        LatLng latLng = new LatLng(lat, lng);

        when(geocodeService.lookup(shopAddress)).thenReturn(latLng);
        when(repository.add(shop)).thenReturn(shop);

        Shop addedShop = service.add(shop);

        verify(geocodeService).lookup(shopAddress);
        verify(repository).add(shop);

        assertNotNull(addedShop);
        assertEquals(shop.getShopName(), addedShop.getShopName());
        assertEquals(shop.getShopLatitude(), addedShop.getShopLatitude());
        assertEquals(shop.getShopLongitude(), addedShop.getShopLongitude());

        assertNotNull(addedShop.getShopAddress());
        assertEquals(shop.getShopAddress().getNumber(), addedShop.getShopAddress().getNumber());
        assertEquals(shop.getShopAddress().getPostCode(), addedShop.getShopAddress().getPostCode());
    }

    @Test(description = "Find nearest shop", dataProviderClass = ShopDataProvider.class, dataProvider = "shopData")
    public void findNearestShopTest(String name, String number, String postCode, Double lat, Double lng) {
        Shop shop = ShopDataProvider.buildShop(name, number, postCode, lat, lng);

        when(repository.findOneNearest(lng, lat)).thenReturn(shop);

        Shop nearestShop = service.getNearest(lng, lat);

        verify(repository).findOneNearest(lng, lat);

        assertNotNull(nearestShop);
        assertEquals(shop.getShopName(), nearestShop.getShopName());
    }
}
