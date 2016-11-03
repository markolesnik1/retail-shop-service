package com.retail.service;

import com.google.maps.model.LatLng;
import com.retail.domain.ShopAddress;

public interface GeocodeService {
    LatLng lookup(ShopAddress shopAddress) throws Exception;
}
