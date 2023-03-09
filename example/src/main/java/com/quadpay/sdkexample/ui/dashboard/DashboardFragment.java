package com.quadpay.sdkexample.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.quadpay.quadpay.QuadPay;
import com.quadpay.quadpay.QuadPayCheckoutDetails;
import com.quadpay.sdkexample.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Button button = root.findViewById(R.id.button);
        final QuadPayCheckoutDetails details = new QuadPayCheckoutDetails();
        details.amount = "100";
        details.customerFirstName = "Quincy";
        details.merchantReference = "customer-order-492101";
        details.customerEmail = "paul.sauer+mobile-sdk-example@quadpay.com";
        details.customerLastName = "Payman";
        details.customerPhoneNumber = "+14076901147";
        details.customerAddressLine1 = "240 Meeker Ave";
        details.customerAddressLine2 = "Apt 35";
        details.customerPostalCode = "11211";
        details.customerCity = "Brooklyn";
        details.customerState = "NY";
        details.customerCountry = "US";

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("SDKExample", "Starting activity");
                // Do something in response to button click
                QuadPay.startCheckout(getActivity(), details);
            }
        });
        return root;
    }
}