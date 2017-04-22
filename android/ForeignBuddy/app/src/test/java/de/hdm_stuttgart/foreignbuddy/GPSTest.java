package de.hdm_stuttgart.foreignbuddy;

import org.junit.Test;

import de.hdm_stuttgart.foreignbuddy.Fragments.MatchesFragment;
import de.hdm_stuttgart.foreignbuddy.UtilityClasses.GPS;

import static org.junit.Assert.assertEquals;

/**
 * Created by Marc-JulianFleck on 22.04.17.
 */

public class GPSTest {

    @Test
    public void correct_distanceCalculation() throws Exception {
        assertEquals(444.69, GPS.distanceInKm(53.86351, 8.65816, 49.86436, 8.65608), 0.01);
        assertEquals(358.00, GPS.distanceInKm(49.86351, 3.65816, 51.86436, 7.65608), 0.01);
    }

}
