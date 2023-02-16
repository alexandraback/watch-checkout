package com.example.checkout;

public record WatchCatalogModel(
        String watchId,
        String name,
        int unitPrice,
        int discountQuantity,
        float discountFactor
        ) {}
