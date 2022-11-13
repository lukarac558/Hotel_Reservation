package com.example.db.utils;

import com.example.db.model.City;
import com.example.db.model.Country;
import com.example.db.model.Food;

import java.util.List;
import java.util.stream.Collectors;

public class QueryHelper {

    public static String preparePartialQuery(List<String> list, String tableName) {
        StringBuilder query = new StringBuilder();
        if (list.size() > 0) {
            query.append("(");
            for (Object item : list) {
                query.append(tableName).append(" LIKE '").append(item.toString()).append("' OR ");
            }

            query = new StringBuilder(query.substring(0, query.length() - 3));
            query.append(')');
        }
        return query.toString();
    }

    public static String prepareFinalQuery(String query, String countryQuery, String cityQuery, String foodQuery) {
        if (!countryQuery.equals(""))
            query += countryQuery;

        if (!cityQuery.equals("") && !countryQuery.equals(""))
            query += " AND " + cityQuery;
        else if (countryQuery.equals("") && !cityQuery.equals(""))
            query += cityQuery;

        if ((!countryQuery.equals("") || !cityQuery.equals("")) && !foodQuery.equals(""))
            query += " AND " + foodQuery;
        else if (countryQuery.equals("") && cityQuery.equals("") && !foodQuery.equals(""))
            query += foodQuery;

        return query;
    }

    public static List<String> countriesToStringList(List<Country> list) {
        return list.stream().map(Country::getName).collect(Collectors.toList());
    }

    public static List<String> citiesToStringList(List<City> list) {
        return list.stream().map(City::getName).collect(Collectors.toList());
    }

    public static List<String> foodTypesToStringList(List<Food> list) {
        return list.stream().map(Food::getType).collect(Collectors.toList());
    }
}
