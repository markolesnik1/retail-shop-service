package com.retail.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.retail.domain.ShopAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class GeocodeServiceImpl implements GeocodeService {
    private static final Logger LOG = LoggerFactory.getLogger(GeocodeServiceImpl.class);
    private GeoApiContext context;
    private String apiKey;
    private String defaultCountry;

    @Autowired
    public GeocodeServiceImpl(
            GeoApiContext context,
            @Value("${application.google.maps.apiKey}") String apiKey,
            @Value("${application.google.maps.defaultCountry}") String defaultCountry) {
        this.context = context;
        this.apiKey = apiKey;
        this.defaultCountry = defaultCountry;
    }

    @Override
    public LatLng lookup(ShopAddress shopAddress) throws Exception {
        LOG.info("Geocode lookup for address {}", shopAddress);

        String address = shopAddress.getNumber();
        String postCode = shopAddress.getPostCode();
        String country = defaultCountry;

        context.setApiKey(apiKey);

        GeocodingApiRequest request = GeocodingApi.newRequest(context)
                .address(address)
                .components(ComponentFilter.postalCode(postCode), ComponentFilter.country(country));

        GeocodingResult[] results = request.await();

        if (results == null || results.length == 0) {
            return null;
        }

        GeocodingResult result = results[0];
        Geometry geometry = result.geometry;

        if (geometry == null) {
            return null;
        }

        LatLng location = geometry.location;

        if (location == null) {
            return null;
        }

        LOG.info("Retrieved location for {}: lat={}, lng={}",
                result.formattedAddress, location.lat, location.lng);

        return location;
    }
}
