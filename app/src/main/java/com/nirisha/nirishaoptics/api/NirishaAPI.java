package com.nirisha.nirishaoptics.api;

/**
 * Created by hp on 09-08-2017.
 */

public enum NirishaAPI {

    API_REGISTER("https://nirisha1q2w3e.000webhostapp.com/CoreService.php?key=register"),
    API_LOGIN("https://nirisha1q2w3e.000webhostapp.com/CoreService.php?key=login"),
    API_VALUES("http://nirisha1q2w3e.000webhostapp.com/Getters/EnumGetter.php?"),
    API_PRODUCTS("http://nirisha1q2w3e.000webhostapp.com/Getters/ProductGetter.php?"),
    APi_ORDER_GETTER("http://nirisha1q2w3e.000webhostapp.com/Getters/OrderGetter.php?"),
    API_ORDER("https://nirisha1q2w3e.000webhostapp.com/CoreService.php?key=order&");

    private String text;
    private NirishaAPI(String text){this.text=text;}
    public String getText(){return text;}
}
