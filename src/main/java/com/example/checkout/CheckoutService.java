package com.example.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public float calculateTotalPrice(List<String> watchIds) {
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

    private float getTotalPrice(Map<String, Integer> watchIdToQuantity, List<WatchCatalogModel> watchCatalogModel ) {
        return watchCatalogModel.stream()
                .map(s -> {
                    if (watchIdToQuantity.containsKey(s.watchId())) {
                        int quantity = watchIdToQuantity.get(s.watchId());
                        if(quantity == 1) {
                            return (float)s.unitPrice();
                        }
                        int amountOfDiscountPackages = quantity / s.discountQuantity();
                        int unDiscountedWatches = quantity % s.discountQuantity();
                        return (unDiscountedWatches * s.unitPrice()) + (s.unitPrice() * (1 - s.discountFactor()) * amountOfDiscountPackages * s.discountQuantity());
                    } else {
                        return 0.0f;
                    }
                }).reduce(0.0f, Float::sum);
    }
}
