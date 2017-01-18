package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Forecaster;
import com.weather.Region;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by ape08 on 18/01/2017.
 */
public class CachedForecasterTest {
    private Mockery context = new Mockery();

    @Test
    public void getNotCachedForecast() {
        final IForecaster mockForecaster = context.mock(IForecaster.class);

        CachedForecaster cachedForecaster = new CachedForecaster(mockForecaster);

        final Forecast forecast = new Forecast("test", 15);

        context.checking(new Expectations() {{
            allowing(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
                will(returnValue(forecast));
            exactly(1).of(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
        }});

        Assert.assertEquals(forecast, cachedForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY));
    }

    @Test
    public void getCached() {
        final IForecaster mockForecaster = context.mock(IForecaster.class);

        CachedForecaster cachedForecaster = new CachedForecaster(mockForecaster);

        final Forecast forecast = new Forecast("test", 15);

        context.checking(new Expectations() {{
//            allowing(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
//            will(returnValue(forecast));
            exactly(1).of(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
        }});


        Forecast forecast1 = cachedForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY);

        context.checking(new Expectations() {{
//            allowing(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
//            will(returnValue(forecast));
            exactly(0).of(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
        }});

        Forecast forecast2 = cachedForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY);

        Assert.assertEquals(forecast1, forecast2);
    }

    @Test
    public void removeHourOldForecast() {
        final IForecaster mockForecaster = context.mock(IForecaster.class);

        CachedForecaster cachedForecaster = new CachedForecaster(mockForecaster);

        final Forecast forecast = new Forecast("test", 15);

        context.checking(new Expectations() {{
//            allowing(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
//            will(returnValue(forecast));
            exactly(1).of(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
        }});

        Forecast forecast1 = cachedForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY);

        List<CachedForecaster.ForecastStorage> cache = cachedForecaster.getCache();

        Assert.assertEquals(forecast1, cache.get(0).forecast);

        CachedForecaster.ForecastStorage forecastStorage = cache.get(0);
        forecastStorage.date = new Date(System.currentTimeMillis() - (60 * 60 * 1002));

        context.checking(new Expectations() {{
//            allowing(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
//            will(returnValue(forecast));
            exactly(1).of(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
        }});

        Forecast forecast2 = cachedForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY);
    }

    @Test
    public void sizeLessThan2(){
        final IForecaster mockForecaster = context.mock(IForecaster.class);
        CachedForecaster cachedForecaster = new CachedForecaster(mockForecaster);

        final Forecast forecast = new Forecast("test", 15);

        cachedForecaster.setSize(1);

        context.checking(new Expectations() {{
            allowing(mockForecaster).forecastFor(Region.LONDON, Day.WEDNESDAY);
                will(returnValue(forecast));
        }});

        Forecast forecast1 = cachedForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY);

        context.checking(new Expectations() {{
            allowing(mockForecaster).forecastFor(Region.LONDON, Day.TUESDAY);
                will(returnValue(forecast));
        }});

        Forecast forecast2  = cachedForecaster.forecastFor(Region.LONDON, Day.TUESDAY);

        List<CachedForecaster.ForecastStorage> cache = cachedForecaster.getCache();

        Assert.assertEquals(1, cache.size());
    }

    @Test
    public void integrationCachedForecaster() {
        Forecaster forecaster = new Forecaster();
        WeatherForecaster concreteForecaster = new WeatherForecaster(forecaster);

        Forecast forecastFromConcrete = concreteForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY);

        CachedForecaster cachedForecaster = new CachedForecaster(concreteForecaster);

        Forecast forecastFromCache = cachedForecaster.forecastFor(Region.LONDON, Day.WEDNESDAY);
        Forecast forecastFromCacheDifferent = cachedForecaster.forecastFor(Region.LONDON, Day.FRIDAY);

        Assert.assertSame(forecastFromCache, forecastFromConcrete);
        Assert.assertNotSame(forecastFromCache, forecastFromCacheDifferent);
    }
}
