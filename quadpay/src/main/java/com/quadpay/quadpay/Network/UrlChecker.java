package com.quadpay.quadpay.Network;

public class UrlChecker {

    private String url;

    public UrlChecker(String url){
        this.url = url;
    }

    public String addHttpsIfNotPresent(){
        if(url !=null){
            if(!url.startsWith("https://")){
                url = "https://" + url;
            }
        }

        return url;
    }
}
