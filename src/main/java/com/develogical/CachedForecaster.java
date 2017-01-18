package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Forecaster;
import com.weather.Region;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ape08 on 18/01/2017.
 */
public class CachedForecaster {
    static Integer limit = 10;

    private IForecaster forecaster;
    private List<ForecastStorage> cache = new ArrayList<>();

    public CachedForecaster(IForecaster forecaster) {
        this.forecaster = forecaster;
    }

    public Forecast forecastFor(Region region, Day day){
        Date dateAnHourAgo = new Date(System.currentTimeMillis() - (60 * 60 * 1000));

        for(ForecastStorage forecastStorage : cache) {
            if (forecastStorage.region == region && forecastStorage.day == day) {
                if (forecastStorage.date.before(dateAnHourAgo)) {
                    cache.remove(forecastStorage);
                    break;
                }
                return forecastStorage.forecast;
            }
        }
        // Get from concrete forecaster
        Forecast forecast = forecaster.forecastFor(region, day);
        if (cache.size() >= limit) {
            cache.remove(0);
        }
        cache.add(new ForecastStorage(region, day, forecast));
        return forecast;
    }




    public List<ForecastStorage> getCache() {
        return this.cache;
    }

    public void setSize(int i) {
        limit = i;
    }

    public class ForecastStorage {
        public Forecast forecast;
        public Region region;
        public Day day;

        public Date date;

        public ForecastStorage(Region region, Day day, Forecast forecast) {
            this.region = region;
            this.day = day;
            this.forecast = forecast;

            date = new Date();
        }
    }
}
