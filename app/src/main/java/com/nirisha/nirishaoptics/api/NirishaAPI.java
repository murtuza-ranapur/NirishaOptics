package com.nirisha.nirishaoptics.api;

/**
 * Created by hp on 09-08-2017.
 */

public enum NirishaAPI {

    API_REGISTER("https://nirisha1q2w3e.000webhostapp.com/CoreService.php?key=register"),
    API_LOGIN("https://nirisha1q2w3e.000webhostapp.com/CoreService.php?key=login");

    private String text;
    private NirishaAPI(String text){this.text=text;}
    public String getText(){return text;}
}
