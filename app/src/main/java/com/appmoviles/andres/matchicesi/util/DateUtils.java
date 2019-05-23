package com.appmoviles.andres.matchicesi.util;

import java.util.Calendar;

public class DateUtils {

    public static int age(Calendar birthDate) {
        Calendar actualDate = Calendar.getInstance();
        int years = actualDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int months = actualDate.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
        int days = actualDate.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH);

        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }
        return years;
    }
}
