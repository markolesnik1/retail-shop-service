package com.retail.test.common;

import com.retail.domain.Shop;
import com.retail.domain.ShopAddress;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class ShopDataProvider {
    @DataProvider(name = "shopData")
    public static Object[][] generateShopData() {
        return new Object[][] {
                { "Shop1", "241 Old St", "EC1V 9EY", 1.1, -31.1 },
                { "Shop2", "49 City Rd", "EC1Y 1AU", 1.2, -31.2 },
                { "Shop3", "111 New Bond St", "W1S 1DP", 2.1, -30.1 },
                { "Shop4", "1-3 Brixton Rd", "SW9 6DE", 1.5, -29.5 }
        };
    }

    public static List<Shop> generateShops() {
        Object[][] shopData = generateShopData();
        List<Shop> shops = new ArrayList<>();

        for(Object[] oneShopData : shopData) {
            String name = (String) oneShopData[0];
            String number = (String) oneShopData[1];
            String postCode = (String) oneShopData[2];
            Double lat = (Double) oneShopData[3];
            Double lng = (Double) oneShopData[4];
            Shop shop = buildShop(name, number, postCode, lat, lng);

            shops.add(shop);
        }

        return shops;
    }

    public static Shop buildShop(String name, String number, String postCode, Double lat, Double lng) {
        ShopAddress shopAddress = new ShopAddress();
        shopAddress.setNumber(number);
        shopAddress.setPostCode(postCode);

        Shop shop = new Shop();
        shop.setShopName(name);
        shop.setShopAddress(shopAddress);
        shop.setShopLatitude(lat);
        shop.setShopLongitude(lng);

        return shop;
    }
}
