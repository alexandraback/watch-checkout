package com.example.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CheckoutRepository {
    private final JdbcTemplate db;

    @Autowired
    public CheckoutRepository(JdbcTemplate db) {
        this.db = db;
    }

    public List<WatchCatalogModel> retriveWatches(List<String> watchIds) {
        List<Integer> idInts = watchIds.stream()
                .map(Integer::parseInt)
                .toList();

        String sql = "SELECT * FROM WATCH_CATALOG WHERE ID IN (%s)";
        String idList = watchIds.stream()
                .map(v ->"?" )
                .collect(Collectors.joining(","));
        sql = String.format(sql, idList);
        return db.query(sql, idInts.toArray(), (rs, row) -> mapToWatchCatalogModel(rs));
    }


    private static WatchCatalogModel mapToWatchCatalogModel(ResultSet rs) throws SQLException {
        return new WatchCatalogModel(
                leftPadWatchId(rs.getString("id")),
                rs.getString("name"),
                rs.getInt("unit_price"),
                rs.getInt("discount_quantity"),
                rs.getFloat("discount_factor")
        );
    }


    private static String leftPadWatchId(String watchId) {
        return String.format("%03d", Integer.parseInt(watchId));
    }
}
