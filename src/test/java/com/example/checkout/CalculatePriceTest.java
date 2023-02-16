package com.example.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.Assert.assertEquals;


public class CalculatePriceTest {
    private WatchCatalogModel watch1, watch2, watch3;
    @BeforeEach
    public void setUp() {
        watch1 = new WatchCatalogModel(
                "004",
                "Patek Philippe",
                1000,
                1,
                1
                );
        watch2 = new WatchCatalogModel(
                "010",
                "Hublot",
                2000,
                2,
                0.16666667f
        );
        watch3 = new WatchCatalogModel(
                "015",
                "Omega",
                500,
                3,
                0.4f
        );
    }

    @Test
    public void testSingleWatchWithoutDiscount() {
        BigDecimal result = CheckoutService.getTotalPrice(Map.ofEntries(entry(watch1.watchId(), 1)), List.of(watch1));
        assertEquals(result.intValue(), watch1.unitPrice());
    }

    @Test
    public void testDiscountCalculation() {
        BigDecimal result = CheckoutService.getTotalPrice(Map.ofEntries(
                entry(watch1.watchId(), 1), //1000
                entry(watch2.watchId(), 3), //2000 + (2000*2* (1-0.1666667)) = 5333.333
                entry(watch3.watchId(), 3)), //900
                List.of(watch1, watch3, watch2));
        assertEquals(result.round(MathContext.DECIMAL32),  BigDecimal.valueOf(7233.333));
    }
}
