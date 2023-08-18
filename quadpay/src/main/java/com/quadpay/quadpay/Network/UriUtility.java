package com.quadpay.quadpay.Network;

public final class UriUtility {

    private  UriUtility(){}

    public static class Scheme{
        public static final String UriSchemeHttps = "https";

        public static String addIfMissing(String url){
            if(url != null){
                final String httpsScheme = UriSchemeHttps + "://";

                if(!url.startsWith(httpsScheme)){
                    url = httpsScheme + url;
                }
            }
            return url;
        }
    }
}
