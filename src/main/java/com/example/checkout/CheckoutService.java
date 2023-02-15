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

    private Map<String, Integer> sumWatchesById(List<String> watchIds) {
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

    public int calculateTotalPrice(Map<String, Integer> watchIdToQuantity, List<WatchCatalogModel> watchCatalogModel ) {
        return watchCatalogModel.stream()
                .map(s -> {
                    if (watchIdToQuantity.containsKey(s.watchId())) {
                        int quantity = watchIdToQuantity.get(s.watchId());
                        return 1;
                    } else {
                        return 0;
                    }
                }).reduce(0, Integer::sum);
    }
}
