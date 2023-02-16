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

    public List<WatchCatalogModel> getWatchCatalog(){
        return this.checkoutRepository.getCatalog();
    }

    public BigDecimal calculateTotalPrice(List<String> watchIds) {
        List<WatchCatalogModel> matchingWatches = this.checkoutRepository.retriveWatches(watchIds);
        Map<String, Integer> aggrigatedWatches = aggregateWatchesById(watchIds);
        return getTotalPrice(aggrigatedWatches, matchingWatches);
    }

    private Map<String, Integer> aggregateWatchesById(List<String> watchIds) {
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

    private BigDecimal getTotalPrice(Map<String, Integer> watchIdToQuantity, List<WatchCatalogModel> watchCatalogModel) {
        return watchCatalogModel.stream()
                .filter(watch -> watchIdToQuantity.containsKey(watch.watchId()))
                .map(watch -> {
                    int quantity = watchIdToQuantity.get(watch.watchId());
                    int amountOfDiscountPackages = quantity / watch.discountQuantity();
                    int unDiscountedWatches = quantity % watch.discountQuantity();
                    if (quantity == 1) {
                        unDiscountedWatches = 1;
                    }
                    BigDecimal unDiscountedPrice = BigDecimal.valueOf(unDiscountedWatches).multiply(BigDecimal.valueOf(watch.unitPrice()));
                    BigDecimal discountedPrice = BigDecimal.valueOf(amountOfDiscountPackages)
                            .multiply(BigDecimal.valueOf(watch.unitPrice()).multiply(BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(watch.discountFactor())))
                                            .multiply(BigDecimal.valueOf(watch.discountQuantity())));
                    return unDiscountedPrice.add(discountedPrice);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
