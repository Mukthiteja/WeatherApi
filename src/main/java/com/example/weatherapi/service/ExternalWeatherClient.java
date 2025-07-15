package com.example.weatherapi.service;

import com.example.weatherapi.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

/**
 * Handles all external API calls to OpenWeatherMap for:
 * - Geolocation (lat/lon) from pincode
 * - Weather data from lat/lon
 */
@Service
public class ExternalWeatherClient {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ExternalWeatherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Calls OpenWeather Geo API to convert pincode into latitude and longitude.
     *
     * @param pincode the Indian postal code
     * @return array of [lat, lon]
     */
    @SuppressWarnings("unchecked")
    public double[] getLatLongFromPincode(String pincode) {
        String url = "http://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",IN&appid=" + apiKey;
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.getForEntity(url, (Class<Map<String, Object>>)(Class<?>)Map.class);
            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("lat") || !body.containsKey("lon")) {
                throw new RuntimeException("Invalid response from Geo API: missing lat/lon");
            }
            double lat = ((Number) body.get("lat")).doubleValue();
            double lon = ((Number) body.get("lon")).doubleValue();
            return new double[]{lat, lon};
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid API key or bad request: " + e.getResponseBodyAsString(), e);
        }
    }

    /**
     * Calls OpenWeather Current Weather API using lat/lon.
     *
     * @param lat  Latitude
     * @param lon  Longitude
     * @param date The date for which weather is requested (used for DB storage, not API)
     * @return Weather object with parsed values
     */
    @SuppressWarnings("unchecked")
    public Weather getWeatherFromLatLong(double lat, double lon, String date) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.getForEntity(url, (Class<Map<String, Object>>)(Class<?>)Map.class);
            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("weather") || !body.containsKey("main")) {
                throw new RuntimeException("Invalid response from Weather API: missing weather or main data");
            }

            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) body.get("weather");
            Map<String, Object> weatherDetails = weatherList.get(0);
            Map<String, Object> mainDetails = (Map<String, Object>) body.get("main");

            Weather w = new Weather();
            w.setDescription((String) weatherDetails.get("description"));
            w.setTemperature(((Number) mainDetails.get("temp")).doubleValue());
            w.setHumidity(((Number) mainDetails.get("humidity")).doubleValue());
            w.setDate(date);

            return w;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid API key or bad request: " + e.getResponseBodyAsString(), e);
        }
    }
}
