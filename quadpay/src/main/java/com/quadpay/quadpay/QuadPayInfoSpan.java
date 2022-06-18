package com.quadpay.quadpay;

import android.content.Context;
import android.content.Intent;
import android.text.style.URLSpan;
import android.view.View;

public final class QuadPayInfoSpan extends URLSpan {

    public QuadPayInfoSpan(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {
        Context context = widget.getContext();
        Intent intent = new Intent(context, ZipWidgetActivity.class);
        String var10001 = this.getURL();

        intent.putExtra("URL", this.getURL());

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            super.onClick(widget);
        }

    }
}