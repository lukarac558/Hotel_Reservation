package com.example.db.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.db.activity.ConfigurationActivity;
import com.example.db.activity.FavouriteOffersActivity;
import com.example.db.activity.HotelsActivity;
import com.example.db.activity.LoginActivity;
import com.example.db.activity.OffersActivity;
import com.example.db.activity.OrdersActivity;
import com.example.db.activity.SearchEngineActivity;
import com.example.db.database.Database;
import com.example.db.R;

public class MenuDirector {
    public static final int hotelMenuOption = R.id.hotelMenuOption;
    public static final int offerMenuOption = R.id.offerMenuOption;
    public static final int adminOrderMenuOption = R.id.adminOrderMenuOption;
    public static final int configurationMenuOption = R.id.configurationMenuOption;
    public static final int adminLogoutMenuOption = R.id.adminLogoutMenuOption;

    public static final int searchEngineMenuOption = R.id.searchEngineMenuOption;
    public static final int favouriteMenuOption = R.id.favouriteMenuOption;
    public static final int userOrderMenuOption = R.id.userOrderMenuOption;
    public static final int loginMenuOption = R.id.loginMenuOption;
    public static final int userLogoutMenuOption = R.id.userLogoutMenuOption;

    private static Intent intent;

    public static boolean setAdminOptionsMenu(AppCompatActivity appCompatActivity, Menu menu) {
        MenuInflater inflater = appCompatActivity.getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    public static boolean setUserOptionsMenu(int userId, AppCompatActivity appCompatActivity, Menu menu) {
        if (userId > 0) {
            return setLoggedOptionsMenu(appCompatActivity, menu);
        } else {
            return setNotLoggedOptionsMenu(appCompatActivity, menu);
        }
    }

    public static boolean setOptionsMenuByPermission(boolean isAdmin, AppCompatActivity appCompatActivity, Menu menu) {
        if (isAdmin) {
            return setAdminOptionsMenu(appCompatActivity, menu);
        } else {
            return setLoggedOptionsMenu(appCompatActivity, menu);
        }
    }

    private static boolean setLoggedOptionsMenu(AppCompatActivity appCompatActivity, Menu menu) {
        MenuInflater inflater = appCompatActivity.getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);

        MenuItem login_item = menu.findItem(R.id.loginMenuOption);
        login_item.setVisible(false);

        return true;
    }

    private static boolean setNotLoggedOptionsMenu(AppCompatActivity appCompatActivity, Menu menu) {
        MenuInflater inflater = appCompatActivity.getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);

        MenuItem logout_item = menu.findItem(R.id.userLogoutMenuOption);
        logout_item.setVisible(false);
        MenuItem showOrders_item = menu.findItem(R.id.userOrderMenuOption);
        showOrders_item.setVisible(false);

        return true;
    }

    public static boolean handleAdminMenuItemSelected(Activity activity, MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case hotelMenuOption:
                intent = new Intent(activity, HotelsActivity.class);
                break;
            case offerMenuOption:
                intent = new Intent(activity, OffersActivity.class);
                break;
            case adminOrderMenuOption:
                intent = new Intent(activity, OrdersActivity.class);
                break;
            case configurationMenuOption:
                intent = new Intent(activity, ConfigurationActivity.class);
                break;
            case adminLogoutMenuOption:
                Database.logOut();
                intent = new Intent(activity, LoginActivity.class);
                break;
        }

        activity.startActivity(intent);
        return true;
    }

    public static boolean handleUserMenuItemSelected(int userId, Activity activity, MenuItem item) {
        if (userId > 0) {
            return handleLoggedMenuItemSelected(activity, item);
        } else {
            return handleNotLoggedMenuItemSelected(activity, item);
        }
    }

    public static boolean handleMenuItemSelectedByPermission(boolean isAdmin,Activity activity, MenuItem item){
        if(isAdmin){
            return handleAdminMenuItemSelected(activity, item);
        }
        else{
            return handleLoggedMenuItemSelected(activity, item);
        }
    }

    private static boolean handleLoggedMenuItemSelected(Activity activity, MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case searchEngineMenuOption:
                intent = new Intent(activity, SearchEngineActivity.class);
                break;
            case favouriteMenuOption:
                intent = new Intent(activity, FavouriteOffersActivity.class);
                break;
            case userOrderMenuOption:
                intent = new Intent(activity, OrdersActivity.class);
                break;
            case userLogoutMenuOption:
                Database.logOut();
                intent = new Intent(activity, LoginActivity.class);
                break;
        }

        activity.startActivity(intent);
        return true;
    }

    private static boolean handleNotLoggedMenuItemSelected(Activity activity, MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case searchEngineMenuOption:
                intent = new Intent(activity, SearchEngineActivity.class);
                break;
            case favouriteMenuOption:
                intent = new Intent(activity, FavouriteOffersActivity.class);
                break;
            case loginMenuOption:
                intent = new Intent(activity, LoginActivity.class);
                break;
        }

        activity.startActivity(intent);
        return true;
    }

}
