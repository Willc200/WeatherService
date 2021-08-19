package com.example.weatherService.views;


import com.example.weatherService.controller.WeatherController;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "")
public class WeatherView extends UI {

    @Autowired
    private WeatherController weatherController;

    private VerticalLayout weatherLayout;
    private HorizontalLayout header;
    private TextField locationTextField;
    private Button searchButton;
    private HorizontalLayout textLayout;
    private HorizontalLayout locationInformation;
    private Label location;
    private Label currentTemp;
    private HorizontalLayout weatherDescriptionLayout;
    private Label weatherDescription;
    private Label tempMin;
    private Label tempMax;
    private Image iconImage;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        weatherLayout();
        setHeader();
        setTextLayout();
        locationTitle();
        weatherInformation();

        // Error handling 1: If nothing is inputted in the search field a notification will pop up
        searchButton.addClickListener(clickEvent -> {
            if(!locationTextField.getValue().equals("")) {
                try {
                    updateLocation();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show("Please enter a location");
            }
        });
    }

    private void weatherLayout() {
        iconImage = new Image();
        weatherLayout = new VerticalLayout();
        weatherLayout.setWidth("100%");
        weatherLayout.setSpacing(true);
        weatherLayout.setMargin(true);
        weatherLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(weatherLayout);
    }

    private void setHeader() {
        header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label title = new Label("Weather Service for Genesys");
        header.addComponent(title);
        weatherLayout.addComponent(header);
    }

    private void setTextLayout() {
        textLayout = new HorizontalLayout();

        // Location text field
        locationTextField = new TextField();
        locationTextField.setId("locationSearchField");
        locationTextField.setWidth("80%");
        textLayout.addComponent(locationTextField);

        // Search button
        searchButton = new Button("Search");
        searchButton.setId("searchButton");
        searchButton.setIcon(VaadinIcons.SEARCH);
        textLayout.addComponent(searchButton);

        weatherLayout.addComponent(textLayout);

    }

    private void locationTitle() {
        locationInformation = new HorizontalLayout();
        locationInformation.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // Location
        location = new Label();
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        // Current Temperature
        currentTemp = new Label();
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);

        locationInformation.addComponents(location, iconImage, currentTemp);
    }

    private void weatherInformation() {
        weatherDescriptionLayout = new HorizontalLayout();
        weatherDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // Description layout
        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // Description of weather
        weatherDescription = new Label();
        descriptionLayout.addComponent(weatherDescription);

        // Minimum Temperature
        tempMin = new Label();
        descriptionLayout.addComponent(tempMin);

        // Maximum Temperature
        tempMax = new Label();
        descriptionLayout.addComponent(tempMax);

        weatherDescriptionLayout.addComponent(descriptionLayout);
    }

    private void updateLocation() throws JSONException {
        String locationName = locationTextField.getValue();
        String unit = "\u00b0" + "C";
        weatherController.setLocation(locationName);
        weatherController.setUnit("metric");

        /* Error handling 2: If the location inputted doe's not exist
        Location exists -> "cod": 200
        Location doe's not exist -> "cod": 404
         */
        if(weatherController.getWeather().getInt("cod") == 404) {
            weatherController.setLocation("Location does not exist");
            location.setValue("Location does not exist");
            iconImage.setSource(new ExternalResource("http://openweathermap.org/img/wn/50d@2x.png"));

            currentTemp.setValue("Unknown");
            weatherDescription.setValue("Weather: Unknown");
            tempMin.setValue("Min Temp: Unknown");
            tempMax.setValue("Max Temp: Unknown");

            // Weather information styling shows the search was a failure
            weatherDescription.setStyleName(ValoTheme.LABEL_FAILURE);
            tempMax.setStyleName(ValoTheme.LABEL_FAILURE);
            tempMin.setStyleName(ValoTheme.LABEL_FAILURE);
        } else {
            weatherController.setUnit("metric");
            location.setValue("Current location: " + locationName);
            JSONObject obj = weatherController.retrieveMain();
            int temp = obj.getInt("temp");
            currentTemp.setValue(temp + unit);

            // Using weather icons found on https://openweathermap.org/weather-conditions
            String iconName = null;
            String weatherType = null;
            JSONArray jsonArray = weatherController.retrieveWeatherArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject weatherObj = jsonArray.getJSONObject(i);
                iconName = weatherObj.getString("icon");
                weatherType = weatherObj.getString("description");
            }

            // Updates weather icon
            iconImage.setSource(new ExternalResource("http://openweathermap.org/img/wn/" + iconName + "@2x.png"));

            // Updates locations weather
            weatherDescription.setValue("Weather: " + weatherType);

            // Updates Min Temperature
            tempMin.setValue("Min Temp: " + weatherController.retrieveMain().getInt("temp_min") + unit);

            // Updates Max Temperature
            tempMax.setValue("Max Temp: " + weatherController.retrieveMain().getInt("temp_max") + unit);

            // Weather information styling shows the search was successful
            weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
            tempMin.setStyleName(ValoTheme.LABEL_SUCCESS);
            tempMax.setStyleName(ValoTheme.LABEL_SUCCESS);
        }
        weatherLayout.addComponents(locationInformation, weatherDescriptionLayout);
    }
}
