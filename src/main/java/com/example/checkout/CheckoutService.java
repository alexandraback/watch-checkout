package com.example.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckoutService {
    private final CheckoutRepository checkoutRepository;
    @Autowired
    public CheckoutService(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    public BigDecimal calculateTotalPrice(List<String> watchIds) {
        List<WatchCatalogModel> matchingWatches = this.checkoutRepository.retriveWatches(watchIds);
        Map<String, Integer> aggrigatedWatches = aggregateWatchesById(watchIds);
        return getTotalPrice(aggrigatedWatches, matchingWatches);
    }

    protected static Map<String, Integer> aggregateWatchesById(List<String> watchIds) {
        Map<String, Integer> result = new HashMap<>();
        for (String watchId : watchIds) {
            if (result.containsKey(watchId)) {
                result.put(watchId, result.get(watchId) + 1);
            } else {
                result.put(watchId, 1);
            }
        }
        return result;
    }

    protected static BigDecimal getTotalPrice(Map<String, Integer> aggregatedWatchesById, List<WatchCatalogModel> watchCatalogModel) {
        return watchCatalogModel.stream()
                .filter(watch -> aggregatedWatchesById.containsKey(watch.watchId()))
                .map(watch -> calculatePriceForWatch(aggregatedWatchesById.get(watch.watchId()), watch))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculatePriceForWatch(int quantity, WatchCatalogModel watch) {
        int amountOfDiscountPackages = quantity / watch.discountQuantity();
        int unDiscountedWatches = quantity % watch.discountQuantity();
        if (quantity == 1) {
            unDiscountedWatches = 1;
        }
        BigDecimal unDiscountedPrice = BigDecimal.valueOf(unDiscountedWatches).multiply(BigDecimal.valueOf(watch.unitPrice()));
        BigDecimal discountedUnitPrice = calculateDiscount(watch.unitPrice(), watch.discountFactor());
        BigDecimal discountedPrice = BigDecimal.valueOf(amountOfDiscountPackages)
                .multiply(discountedUnitPrice)
                .multiply(BigDecimal.valueOf(watch.discountQuantity()));
        return unDiscountedPrice.add(discountedPrice);
    }

    protected static BigDecimal calculateDiscount(int unitPrice, float discountFactor) {
        BigDecimal discount = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(discountFactor));
        return BigDecimal.valueOf(unitPrice).multiply(discount);
    }

}
