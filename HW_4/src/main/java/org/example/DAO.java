package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO {

    public static void createItem(String name, int price, DBConnection connection) {
        connection.executeQuery("INSERT INTO public.items (name, price) VALUES ('" + name + "', '" + price + "')");
    }

    public static void deleteItem(String name, DBConnection connection) {
        connection.executeQuery("DELETE FROM public.items WHERE name = '" + name + "'");
    }

    public static void readItem(String name, DBConnection connection) {
        connection.executeQuery("SELECT * FROM public.items WHERE name = '" + name + "'");
    }

    public static void updateItem(String name, String newName, int newPrice, DBConnection connection) {
        connection.executeQuery("UPDATE public.items SET name = '" + newName + "', price = '" + newPrice + "' WHERE name = '" + name + "'");
    }

    public static void filterByPrice(int minPrice, int maxPrice, DBConnection connection) {
        connection.executeQuery("SELECT * FROM public.items WHERE price > '" + minPrice + "' AND price < '" + maxPrice + "'");
    }

    public static void processResult(ResultSet resultSet) {

        try {

            if(!resultSet.next()) {
                System.out.println("No results were returned");
                return;
            }

            ArrayList<Item> result = new ArrayList<>();

            while(resultSet.next()) {
                result.add(new Item(resultSet.getString(2), resultSet.getInt(3)));
            }

            //TODO pass the ArrayList to the page with list of items and render the next page
            // now I will just print it

            for (Item item : result) {
                System.out.println("Item received: " + item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
