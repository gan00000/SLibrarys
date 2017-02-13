package com.startpy.sdk.out;

/**
 * Created by Efun on 2017/2/13.
 */

public class StarpyFactory {

    public static IStarpy create(){

        return new StarpyImpl();
    }

}
