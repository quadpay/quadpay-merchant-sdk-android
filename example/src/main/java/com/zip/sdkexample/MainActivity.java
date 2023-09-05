package com.zip.sdkexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.zip.zip.QuadPay;
import com.zip.zip.ZipCustomer;
import com.zip.zip.QuadPayCard;
import com.zip.zip.ZipCardholder;
import com.zip.zip.ZipVirtualCheckoutDelegate;

public class MainActivity extends AppCompatActivity implements ZipVirtualCheckoutDelegate {

    void alertTo(String message) {
        new AlertDialog.Builder(this)
            .setTitle("QuadPaySDK")
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {}
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    @Override
    public void checkoutCancelled(String reason) {
        Log.d("SDKExample", "QuadPay checkout cancelled - " + reason);
        if (reason == null) {
            reason = "(no reason given)";
        }
        alertTo("QuadPaySDK.checkoutCancelled: " + reason);
    }
//
//    // This is what the callback would look like for virtual checkout!
//    @Override
//    public void checkoutSuccessful(String orderId, QuadPayCustomer customer) {
//        Log.d("SDKExample", "QuadPay virtual checkout successful - " + orderId + " customer: " + customer.toString());
//        alertTo("QuadPaySDK.checkoutSuccessful: " + orderId);
//    }

// This is what the callback would look like for virtual checkout -- not needed for the standard integration!
    @Override
    public void checkoutSuccessful(QuadPayCard card, ZipCardholder cardholder, ZipCustomer customer, String orderId) {
        Log.d("SDKExample", "QuadPay virtual checkout successful - " + card.toString() + " for "  + cardholder.toString() + " customer: " + customer.toString() + "for Order Id: " + orderId);
        alertTo("QuadPaySDK.checkoutSuccessful: " + card.number);
    }

    @Override
    public void checkoutError(String error) {
        Log.d("SDKExample", "QuadPay checkout encountered an error - " + error);
        alertTo("QuadPaySDK.checkoutError: " + error);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //dev merchant a3ef4ac2-4b26-46be-b516-2b86f1f0959e
        //sandbox merchant 5898b9a9-46bb-4647-92ed-52643d019d8c
        
        QuadPay.initialize(new QuadPay.Configuration.Builder("a3ef4ac2-4b26-46be-b516-2b86f1f0959e")
                .setEnvironment(QuadPay.Environment.CI)
                .setLocale(QuadPay.Locale.US)
                .build()
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("SDKExample", "onActivityResult - " + requestCode + " " + resultCode);
        if (QuadPay.handleQuadPayActivityResults(
                this,
                requestCode,
                resultCode,
                data
        )) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
