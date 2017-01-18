package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Region;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ape08 on 18/01/2017.
 */
public class WeatherForecasterTest {

    @Test
    public void retrieveForecastNotCached() throws Exception {
        WeatherForecaster forecaster = new WeatherForecaster(new MockForecaster());
        Forecast forecast = forecaster.forecastFor(Region.LONDON, Day.MONDAY);

        Assert.assertEquals(10, forecast.temperature());
        Assert.assertEquals("Test", forecast.summary());
    }
}
