package com.quadpay.quadpay.Network;

import java.util.ArrayList;

public class WidgetData {

    private ArrayList<FeeTier> feeTiers;

    private String bankPartner;

    public class FeeTier{
        private float feeStartsAt;

        private float totalFeePerOrder;

        public float getFeeStartsAt(){
            return feeStartsAt;
        }

        public float getTotalFeePerOrder(){
            return totalFeePerOrder;
        }
    }

    public ArrayList<FeeTier> getFeeTierList(){
        return feeTiers;
    }

    public String getBankPartner(){
        return bankPartner;
    }

}

