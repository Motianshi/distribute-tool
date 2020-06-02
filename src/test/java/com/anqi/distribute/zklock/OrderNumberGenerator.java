package com.anqi.distribute.zklock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumberGenerator {
    private int count = 0;

    public String getNumber() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        return format.format(new Date()) + ++count;
    }


}
