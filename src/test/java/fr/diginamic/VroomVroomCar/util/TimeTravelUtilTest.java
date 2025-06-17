package fr.diginamic.VroomVroomCar.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeTravelUtilTest {
    private final TimeTravelUtil util = new TimeTravelUtil();

    @Test
    public void testOnlyMinutes() {
        String result = util.formatDuration(300); // 5 minutes
        assertEquals("5min", result);
    }

    @Test
    public void testExactHour() {
        String result = util.formatDuration(3600); // 1 hour
        assertEquals("1h", result);
    }

    @Test
    public void testHourAndMinutes() {
        String result = util.formatDuration(5400); // 1h 30min
        assertEquals("1h 30min", result);
    }


    @Test
    public void testRoundedUp() {
        String result = util.formatDuration(89); // ~1.48 minutes → round to 1
        assertEquals("1min", result);
    }

    @Test
    public void testRoundedDown() {
        String result = util.formatDuration(119); // ~1.98 minutes → round to 2
        assertEquals("2min", result);
    }

    @Test
    public void testMultipleHours() {
        String result = util.formatDuration(3 * 3600 + 45 * 60); // 3h 45min
        assertEquals("3h 45min", result);
    }
}
