package com.starpy.pay;

import com.starpy.pay.gp.GooglePayImpl;

/**
 * Created by Efun on 2017/2/23.
 */

public class IPayFactory {

    public static final int PAY_GOOGLE = 0;

    public static IPay create(int payTpye){
        return new GooglePayImpl();
    }
}
