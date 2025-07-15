package com.example.weatherapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PincodeTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        Pincode pincode = new Pincode("411014", 18.52, 73.85);

        assertEquals("411014", pincode.getCode());
        assertEquals(18.52, pincode.getLatitude());
        assertEquals(73.85, pincode.getLongitude());
    }

    @Test
    void testSetters() {
        Pincode pincode = new Pincode();
        pincode.setCode("560001");
        pincode.setLatitude(12.97);
        pincode.setLongitude(77.59);

        assertEquals("560001", pincode.getCode());
        assertEquals(12.97, pincode.getLatitude());
        assertEquals(77.59, pincode.getLongitude());
    }

    @Test
    void testNoArgsConstructor() {
        Pincode pincode = new Pincode();
        assertNotNull(pincode);
    }
}
