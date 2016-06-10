package cn.aigestudio.datepicker.bizs.calendars;

import android.text.TextUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 美国月历
 *
 * Calendar of America
 *
 * @author AigeStudio 2015-07-22
 */
public class DPUSCalendar extends DPCalendar {
    private static final String[][] FESTIVAL_G = {
            {"New Year", "Republic Day"},
            {"St.Valentine's Day"},
            {},
            {"All Fools' Day"},
            {"May Day"},
            {"Environment Day"},
            {""},
            {"Independence Day"},
            {},
            {"Gandhi Jayanthi"},
            {},
            {"Christmas"}};

    private static final int[][] FESTIVAL_G_DATE = {
            {1,26},
            {14},
            {},
            {1},
            {1},
            {6},
            {},
            {15},
            {},
            {2},
            {},
            {25}};
    private static final String[][] HOLIDAY = {{"1", "26"}, {""}, {""}, {""}, {"1"}, {""}, {""}, {"15"}, {""}, {"2"}, {""}, {"25"}};

    @Override
    public String[][] buildMonthFestival(int year, int month) {
        String[][] gregorianMonth = buildMonthG(year, month);
        String tmp[][] = new String[6][7];
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                tmp[i][j] = "";
                String day = gregorianMonth[i][j];
                if (!TextUtils.isEmpty(day)) {
                    tmp[i][j] = getFestivalG(month, Integer.valueOf(day));
                }
            }
        }
        return tmp;
    }

    @Override
    public Set<String> buildMonthHoliday(int year, int month) {
        Set<String> tmp = new HashSet<>();
        if (year == 2015) {
            Collections.addAll(tmp, HOLIDAY[month - 1]);
        }
        return tmp;
    }

    private String getFestivalG(int month, int day) {
        String tmp = "";
        int[] daysInMonth = FESTIVAL_G_DATE[month - 1];
        for (int i = 0; i < daysInMonth.length; i++) {
            if (day == daysInMonth[i]) {
                tmp = FESTIVAL_G[month - 1][i];
            }
        }
        return tmp;
    }
}
