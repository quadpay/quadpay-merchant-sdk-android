package com.quadpay.sdkexample.ui.home;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.widget.Button;
import android.widget.Toast;
import com.quadpay.quadpay.QuadPay;
import com.quadpay.quadpay.QuadPayCheckoutDetails;
import com.quadpay.sdkexample.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    /*private WebView webView;
    private WebViewClient client;
    private final Handler handler = new Handler(this);
    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
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
        details.merchantFeeForPaymentPlan = "1.0";

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("SDKExample", "Starting activity");
                // Do something in response to button click
                QuadPay.startVirtualCheckout(getActivity(), details);
            }
        });

        /*webView = (WebView) root.findViewById(R.id.widget2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOnTouchListener(this);
        client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                handler.sendEmptyMessage(CLICK_ON_URL);

                return false;
            }
        };
        webView.setWebViewClient(client);
        webView.loadUrl("file:///android_asset/index.html");*/

        return root;
    }

    /*@Override
    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_URL) {
            handler.removeMessages(CLICK_ON_WEBVIEW);
            return true;
        }
        if (msg.what == CLICK_ON_WEBVIEW) {
            Toast.makeText(getContext(),
                    "WebView clicked",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }*/
/*    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.widget2 && event.getAction() == MotionEvent.ACTION_DOWN) {
            webView.setBackgroundColor(Color.BLUE);
            webView.setLayoutParams(new ConstraintLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels,850));
            handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 100);
        }
        return false;
    }*/

}