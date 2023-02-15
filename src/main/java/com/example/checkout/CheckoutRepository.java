package com.example.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class CheckoutRepository {
    private final JdbcTemplate db;
    public static final String WATCH_CATALOG_TABLE_NAME = "WATCH_CATALOG";

    @Autowired
    public CheckoutRepository(JdbcTemplate db) {
        this.db = db;
    }

    public List<WatchCatalogModel> retriveWatches(List<String> watchIds){
        String sql = "SELECT * FROM %s WHERE ID IN (?)";
        return Collections.emptyList();
    }
}
