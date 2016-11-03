package com.retail.service;

import com.retail.domain.Shop;

public interface ShopService {
    Shop add(Shop shop);
    Shop getNearest(Double lng, Double lat);
}
