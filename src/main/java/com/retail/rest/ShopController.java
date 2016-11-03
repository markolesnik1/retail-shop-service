package com.retail.rest;

import com.retail.api.ShopApi;
import com.retail.domain.Shop;
import com.retail.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(
        name = "Shops",
        path = "api/shops")
public class ShopController implements ShopApi {
    private static final Logger LOG = LoggerFactory.getLogger(ShopController.class);
    private ShopService service;

    @Autowired
    public ShopController(ShopService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity add(@RequestBody Shop shop) {
        LOG.info("Request to add shop ID: {}", shop);

        Shop addedShop = service.add(shop);

        if (addedShop == null) {
            LOG.error("Shop with name {} already exists", shop.getShopName());

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        LOG.info("Shop with name {} added", shop.getShopName());

        URI self = linkTo(ShopController.class).slash(shop.getShopName()).toUri();

        return ResponseEntity.created(self).build();
    }

    @Override
    public ResponseEntity<Shop> getNearest(
            @RequestParam(value = "customerLongitude") Double lng,
            @RequestParam(value = "customerLatitude") Double lat) {
        LOG.info("Request to retrieve shop nearest to: lng={}, lat={}", lng, lat);

        Shop shop = service.getNearest(lng, lat);

        if (shop == null) {
            LOG.error("No shops found");

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        LOG.info("Returning nearest shop {}", shop);

        return ResponseEntity.ok(shop);
    }
}
