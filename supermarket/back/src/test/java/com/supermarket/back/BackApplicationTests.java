package com.supermarket.back;

import com.supermarket.back.entity.Commodity;
import com.supermarket.back.entity.IDClass.CommodityID;
import com.supermarket.back.repository.CommodityRepository;
import com.supermarket.back.service.Impl.AccountServiceImpl;
import com.supermarket.back.service.Impl.CommodityServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootTest
class BackApplicationTests {
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private CommodityRepository commodityRepository;

    public static String round(String value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.toPlainString();
    }

    @Test
    void contextLoads() {
        Commodity commodity;
        commodity = commodityService.findFirstByCid("6928804011142");
        commodity.setBatchNumber("20220202");

        System.out.println(commodity);

    }


}
