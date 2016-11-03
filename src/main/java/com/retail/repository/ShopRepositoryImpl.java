package com.retail.repository;

import com.retail.domain.Shop;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ShopRepositoryImpl implements ShopRepository {
    private List<Shop> shops;

    public ShopRepositoryImpl() {
        shops = new ArrayList<>();
    }

    @Override
    public Shop add(Shop shop) {
        String shopName = shop.getShopName();
        boolean shopExists = shops.stream().anyMatch(x -> x.getShopName().equalsIgnoreCase(shopName));

        if (shopExists) {
            return null;
        }

        shops.add(shop);

        return shop;
    }

    @Override
    public Shop findOneNearest(Double lng, Double lat) {
        if (shops.size() == 0) {
            return null;
        }

        Map<Double, Shop> shopsDistances = shops.stream()
                .collect(Collectors.toMap(
                        x -> Math.sqrt(Math.pow(x.getShopLongitude() - lng, 2) + Math.pow(x.getShopLatitude() - lat, 2)),
                        x -> x));

        if (shopsDistances.size() == 0) {
            return null;
        }

        OptionalDouble minDistanceOptional = shopsDistances.keySet().stream().mapToDouble(x -> x).min();

        if (!minDistanceOptional.isPresent()) {
            return null;
        }

        Double minDistance = minDistanceOptional.getAsDouble();
        Shop nearestShop = shopsDistances.get(minDistance);

        return nearestShop;
    }
}
