package com.anqi.distribute.iptest;

import com.anqi.distribute.DistributeToolApplication;
import com.anqi.distribute.iputil.IPZone;
import com.anqi.distribute.iputil.QQWry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = DistributeToolApplication.class)
public class IpTest {

    @Test
    public void testIp() throws IOException {
        QQWry qqWry = new QQWry();
        IPZone ip = qqWry.findIP("218.82.214.115");
        System.out.println(ip.getIp());
        System.out.println(ip.getMainInfo());
        System.out.println(ip.getSubInfo());
    }
}
