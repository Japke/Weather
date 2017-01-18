package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Forecaster;
import com.weather.Region;

/**
 * Created by ape08 on 18/01/2017.
 */
public class WeatherForecaster implements IForecaster {

    private Forecaster forecaster;

    public WeatherForecaster(Forecaster forecaster) {
        this.forecaster = forecaster;
    }

    @Override
    public Forecast forecastFor(Region region, Day day) {
        return forecaster.forecastFor(region, day);
    }
}
