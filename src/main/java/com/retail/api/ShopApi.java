package com.retail.api;

import com.retail.domain.Shop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "Shop", description = "Manage shops")
public interface ShopApi {
    @RequestMapping(
            path = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add shop")
    ResponseEntity add(@ApiParam(value = "Shop details") Shop shop);

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get nearest shop by longitude and latitude")
    ResponseEntity<Shop> getNearest(
            @ApiParam(value = "Longitude") Double lng,
            @ApiParam(value = "Latitude") Double lat);
}
