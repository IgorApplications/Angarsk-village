package com.iapp.angara.util;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.Random;

public class TimeUtilTest {

    @Test
    public void getSeconds() {
        Random random = new Random();
        int testNano = random.nextInt(Integer.MAX_VALUE);
        assertEquals(Math.round(testNano / 1000), TimeUtil.getSeconds(testNano));
    }

    @Test
    public void getMinutes() {
        Random random = new Random();
        int testNano = random.nextInt(Integer.MAX_VALUE);
        assertEquals(Math.round(testNano / 1000 / 60), TimeUtil.getMinutes(testNano));
    }

    @Test
    public void getHours() {
        Random random = new Random();
        int testNano = random.nextInt(Integer.MAX_VALUE);
        assertEquals(Math.round(testNano / 1000 / 60 / 60), TimeUtil.getHours(testNano));
    }

    @Test
    public void getDays() {
        Random random = new Random();
        int testNano = random.nextInt(Integer.MAX_VALUE);
        assertEquals(Math.round(testNano / 1000 / 60 / 60 / 24), TimeUtil.getDays(testNano));
    }

    @Test
    public void getYears() {
        Random random = new Random();
        int testNano = random.nextInt(Integer.MAX_VALUE);
        assertEquals(Math.round(testNano / 1000 / 60 / 60 / 24 / 365), TimeUtil.getYears(testNano));
    }
}