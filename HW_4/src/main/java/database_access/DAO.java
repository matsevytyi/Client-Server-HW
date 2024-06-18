package database_access;

import entities.business_logic.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO {

    public static String createItem(String name, int price) {
        return "INSERT INTO public.items (name, price) VALUES ('" + name + "', '" + price + "')";
    }

    public static String deleteItem(String name) {
        return "DELETE FROM public.items WHERE name = '" + name + "'";
    }

    public static String readItem(String name) {
        return "SELECT * FROM public.items WHERE name = '" + name + "'";
    }

    public static String updateItem(String name, String newName, int newPrice) {
        return "UPDATE public.items SET name = '" + newName + "', price = '" + newPrice + "' WHERE name = '" + name + "'";
    }

    public static String filterByPrice(int minPrice, int maxPrice) {
        return "SELECT * FROM public.items WHERE price > '" + minPrice + "' AND price < '" + maxPrice + "'";
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
