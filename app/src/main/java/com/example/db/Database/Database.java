package com.example.db.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.db.Class.CartItem;
import com.example.db.Class.City;
import com.example.db.Class.Country;
import com.example.db.Class.Food;
import com.example.db.Class.Hotel;
import com.example.db.Class.Offer;
import com.example.db.Class.Order;
import com.example.db.Class.SerializableBitmap;
import com.example.db.Class.User;

public class Database {

    private final static ConnectionHelper connectionHelper = new ConnectionHelper();
    public static int userId = -1;
    public static boolean isAdmin = false;

    private Database() {
    }

    public static void register(String login, String password, String email) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO [user](login,password,email) VALUES(?,?,?);";

            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, email);

            ps.executeUpdate();
        }
    }

    public static void login(String login, String password) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "SELECT user_id,is_admin FROM [user] WHERE login=? AND password=?";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setString(1, login);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("user_id");
                    isAdmin = rs.getBoolean("is_admin");
                }

            } catch (SQLException sqlE) {
                Log.d("Login error", sqlE.getMessage());
            }
        }
    }

    public static void changeUserPermission(int userId, boolean isAdminCurrently) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query;
            if (isAdminCurrently)
                query = "UPDATE [user] SET is_admin = 0 WHERE user_id = ?;";
            else
                query = "UPDATE [user] SET is_admin = 1 WHERE user_id = ?;";

            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, userId);

            ps.executeUpdate();
            Log.d("User made admin", "Pomyślnie zmieniono rolę na administratora.");
        }
    }

    public static boolean possibleToChangePermission(int userId) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();
        boolean possibleToChange = false;

        if (connection.isPresent()) {
            String query = "SELECT COUNT(order_id) FROM [order] WHERE user_id = ?;";

            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) <= 0)
                possibleToChange = true;
        }

        return possibleToChange;
    }

    public static List<User> getAllUsers() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<User> queriedUsers = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT user_id,login,email,is_admin FROM [user];";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String login = rs.getString("login");
                    String email = rs.getString("email");
                    boolean isAdmin = rs.getBoolean("is_admin");
                    queriedUsers.add(new User(userId, login, email, isAdmin));
                }

            } catch (SQLException sqlE) {
                Log.d("Get all countries error", sqlE.getMessage());
            }
        }
        return queriedUsers;
    }

    public static void logOut() {
        userId = -1;
        isAdmin = false;
    }

    public static void addCountry(Country country) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO country(code,name) VALUES(?,?);";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setString(1, country.getCode());
            ps.setString(2, country.getName());

            ps.executeUpdate();
            Log.d("Country added", "Pomyślnie dodano państwo");
        }
    }

    public static List<Country> getAllCountries() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Country> queriedCountries = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT code,name FROM country;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String code = rs.getString("code");
                    String name = rs.getString("name");
                    queriedCountries.add(new Country(code, name));
                }

            } catch (SQLException sqlE) {
                Log.d("Get all countries error", sqlE.getMessage());
            }
        }
        return queriedCountries;
    }

    public static List<Country> getCountriesInOffer() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Country> queriedCountries = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT DISTINCT code,co.name as countryName FROM country co INNER JOIN city c ON co.code = c.country_code;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String code = rs.getString("code");
                    String name = rs.getString("countryName");
                    queriedCountries.add(new Country(code, name));
                }

            } catch (SQLException sqlE) {
                Log.d("Get all countries error", sqlE.getMessage());
            }
        }
        return queriedCountries;
    }

    public static String getCountryCodeByName(String countryName) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "SELECT code FROM country WHERE name = ? ";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setString(1, countryName);
                ResultSet rs = ps.executeQuery();

                if (rs.next())
                    return rs.getString("code");

            } catch (SQLException sqlE) {
                Log.d("Get country code error", sqlE.getMessage());
            }
        }
        return "";
    }

    public static void deleteCountry(String countryCode) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "DELETE FROM country WHERE code = ?;";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setString(1, countryCode);

            int result = ps.executeUpdate();
            if (result > 0)
                Log.d("Country deleted", "Pomyślnie usunięto państwo.");
            else
                Log.d("Country doesn't exist", "Wprowadzone państwo nie istnieje w bazie");
        }
    }

    public static void addCity(City city) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO city(name,country_code) VALUES(?,?);";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setString(1, city.getName());
            ps.setString(2, city.getCountryCode());

            ps.executeUpdate();
            Log.d("City added", "Pomyślnie dodano miasto");
        }
    }

    public static void deleteCity(City city) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "DELETE FROM city WHERE name = ? AND country_code = ?;";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setString(1, city.getName());
            ps.setString(2, city.getCountryCode());

            int result = ps.executeUpdate();
            if (result > 0)
                Log.d("City deleted", "Pomyślnie usunięto miasto wraz z powiązaniami");
            else
                Log.d("City doesn't exist", "Wprowadzone miasto nie istnieje w bazie");
        }
    }

    public static List<City> getAllCities() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<City> queriedCities = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT city_id,name,country_code FROM city;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int cityId = rs.getInt("city_id");
                    String name = rs.getString("name");
                    String countryCode = rs.getString("country_code");
                    queriedCities.add(new City(cityId, name, countryCode));
                }
            } catch (SQLException sqlE) {
                Log.d("Get cities error", sqlE.getMessage());
            }
        }
        return queriedCities;
    }

    public static List<City> getCitiesByCountryCode(String countryCode) {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<City> queriedCities = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT city_id,name FROM city WHERE country_code = ?;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setString(1, countryCode);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int cityId = rs.getInt("city_id");
                    String name = rs.getString("name");
                    queriedCities.add(new City(cityId, name, countryCode));
                }
            } catch (SQLException sqlE) {
                Log.d("Get city by co id error", sqlE.getMessage());
            }
        }
        return queriedCities;
    }

    public static int getCityIdByName(String cityName) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "SELECT city_id FROM city WHERE name = ? ";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setString(1, cityName);
                ResultSet rs = ps.executeQuery();

                if (rs.next())
                    return rs.getShort("city_id");

            } catch (SQLException sqlE) {
                Log.d("Get city id error", sqlE.getMessage());
            }
        }
        return -1;
    }

    public static void addFoodType(String foodType) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO food(type) VALUES(?);";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setString(1, foodType);
                ps.executeUpdate();
                Log.d("Food type added", "Pomyślnie dodano typ wyżywienia");

            } catch (SQLException sqlE) {
                Log.d("Add food type error", sqlE.getMessage());
            }
        }
    }

    public static void deleteFoodType(String foodType) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "DELETE FROM food WHERE type = ?;";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setString(1, foodType);
            int result = ps.executeUpdate();
            if (result > 0)
                Log.d("Food type deleted", "Pomyślnie usunięto typ wyżywienia");
            else
                Log.d("Food type doesn't exist", "Wprowadzony typ wyżywienia nie istnieje w bazie");
        }
    }

    public static List<Food> getFoodTypes() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Food> queriedFoodTypes = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT food_id,type FROM food;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int foodId = rs.getInt("food_id");
                    String type = rs.getString("type");
                    queriedFoodTypes.add(new Food(foodId, type));
                }
            } catch (SQLException sqlE) {
                Log.d("Get food types error", sqlE.getMessage());
            }
        }
        return queriedFoodTypes;
    }

    public static int getFoodIdByType(String foodType) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "SELECT food_id FROM food WHERE type = ? ";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setString(1, foodType);
                ResultSet rs = ps.executeQuery();

                if (rs.next())
                    return rs.getShort("food_id");

            } catch (SQLException sqlE) {
                Log.d("Get food id error", sqlE.getMessage());
            }
        }
        return -1;
    }

//    public static String getFoodTypeById(int foodId) {
//        Optional<Connection> connection = connectionHelper.getConnection();
//
//        if (connection.isPresent()) {
//            String query = "SELECT type FROM food WHERE food_id = ? ";
//            try {
//                PreparedStatement ps = connection.get().prepareStatement(query);
//                ps.setInt(1, foodId);
//                ResultSet rs = ps.executeQuery();
//
//                if (rs.next()) {
//
//                    return rs.getString("type");
//                }
//            } catch (SQLException sqlE) {
//                Log.d("Get food type error", sqlE.getMessage());
//            }
//        }
//        return null;
//    }

    public static void addHotel(Hotel hotel, Bitmap bitmap) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO hotel(city_id,star_count,description,name,image_path) VALUES(?,?,?,?,?);";

            byte[] imageBytes = bitmapToByteArray(bitmap);
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, hotel.getCityId());
            ps.setShort(2, hotel.getStarCount());
            ps.setString(3, hotel.getDescription());
            ps.setString(4, hotel.getName());
            ps.setBytes(5, imageBytes);

            ps.executeUpdate();
            Log.d("Hotel added", "Pomyślnie dodano hotel");
        }
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        return baos.toByteArray();
    }

    public static void updateHotel(int hotelId, short newStarsCount, Bitmap newBitmap, String newDescription) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "UPDATE hotel SET star_count = ?, image_path = ?, description = ? WHERE hotel_id = ?;";
            try {
                byte[] imageBytes = bitmapToByteArray(newBitmap);
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setShort(1, newStarsCount);
                ps.setBytes(2, imageBytes);
                ps.setString(3, newDescription);
                ps.setInt(4, hotelId);

                ps.executeUpdate();
                Log.d("Hotel update made", "Pomyślnie aktualizowano hotel.");

            } catch (SQLException sqlE) {
                Log.d("Update count error", sqlE.getMessage());
            }
        }
    }

    public static void deleteHotel(int hotelId) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "DELETE FROM hotel WHERE hotel_id = ?;";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, hotelId);
            int result = ps.executeUpdate();
            if (result > 0)
                Log.d("Hotel deleted", "Pomyślnie usunięto hotel.");
            else
                Log.d("Hotel doesn't exist", "Zadany hotel nie istnieje w bazie");
        }
    }

    public static List<Hotel> getAllHotels() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Hotel> queriedHotels = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT hotel_id,star_count,description,c.name AS cityName,co.name AS countryName,h.name AS hotelName,image_path " +
                    "FROM hotel h INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.code=c.country_code";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int hotelId = rs.getInt("hotel_id");
                    short starCount = rs.getShort("star_count");
                    String description = rs.getString("description");
                    String cityName = rs.getString("cityName");
                    String countryName = rs.getString("countryName");
                    String hotelName = rs.getString("hotelName");
                    byte[] imageBytes = rs.getBytes("image_path");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    SerializableBitmap serializableBitmap = new SerializableBitmap(bitmap);
                    Hotel hotel = new Hotel(hotelId, new City(cityName, new Country(countryName)),
                            starCount, description, hotelName, serializableBitmap);

                    queriedHotels.add(hotel);
                }
            } catch (SQLException sqlE) {
                Log.d("Get hotels error", sqlE.getMessage());
            }
        }
        return queriedHotels;
    }

    public static int getHotelByParams(String country, String city, String name) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "SELECT hotel_id FROM hotel h INNER JOIN city c ON h.city_id = c.city_id INNER JOIN country co ON c.country_code = co.code " +
                    "WHERE (co.name = ?) AND (c.name = ?) AND (h.name = ?)";

            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setString(1, country);
                ps.setString(2, city);
                ps.setString(3, name);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("hotel_id");
                }

            } catch (SQLException sqlE) {
                Log.d("Get id by params error", sqlE.getMessage());
            }
        }

        return -1;
    }


    public static Hotel getHotelById(int hotelId) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "SELECT star_count,description,c.name AS cityName,co.name AS countryName,h.name AS hotelName,image_path " +
                    "FROM hotel h INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.code=c.country_code WHERE hotel_id = ?;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setInt(1, hotelId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    short starCount = rs.getShort("star_count");
                    String description = rs.getString("description");
                    String cityName = rs.getString("cityName");
                    String countryName = rs.getString("countryName");
                    String hotelName = rs.getString("hotelName");
                    byte[] imageBytes = rs.getBytes("image_path");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    SerializableBitmap serializableBitmap = new SerializableBitmap(bitmap);

                    return new Hotel(hotelId, new City(cityName, new Country(countryName)),
                            starCount, description, hotelName, serializableBitmap);
                }
            } catch (SQLException sqlE) {
                Log.d("Get hotel error", sqlE.getMessage());
            }
        }
        return null;
    }

    public static List<String> getHotelNameByCityId(int cityId) {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<String> queriedHotelNames = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT name from hotel WHERE city_id = ?;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setInt(1, cityId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    queriedHotelNames.add(rs.getString("name"));
                }
            } catch (SQLException sqlE) {
                Log.d("Get hotel name error", sqlE.getMessage());
            }
        }
        return queriedHotelNames;
    }

    public static void makeOrder(Order order) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO [order](total_cost,offer_id,people_count,user_id,order_date) VALUES(?,?,?,?,?);";
            try {
                Date date = Date.valueOf(String.valueOf(LocalDate.of(order.getOrderDate().getYear(), order.getOrderDate().getMonthValue(), order.getOrderDate().getDayOfMonth())));

                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setDouble(1, order.getTotalCost());
                ps.setInt(2, order.getOfferId());
                ps.setShort(3, order.getPeopleCount());
                ps.setInt(4, order.getUserId());
                ps.setDate(5, date);
                ps.executeUpdate();
                Log.d("Order made", "Pomyślnie złożono zamówienie");

                updateOfferCount(order.getOfferId(), order.getPeopleCount());

            } catch (SQLException sqlE) {
                Log.d("Make order error", sqlE.getMessage());
            }
        }
    }

    public static void updateOfferCount(int offerId, short peopleCount) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "UPDATE offer SET places_number = places_number - (?) WHERE offer_id = ?;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setShort(1, peopleCount);
                ps.setInt(2, offerId);
                ps.executeUpdate();
                Log.d("Update made", "Pomyślnie aktualizowano liczbę ofert.");

            } catch (SQLException sqlE) {
                Log.d("Update count error", sqlE.getMessage());
            }
        }
    }

    public static void cancelOrder(int orderId, int offerId, short peopleCount) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "DELETE FROM [order] WHERE order_id = ?;";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, orderId);
            int result = ps.executeUpdate();
            if (result > 0) {
                Log.d("Order canceled", "Pomyślnie anulowano zamówienie.");
                updateOfferCount(offerId, (short) -peopleCount);
            } else
                Log.d("Order doesn't exist", "Zadane zamówienie nie istnieje w bazie");
        }
    }

    public static List<Order> getUserOrders() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Order> queriedOrders = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT order_id,total_cost,offer_id,people_count,order_date FROM [order] WHERE user_id = ?;";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    double totalCost = rs.getDouble("total_cost");
                    int offerId = rs.getInt("offer_id");
                    short peopleCount = rs.getShort("people_count");
                    Date orderDate = rs.getDate("order_date");
                    LocalDate localDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    queriedOrders.add(new Order(orderId, totalCost, peopleCount, offerId, localDate, userId));
                }
            } catch (SQLException sqlE) {
                Log.d("Get orders error", sqlE.getMessage());
            }
        }
        return queriedOrders;
    }

    public static List<Order> gettAllOrders() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Order> queriedOrders = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT order_id,total_cost,offer_id,people_count,order_date FROM [order];";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    double totalCost = rs.getDouble("total_cost");
                    int offerId = rs.getInt("offer_id");
                    short peopleCount = rs.getShort("people_count");
                    Date orderDate = rs.getDate("order_date");
                    LocalDate localDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    queriedOrders.add(new Order(orderId, totalCost, peopleCount, offerId, localDate, userId));
                }
            } catch (SQLException sqlE) {
                Log.d("Get all orders error", sqlE.getMessage());
            }
        }
        return queriedOrders;
    }

    public static void addOffer(Offer offer) {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO offer(places_number,price,start_date,end_date,hotel_id,food_id) VALUES(?,?,?,?,?,?);";
            try {
                Date date1 = Date.valueOf(String.valueOf(LocalDate.of(offer.getStartDate().getYear(), offer.getStartDate().getMonthValue(), offer.getStartDate().getDayOfMonth())));
                Date date2 = Date.valueOf(String.valueOf(LocalDate.of(offer.getEndDate().getYear(), offer.getEndDate().getMonthValue(), offer.getEndDate().getDayOfMonth())));

                double formattedPrice = BigDecimal.valueOf(offer.getPrice())
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setShort(1, offer.getPlacesNumber());
                ps.setDouble(2, formattedPrice);
                ps.setDate(3, date1);
                ps.setDate(4, date2);
                ps.setInt(5, offer.getHotelId());
                ps.setInt(6, offer.getFoodId());
                ps.executeUpdate();
                Log.d("Offer added", "Pomyślnie dodano ofertę");

            } catch (SQLException sqlE) {
                Log.d("Add offer error", sqlE.getMessage());
            }
        }
    }

    public static void deleteOffer(int offerId) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "DELETE FROM offer WHERE offer_id = ?;";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, offerId);
            int result = ps.executeUpdate();
            if (result > 0)
                Log.d("Offer deleted", "Pomyślnie usunięto ofertę.");
            else
                Log.d("Offer doesn't exist", "Zadana oferta nie istnieje w bazie");
        }
    }

    public static List<Offer> getAllOffers() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Offer> queriedOffers = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT offer_id,places_number,price,start_date,end_date,h.hotel_id AS hotelId,star_count,description,co.name AS countryName," +
                    "c.name AS cityName,f.type AS foodType,h.name AS hotelName,image_path FROM offer o INNER JOIN hotel h ON o.hotel_id=h.hotel_id " +
                    "INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.code = c.country_code INNER JOIN food f ON f.food_id = o.food_id";
            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int offerId = rs.getInt("offer_id");
                    short availablePlaces = rs.getShort("places_number");
                    double price = rs.getDouble("price");
                    Date start = rs.getDate("start_date");
                    Date end = rs.getDate("end_date");
                    int hotelId = rs.getInt("hotelId");
                    short starCount = rs.getShort("star_count");
                    String description = rs.getString("description");
                    String countryName = rs.getString("countryName");
                    String cityName = rs.getString("cityName");
                    String foodType = rs.getString("foodType");
                    String hotelName = rs.getString("hotelName");
                    byte[] imageBytes = rs.getBytes("image_path");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    SerializableBitmap serializableBitmap = new SerializableBitmap(bitmap);
                    LocalDate localDate1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate localDate2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    Hotel h = new Hotel(hotelId, new City(cityName, new Country(countryName)),
                            starCount, description, hotelName, serializableBitmap);

                    queriedOffers.add(new Offer(offerId, availablePlaces, price, localDate1, localDate2, h, new Food(foodType)));
                }
            } catch (SQLException sqlE) {
                Log.d("Get offers error", sqlE.getMessage());
            }
        }

        return queriedOffers;
    }

    public static Offer getOfferById(int offerId) {
        Optional<Connection> connection = connectionHelper.getConnection();
        Offer offer = null;

        if (connection.isPresent()) {
            String query = "SELECT offer_id,places_number,price,start_date,end_date,h.hotel_id AS hotelId,star_count,description,co.name AS countryName," +
                    "c.name AS cityName,f.type AS foodType,h.name AS hotelName,image_path FROM offer o INNER JOIN hotel h ON o.hotel_id=h.hotel_id " +
                    "INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.code = c.country_code INNER JOIN food f ON f.food_id = o.food_id WHERE offer_id = ?";

            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setInt(1, offerId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    short availablePlaces = rs.getShort("places_number");
                    double price = rs.getDouble("price");
                    Date start = rs.getDate("start_date");
                    Date end = rs.getDate("end_date");
                    int hotelId = rs.getInt("hotelId");
                    short starCount = rs.getShort("star_count");
                    String description = rs.getString("description");
                    String countryName = rs.getString("countryName");
                    String cityName = rs.getString("cityName");
                    String foodType = rs.getString("foodType");
                    String hotelName = rs.getString("hotelName");
                    byte[] imageBytes = rs.getBytes("image_path");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    SerializableBitmap serializableBitmap = new SerializableBitmap(bitmap);

                    LocalDate localDate1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate localDate2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Hotel h = new Hotel(hotelId, new City(cityName, new Country(countryName)),
                            starCount, description, hotelName, serializableBitmap);

                    offer = new Offer(offerId, availablePlaces, price, localDate1, localDate2, h, new Food(foodType));
                }

            } catch (SQLException sqlE) {
                Log.d("Get offer by id error", sqlE.getMessage());
            }
        }

        return offer;
    }

    // do poprawy
//    public static List<Offer> filterOffers(short placesNumber, double minPrice, double maxPrice, LocalDate startDate, LocalDate endDate, List<Country> countryList, List<City> cityList, List<Food> foodList, short minStarCount) {
//            Optional<Connection> connection = connectionHelper.getConnection();
//
//            if (connection.isPresent()) {
//
//                String query = "SELECT offer_id,places_number,price,start_date,end_date,h.hotel_id AS hotelId,star_count,description,co.name AS CountryName," +
//                        "c.name AS cityName,f.type AS feedType,hn.name AS hotelName,image_path FROM offer o INNER JOIN hotel h ON o.hotel_id=h.hotel_id " +
//                        "INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.country_id = h.country_id INNER JOIN food f ON f.food_id=h.food_id " +
//                        "INNER JOIN hotel_name hn ON hn.hotel_name_id = h.name_id WHERE " +
//                        "(? <= places_number) AND (price >= ? AND price <= ?) AND (start_date >= ? AND end_date <= ?) AND (? <= star_count) AND ";
//
//                List<String> stringCountryList  = countryList.stream().map(Objects::toString).collect(Collectors.toList());
//                List<String> stringCityList = cityList.stream().map(Objects::toString).collect(Collectors.toList());
//                List<String> stringFoodList = foodList.stream().map(Objects::toString).collect(Collectors.toList());
//
//                String countryQuery = prepareQuery(stringCountryList, "co.name");
//                String cityQuery = prepareQuery(stringCityList, "c.name");
//                String foodQuery = prepareQuery(stringFoodList, "f.type");
//
//                if (!countryQuery.equals(""))
//                    query += countryQuery;
//
//                if (!cityQuery.equals("") && !countryQuery.equals(""))
//                    query += " AND " + cityQuery;
//                else if (countryQuery.equals("") && !cityQuery.equals(""))
//                    query += cityQuery;
//
//                if ((!countryQuery.equals("") || !cityQuery.equals("")) && !foodQuery.equals(""))
//                    query += " AND " + foodQuery;
//                else if (countryQuery.equals("") && cityQuery.equals("") && !foodQuery.equals(""))
//                    query += foodQuery;
//
//
//                try {
//                    Date date1 = Date.valueOf(String.valueOf(LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth())));
//                    Date date2 = Date.valueOf(String.valueOf(LocalDate.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth())));
//
//                    PreparedStatement ps = connection.get().prepareStatement(query);
//                    ps.setShort(1, placesNumber);
//                    ps.setDouble(2, minPrice);
//                    ps.setDouble(3, maxPrice);
//                    ps.setDate(4, date1);
//                    ps.setDate(5, date2);
//                    ps.setShort(6, minStarCount);
//                    ResultSet rs = ps.executeQuery();
//
//                    List<Offer> filteredOffersList = new ArrayList<>();
//
//                    while (rs.next()) {
//                        int offerId = rs.getInt("offer_id");
//                        short availablePlaces = rs.getShort("places_number");
//                        double price = rs.getDouble("price");
//                        Date start = rs.getDate("start_date");
//                        Date end = rs.getDate("end_date");
//                        int hotelId = rs.getInt("hotelId");
//                        short starCount = rs.getShort("star_count");
//                        String description = rs.getString("description");
//                        String countryName = rs.getString("countryName");
//                        String cityName = rs.getString("cityName");
//                        String foodType = rs.getString("feedType");
//                        String hotelName = rs.getString("hotelName");
//                        byte[] imageBytes = rs.getBytes("image_path");
//
//                        Bitmap bitmap =  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                        SerializableBitmap serializableBitmap = new SerializableBitmap(bitmap);
//
//                        LocalDate localDate1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                        LocalDate localDate2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//                        Hotel h = new Hotel(hotelId, new Country(countryName), new City(cityName), new Food(foodType), starCount, description, new HotelName(hotelName), serializableBitmap);
//
//                        filteredOffersList.add(new Offer(offerId, availablePlaces, price, localDate1, localDate2, h));
//                    }
//                    return filteredOffersList;
//
//                } catch (SQLException sqlE) {
//                    Log.d("Get filter error", sqlE.getMessage());
//                }
//            }
//        return null;
//    }

    public static List<Integer> filterOffersIds(short placesNumber, double minPrice, double maxPrice, LocalDate startDate, LocalDate endDate, List<Country> countryList, List<City> cityList, List<Food> foodList, short minStarCount) {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<Integer> queriedOffers = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT offer_id FROM offer o INNER JOIN hotel h ON o.hotel_id=h.hotel_id INNER JOIN city c ON c.city_id = h.city_id " +
                    "INNER JOIN country co ON co.code = c.country_code INNER JOIN food f ON f.food_id=o.food_id WHERE " +
                    "(? <= places_number) AND (price >= ? AND price <= ?) AND (start_date >= ? AND end_date <= ?) AND (? <= star_count) AND ";

            String countryQuery = preparePartialQuery(countriesToString(countryList), "co.name");
            String cityQuery = preparePartialQuery(citiesToString(cityList), "c.name");
            String foodQuery = preparePartialQuery(foodToString(foodList), "f.type");
            query = prepareFinalQuery(query, countryQuery, cityQuery, foodQuery);

            try {
                Date date1 = Date.valueOf(String.valueOf(LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth())));
                Date date2 = Date.valueOf(String.valueOf(LocalDate.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth())));

                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setShort(1, placesNumber);
                ps.setDouble(2, minPrice);
                ps.setDouble(3, maxPrice);
                ps.setDate(4, date1);
                ps.setDate(5, date2);
                ps.setShort(6, minStarCount);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {
                    int offerId = rs.getInt("offer_id");
                    queriedOffers.add(offerId);
                }
            } catch (SQLException sqlE) {
                Log.d("Get filter error", sqlE.getMessage());
            }
        }
        return queriedOffers;
    }

    private static String preparePartialQuery(List<String> list, String tableName) {
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

    private static String prepareFinalQuery(String query, String countryQuery, String cityQuery, String foodQuery) {
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

    private static List<String> countriesToString(List<Country> list) {
        return list.stream().map(Country::getName).collect(Collectors.toList());
    }

    private static List<String> citiesToString(List<City> list) {
        return list.stream().map(City::getName).collect(Collectors.toList());
    }

    private static List<String> foodToString(List<Food> list) {
        return list.stream().map(Food::getType).collect(Collectors.toList());
    }

    public static void addOfferToCart(int offerId, short peopleCount) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "INSERT INTO cart(offer_id,user_id,people_count) VALUES(?,?,?);";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, offerId);
            ps.setInt(2, userId);
            ps.setShort(3, peopleCount);
            ps.executeUpdate();
            Log.d("Offer to cart added", "Pomyślnie dodano ofertę do koszyka");
        }
    }

    public static void deleteFromCart(int id) throws SQLException {
        Optional<Connection> connection = connectionHelper.getConnection();

        if (connection.isPresent()) {
            String query = "DELETE FROM cart WHERE offer_id = ? AND user_id = ?;";
            PreparedStatement ps = connection.get().prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, userId);
            int result = ps.executeUpdate();
            if (result > 0)
                Log.d("Offer from cart deleted", "Pomyślnie usunięto ofertę z koszyka");
        }
    }

//    public static List<Offer> getOffersFromCart() {
//        Optional<Connection> connection = connectionHelper.getConnection();
//        List<Offer> queriedOffers = new ArrayList<>();
//
//        if (connection.isPresent()) {
//            String query = "SELECT o.offer_id AS offerId,people_count,places_number,price,start_date,end_date,h.hotel_id AS hotelId,star_count,description,co.name AS countryName," +
//                    "c.name AS cityName,f.type AS foodType,h.name AS hotelName,image_path FROM cart ca INNER JOIN offer o ON ca.offer_id = o.offer_id " +
//                    "INNER JOIN hotel h ON o.hotel_id=h.hotel_id INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.code = c.country_code " +
//                    "INNER JOIN food f ON f.food_id = o.food_id WHERE user_id = ?";
//
//            try {
//                PreparedStatement ps = connection.get().prepareStatement(query);
//                ps.setInt(1, userId);
//                ResultSet rs = ps.executeQuery();
//
//                while (rs.next()) {
//                    int offerId = rs.getInt("offerId");
//                    short peopleCount = rs.getShort("people_count");
//                    short availablePlaces = rs.getShort("places_number");
//                    double price = rs.getDouble("price");
//                    Date start = rs.getDate("start_date");
//                    Date end = rs.getDate("end_date");
//                    int hotelId = rs.getInt("hotelId");
//                    short starCount = rs.getShort("star_count");
//                    String description = rs.getString("description");
//                    String countryName = rs.getString("countryName");
//                    String cityName = rs.getString("cityName");
//                    String foodType = rs.getString("foodType");
//                    String hotelName = rs.getString("hotelName");
//                    byte[] imageBytes = rs.getBytes("image_path");
//
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                    SerializableBitmap serializableBitmap = new SerializableBitmap(bitmap);
//                    LocalDate localDate1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                    LocalDate localDate2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                    Hotel h = new Hotel(hotelId, new City(cityName, new Country(countryName)),
//                            starCount, description, hotelName, serializableBitmap);
//
//                    queriedOffers.add(new Offer(offerId, availablePlaces, price, localDate1, localDate2, h, new Food(foodType), peopleCount));
//                }
//            } catch (SQLException sqlE) {
//                Log.d("Get from cart error", sqlE.getMessage());
//            }
//        }
//        return queriedOffers;
//    }

    public static List<CartItem> getAllFromCart() {
        Optional<Connection> connection = connectionHelper.getConnection();
        List<CartItem> queriedFromCartItem = new ArrayList<>();

        if (connection.isPresent()) {
            String query = "SELECT id,offer_id,people_count FROM cart WHERE user_id = ?";

            try {
                PreparedStatement ps = connection.get().prepareStatement(query);
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int offerId = rs.getInt("offer_id");
                    short peopleCount = rs.getShort("people_count");

                    queriedFromCartItem.add(new CartItem(id, offerId, peopleCount, userId));
                }
            } catch (SQLException sqlE) {
                Log.d("Get from cart error", sqlE.getMessage());
            }
        }
        return queriedFromCartItem;
    }

    // do poprawy
//    public static List<Integer> getOffersIdsFromCart() {
//        Optional<Connection> connection = connectionHelper.getConnection();
//
//        if (connection.isPresent()) {
//            String query = "SELECT o.offer_id AS offerId FROM cart INNER JOIN offer o ON cart.offer_id=o.offer_id INNER JOIN hotel h ON o.hotel_id=h.hotel_id " +
//                    "INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.country_id = h.country_id INNER JOIN food f ON f.food_id=h.food_id " +
//                    "INNER JOIN hotel_name hn ON hn.hotel_name_id = h.name_id WHERE user_id = ?;";
//            try {
//                PreparedStatement ps = connection.get().prepareStatement(query);
//                ps.setInt(1, userId);
//                ResultSet rs = ps.executeQuery();
//
//                List<Integer> offersIdsList = new ArrayList<>();
//
//                while (rs.next()) {
//                    int offerId = rs.getInt("offerId");
//                    offersIdsList.add(offerId);
//                }
//
//                return offersIdsList;
//
//            } catch (SQLException sqlE) {
//                Log.d("Get from cart error", sqlE.getMessage());
//            }
//        }
//        return null;
//    }

}

