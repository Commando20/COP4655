package com.example.weatherapp;

public class WeatherData {
    private String name;
    private String description;
    private String temperature;
    private String tempMaxMin;
    private String wind;
    private String pressure;
    private String humidity;
    private String sunrise;
    private String sunset;
    private String latitude;
    private String longitude;

    public WeatherData() {}

    public void setName(String weatherName) { this.name = weatherName; }

    public void setDescription(String weatherDescription) { this.description = weatherDescription; }

    public void setTemperature(String weatherTemperature) { this.temperature = weatherTemperature; }

    public void setTempMaxMin(String weatherTempMaxMin) { this.tempMaxMin = weatherTempMaxMin; }

    public void setWind(String weatherWind) { this.wind = weatherWind; }

    public void setPressure(String weatherPressure) { this.pressure = weatherPressure; }

    public void setHumidity(String weatherHumidity) { this.humidity = weatherHumidity; }

    public void setSunrise(String weatherSunrise) { this.sunrise = weatherSunrise; }

    public void setSunset(String weatherSunset) { this.sunset = weatherSunset; }

    public void setLatitude(String weatherLatitude) { this.latitude = weatherLatitude; }

    public void setLongitude(String weatherLongitude) { this.longitude = weatherLongitude; }

    public String getName() { return this.name; }

    public String getDescription() { return this.description; }

    public String getTemperature() { return this.temperature; }

    public String getTempMaxMin() { return this.tempMaxMin; }

    public String getWind() { return this.wind; }

    public String getPressure() { return this.pressure; }

    public String getHumidity() { return this.humidity; }

    public String getSunrise() { return this.sunrise; }

    public String getSunset() { return this.sunset; }

    public String getLatitude() { return this.latitude; }

    public String getLongitude() { return this.longitude; }
}