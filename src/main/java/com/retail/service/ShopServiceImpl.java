package com.retail.service;

import com.google.maps.model.LatLng;
import com.retail.domain.Shop;
import com.retail.domain.ShopAddress;
import com.retail.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {
    private static final Logger LOG = LoggerFactory.getLogger(ShopServiceImpl.class);
    private ShopRepository repository;
    private GeocodeService geocodeService;

    @Autowired
    public ShopServiceImpl(ShopRepository repository,
                           GeocodeService geocodeService) {
        this.repository = repository;
        this.geocodeService = geocodeService;
    }

    @Override
    public Shop add(Shop shop) {
        try {
            ShopAddress shopAddress = shop.getShopAddress();
            LatLng latLng = geocodeService.lookup(shopAddress);

            if (latLng == null) {
                return null;
            }

            shop.setShopLatitude(latLng.lat);
            shop.setShopLongitude(latLng.lng);

            Shop addedShop = repository.add(shop);

            return addedShop;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Shop getNearest(Double lng, Double lat) {
        Shop shop = repository.findOneNearest(lng, lat);

        return shop;
    }
}
