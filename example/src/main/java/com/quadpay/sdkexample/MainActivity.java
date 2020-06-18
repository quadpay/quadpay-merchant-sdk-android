package com.quadpay.sdkexample;

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

import com.quadpay.quadpay.QuadPay;
import com.quadpay.quadpay.QuadPayCard;
import com.quadpay.quadpay.QuadPayCardholder;
import com.quadpay.quadpay.QuadPayVirtualCheckoutDelegate;
import com.quadpay.quadpay.QuadPayCheckoutDelegate;

public class MainActivity extends AppCompatActivity implements QuadPayCheckoutDelegate {

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

    // This is what the callback would look like for virtual checkout!
    @Override
    public void checkoutSuccessful(String orderId) {
        Log.d("SDKExample", "QuadPay virtual checkout successful - " + orderId);
        alertTo("QuadPaySDK.checkoutSuccessful: " + orderId);
    }

// This is what the callback would look like for virtual checkout -- not needed for the standard integration!
//    @Override
//    public void checkoutSuccessful(QuadPayCard card, QuadPayCardholder cardholder) {
//        Log.d("SDKExample", "QuadPay virtual checkout successful - " + card.toString() + " for "  + cardholder.toString());
//        alertTo("QuadPaySDK.checkoutSuccessful: " + card.number);
//    }

    @Override
    public void checkoutError(String error) {
        Log.d("SDKExample", "QuadPay checkout encountered an error - " + error);
        alertTo("QuadPaySDK.checkoutError: " + error);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QuadPay.initialize(new QuadPay.Configuration.Builder("44444444-4444-4444-4444-444444444444")
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
