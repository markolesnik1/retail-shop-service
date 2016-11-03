package com.retail.test.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.retail.RetailShopServiceApplication;
import com.retail.domain.Shop;
import com.retail.domain.ShopAddress;
import com.retail.service.GeocodeService;
import com.retail.service.GeocodeServiceImpl;
import com.retail.test.common.ShopDataProvider;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

// for Geocode service unit test example see:
// http://stackoverflow.com/questions/35641902/how-can-i-mock-googles-geocoding-api-request-using-mockito-powermock

@SpringBootTest(classes = { RetailShopServiceApplication.class })
@ActiveProfiles("devmock")
@Test(groups = "service", description = "Geocode Service")
@PrepareForTest({ GeocodingApi.class, GeocodingApiRequest.class })
public class GeocodeServiceTest extends PowerMockTestCase {
    @Mock
    private GeoApiContext geoApiContext;

    private GeocodeService service;
    private final String apiKey = "123";
    private final String defaultCountry = "UK";

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new PowerMockObjectFactory();
    }

    @BeforeMethod
    public void beforeScenario(){
        service = new GeocodeServiceImpl(geoApiContext, apiKey, defaultCountry);
    }

    @Test(description = "Lookup shop geo data", dataProviderClass = ShopDataProvider.class, dataProvider = "shopData")
    public void lookupTest(String name, String number, String postCode, Double lat, Double lng) throws Exception {
        Shop shop = ShopDataProvider.buildShop(name, number, postCode, null, null);
        ShopAddress shopAddress = shop.getShopAddress();

        LatLng latLng = new LatLng(lat, lng);
        GeocodingResult geocodingResult = new GeocodingResult();
        Geometry geometry = new Geometry();
        geometry.location = latLng;
        geocodingResult.geometry = geometry;
        GeocodingResult[] geocodingResults = new GeocodingResult[] { geocodingResult };

        GeocodingApiRequest geocodingApiRequest = PowerMockito.mock(GeocodingApiRequest.class);
        when(geocodingApiRequest.address(any())).thenReturn(geocodingApiRequest);
        when(geocodingApiRequest.components(any(), any())).thenReturn(geocodingApiRequest);
        when(geocodingApiRequest.await()).thenReturn(geocodingResults);

        mockStatic(GeocodingApi.class);
        when(GeocodingApi.newRequest(eq(geoApiContext))).thenReturn(geocodingApiRequest);

        LatLng returnedLatLng = service.lookup(shopAddress);
        verifyStatic(times(1));

        assertNotNull(returnedLatLng);
        assertEquals(lat, returnedLatLng.lat);
        assertEquals(lng, returnedLatLng.lng);
    }
}
