package Database;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import Classess.CartItem;
import Classess.City;
import Classess.Country;
import Classess.Food;
import Classess.Hotel;
import Classess.HotelName;
import Classess.Offer;
import Classess.Order;
import Classess.User;

public class Database {

    private static ConnectionHelper connectionHelper = new ConnectionHelper();
    public static int userId = -1;
    public static String permission;

    private Database() {
    }

    public static void register(String login, String password, String email, int phoneNumber) throws SQLException {

        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "INSERT INTO [user](login,password,email,phone_number) VALUES(?,?,?,?);";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setInt(4, phoneNumber);

            ps.executeUpdate();
        }
    }

    public static void login(String login, String password) {

        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "SELECT user_id,login,permission FROM [user] WHERE login=? AND password=?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, login);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("user_id");
                    permission = rs.getString("permission");
                }

            } catch (SQLException sqlE) {
                // Podano błedne dane
                Log.d("Login error", sqlE.getMessage());
            }
        }
    }

    public static void logOut() {
        userId = -1;
        permission = "";
    }

    public static void addCity(String cityName) {
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "INSERT INTO city(name) VALUES(?);";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, cityName);

                ps.executeUpdate();
                Log.d("City added", "Pomyślnie dodano miasto");

            } catch (SQLException sqlE) {
                // Miasto musi być unikatowe
                Log.d("Add city error", sqlE.getMessage());
            }
        }
    }

    // przed wywołaniem tej metody musi wyskoczyć warning, że zostaną usunięte wszystkie rekordy powiązane z tym miastem (wymagane potwierdzenie)
    public static void deleteCity(String cityName) {
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "DELETE FROM city WHERE name = ?;";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, cityName);

                int result = ps.executeUpdate();
                if (result > 0)
                    Log.d("City deleted", "Pomyślnie usunięto miasto wraz z powiązaniami");
                else
                    Log.d("City doesn't exist", "Wprowadzone miasto nie istnieje w bazie");

            } catch (SQLException sqlE) {
                Log.d("Delete city error", sqlE.getMessage());
            }
        }
    }

    public static List<City> getCities() {
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "SELECT city_id,name FROM city;";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                List cityList = new ArrayList();

                while (rs.next()) {
                    int cityId = rs.getInt("city_id");
                    String name = rs.getString("name");

                    cityList.add(new City(cityId, name));
                }

                return cityList;

            } catch (SQLException sqlE) {
                Log.d("Get cities error", sqlE.getMessage());
            }
        }
        return null;
    }

    public static void addCountry(String countryName) {
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "INSERT INTO country(name) VALUES(?);";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, countryName);

                ps.executeUpdate();
                Log.d("Country added", "Pomyślnie dodano państwo");

            } catch (SQLException sqlE) {
                // Miasto musi być unikatowe
                Log.d("Add country error", sqlE.getMessage());
            }
        }
    }

    public static void deleteCountry(String countryName) {
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "DELETE FROM country WHERE name = ?;";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, countryName);

                int result = ps.executeUpdate();
                if (result > 0)
                    Log.d("Country deleted", "Pomyślnie usunięto państwo wraz z powiązaniami");
                else
                    Log.d("Country doesn't exist", "Wprowadzone państwo nie istnieje w bazie");

            } catch (SQLException sqlE) {
                Log.d("Delete city error", sqlE.getMessage());
            }
        }
    }

    public static List<Country> getCountries() {
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "SELECT country_id,name FROM country;";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                List countryList = new ArrayList();

                while (rs.next()) {
                    int countryId = rs.getInt("country_id");
                    String name = rs.getString("name");

                    countryList.add(new Country(countryId, name));
                }

                return countryList;

            } catch (SQLException sqlE) {
                Log.d("Get countries error", sqlE.getMessage());
            }
        }
        return null;
    }

    public static void addFoodType(String foodType) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "INSERT INTO food(type) VALUES(?);";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, foodType);

                    ps.executeUpdate();
                    Log.d("Food type added", "Pomyślnie dodano typ wyżywienia");

                } catch (SQLException sqlE) {
                    Log.d("Add food type error", sqlE.getMessage());
                }
            }
    }

    public static void deleteFoodType(String foodType) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "DELETE FROM food WHERE type = ?;";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, foodType);

                    int result = ps.executeUpdate();
                    if (result > 0)
                        Log.d("Food type deleted", "Pomyślnie usunięto typ wyżywienia wraz z powiązaniami");
                    else
                        Log.d("Food type doesn't exist", "Wprowadzony typ wyżywienia nie istnieje w bazie");

                } catch (SQLException sqlE) {
                    Log.d("Delete food type error", sqlE.getMessage());
                }
            }
    }

    public static List<Food> getFoodTypes() {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "SELECT food_id,type FROM food;";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    List foodList = new ArrayList();

                    while (rs.next()) {
                        int foodId = rs.getInt("food_id");
                        String type = rs.getString("type");

                        foodList.add(new Food(foodId, type));
                    }

                    return foodList;

                } catch (SQLException sqlE) {
                    Log.d("Get food types error", sqlE.getMessage());
                }
            }
            return null;
    }

    public static void addHotelName(String hotelName) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "INSERT INTO hotel_name(name) VALUES(?);";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, hotelName);

                    ps.executeUpdate();
                    Log.d("Hotel name added", "Pomyślnie dodano nazwę hotelu");

                } catch (SQLException sqlE) {
                    Log.d("Add hotel name error", sqlE.getMessage());
                }
            }
    }

    public static void deleteHotelName(String hotelName) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "DELETE FROM hotel_name WHERE name = ?;";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, hotelName);

                    int result = ps.executeUpdate();
                    if (result > 0)
                        Log.d("Hotel name deleted", "Pomyślnie usunięto nazwę hotelu wraz z powiązaniami");
                    else
                        Log.d("HotelName doesn't exist", "Wprowadzona nazwa hotelu nie istnieje w bazie");

                } catch (SQLException sqlE) {
                    Log.d("Delete food type error", sqlE.getMessage());
                }
            }
    }

    public static List<HotelName> getHotelNames() {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "SELECT hotel_name_id,name FROM hotel_name;";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    List hotelNameList = new ArrayList();

                    while (rs.next()) {
                        int hotelNameId = rs.getInt("hotel_name_id");
                        String name = rs.getString("name");

                        hotelNameList.add(new HotelName(hotelNameId, name));
                    }

                    return hotelNameList;

                } catch (SQLException sqlE) {
                    Log.d("Get hotel name error", sqlE.getMessage());
                }
            }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void makeOrder(Order order) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "INSERT INTO [order](total_cost,hotel_id,people_count,user_id,order_date) VALUES(?,?,?,?,?);";
                try {
                    Date date = (Date) Date.from(order.getOrderDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setDouble(1, order.getTotalCost());
                    ps.setInt(2, order.getHotelId());
                    ps.setShort(3, order.getPeopleCount());
                    ps.setInt(4, order.getUserId());
                    ps.setDate(5, date);

                    ps.executeUpdate();
                    Log.d("Order made", "Pomyślnie złożono zamówienie");

                } catch (SQLException sqlE) {
                    Log.d("Make order error", sqlE.getMessage());
                }
            }
    }

    public static void cancelOrder(int orderId) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "DELETE FROM [order] WHERE order_id = ?;";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, orderId);

                    int result = ps.executeUpdate();
                    if (result > 0)
                        Log.d("Order canceled", "Pomyślnie anulowano zamówienie hotelu wraz z powiązaniami");
                    else
                        Log.d("Order doesn't exist", "Zamówienie o takim id nie istnieje w bazie");

                } catch (SQLException sqlE) {
                    Log.d("Cancel order error", sqlE.getMessage());
                }
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Order> getUserOrders() {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "SELECT order_id,total_cost,hotel_id,people_count,order_date FROM [order] WHERE user_id = ?;";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, userId);
                    ResultSet rs = ps.executeQuery();

                    List orderList = new ArrayList();

                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        double totalCost = rs.getDouble("total_cost");
                        int hotelId = rs.getInt("hotel_id");
                        short peopleCount = rs.getShort("people_count");
                        Date orderDate = rs.getDate("order_date");

                        LocalDate localDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        orderList.add(new Order(orderId, totalCost, peopleCount, hotelId, localDate, userId));
                    }

                    return orderList;

                } catch (SQLException sqlE) {
                    Log.d("Get orders error", sqlE.getMessage());
                }
            }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addOffer(Offer offer) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "INSERT INTO offer(places_number,price,start_date,end_date,hotel_id) VALUES(?,?,?,?,?);";
                try {
                    Date date1 = Date.valueOf(String.valueOf(LocalDate.of(offer.getStartDate().getYear(), offer.getStartDate().getMonthValue(), offer.getStartDate().getDayOfMonth())));
                    Date date2 = Date.valueOf(String.valueOf(LocalDate.of(offer.getEndDate().getYear(), offer.getEndDate().getMonthValue(), offer.getEndDate().getDayOfMonth())));

                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setShort(1, offer.getPlacesNumber());
                    ps.setDouble(2, offer.getPrice());
                    ps.setDate(3, date1);
                    ps.setDate(4, date2);
                    ps.setInt(5, offer.getHotelId());

                    ps.executeUpdate();
                    Log.d("Offer added", "Pomyślnie dodano ofertę");

                } catch (SQLException sqlE) {
                    Log.d("Add offer error", sqlE.getMessage());
                }
            }
    }

    public static void deleteOffer(int offerId) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {
                String query = "DELETE FROM offer WHERE order_id = ?;";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, offerId);

                    int result = ps.executeUpdate();
                    if (result > 0)
                        Log.d("Offer deleted", "Pomyślnie usunięto ofertę wraz z powiązaniami");
                    else
                        Log.d("Offer doesn't exist", "Oferta o takim id nie istnieje w bazie");

                } catch (SQLException sqlE) {
                    Log.d("Offer delete error", sqlE.getMessage());
                }
            }
    }

    private static String prepareQuery(List<String> list, String tableName) {
        String query = "";

        if (list.size() > 0) {
            query += "(";
            for (Object item : list) {
                query += tableName + " LIKE '" + item.toString() + "' OR ";
            }

            query = query.substring(0, query.length() - 3);
            query += ')';
        }
        return query;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Offer> filterOffers(short placesNumber, double minPrice, double maxPrice, LocalDate startDate, LocalDate endDate, List<Country> countryList, List<City> cityList, List<Food> foodList, short minStarCount) {
            Connection connection = connectionHelper.getConnection();

            if (connection != null) {

                String query = "SELECT offer_id,places_number,price,start_date,end_date,h.hotel_id AS hotelId,star_count,description,co.name AS CountryName," +
                        "c.name AS cityName,f.type AS feedType,hn.name AS hotelName FROM offer o INNER JOIN hotel h ON o.hotel_id=h.hotel_id " +
                        "INNER JOIN city c ON c.city_id = h.city_id INNER JOIN country co ON co.country_id = h.country_id INNER JOIN food f ON f.food_id=h.food_id " +
                        "INNER JOIN hotel_name hn ON hn.hotel_name_id = h.name_id WHERE " +
                        "(? <= places_number) AND (price >= ? AND price <= ?) AND (start_date >= ? AND end_date <= ?) AND (? <= star_count) AND ";

                List<String> stringCountryList  = countryList.stream().map(object -> Objects.toString(object, null)).collect(Collectors.toList());
                List<String> stringCityList = cityList.stream().map(object -> Objects.toString(object, null)).collect(Collectors.toList());
                List<String> stringFoodList = foodList.stream().map(object -> Objects.toString(object, null)).collect(Collectors.toList());

                String countryQuery = prepareQuery(stringCountryList, "co.name");
                String cityQuery = prepareQuery(stringCityList, "c.name");
                String foodQuery = prepareQuery(stringFoodList, "f.type");

                if (countryQuery != "")
                    query += countryQuery;

                if (cityQuery != "" && countryQuery != "")
                    query += " AND " + cityQuery;
                else if (cityQuery != "" && countryQuery == "")
                    query += cityQuery;

                if ((countryQuery != "" || cityQuery != "") && foodQuery != "")
                    query += " AND " + foodQuery;
                else if (countryQuery == "" && cityQuery == "" && foodQuery != "")
                    query += foodQuery;


                try {
                    Date date1 = Date.valueOf(String.valueOf(LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth())));
                    Date date2 = Date.valueOf(String.valueOf(LocalDate.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth())));

                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setShort(1, placesNumber);
                    ps.setDouble(2, minPrice);
                    ps.setDouble(3, maxPrice);
                    ps.setDate(4, date1);
                    ps.setDate(5, date2);
                    ps.setShort(6, minStarCount);
                    ResultSet rs = ps.executeQuery();

                    List filteredOffersList = new ArrayList();

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
                        String foodType = rs.getString("feedType");
                        String hotelName = rs.getString("hotelName");

                        LocalDate localDate1 = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate localDate2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        Hotel h = new Hotel(hotelId, new Country(0, countryName), new City(0, cityName), new Food(0, foodType), starCount, description, new HotelName(0, hotelName));

                        filteredOffersList.add(new Offer(offerId, availablePlaces, price, localDate1, localDate2, h));
                    }

                    return filteredOffersList;

                } catch (SQLException sqlE) {
                    Log.d("Get filter error", sqlE.getMessage());
                }
            }
        return null;
    }

    public static void addOfferToCart(int offerId){
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "INSERT INTO cart(offer_id,user_id) VALUES(?,?);";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, offerId);
                ps.setInt(2, userId);

                ps.executeUpdate();
                Log.d("Offer to cart added", "Pomyślnie dodano ofertę do koszyka");

            } catch (SQLException sqlE) {
                Log.d("Add to cart error", sqlE.getMessage());
            }
        }
    }

    public static void deleteFromCart(int id){
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "DELETE FROM cart WHERE id = ?;";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, id);

                int result = ps.executeUpdate();
                if (result > 0)
                    Log.d("Offer from cart deleted", "Pomyślnie usunięto ofertę z koszyka");

            } catch (SQLException sqlE) {
                Log.d("Delete from cart error", sqlE.getMessage());
            }
        }
    }

    public static List<Offer> getOffersFromCart(){
        Connection connection = connectionHelper.getConnection();

        if (connection != null) {
            String query = "SELECT id,offer_id WHERE user_id = ?;";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                List cartList = new ArrayList();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int offerId = rs.getInt("offer_id");

                    cartList.add(new CartItem(id, offerId, userId));
                }

                return cartList;

            } catch (SQLException sqlE) {
                Log.d("Get from cart error", sqlE.getMessage());
            }
        }
        return null;
    }

    // Jeśli użytkownik zalogowany to ulubione są zapisywanie do bazy, jeśli niezalogowany do SharedPreferences
}

