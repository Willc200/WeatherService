# Weather Service

This weather service was written using spring boot and vaadin. It retrieves the live weather conditions in locations using the openweathermap api.

## Process

To run the application, run the "WeatherServiceApplication" file located in "src/main/java/com/example/weatherService/WeatherServiceApplication.java".

Once the program is running, navigate to your browser and type "http://localhost:8080/" into your url. A text field will appear alongside a search button. 

By entering a location into the text field and clicking search will return the current weather condition and temperature in that area. However, if the location doe's not exist there is error handling put in place to return "Location does not exist".

## Test-automation with Selenium

To run selenium the "WeatherServiceApplication" must be running. Navigate to the "WeatherServiceApplicationTests" file located in "src/test/java/com/example/weatherService/WeatherServiceApplicationTests.java" and click run.

Chromedriver is used which after running will open a chrome browser which will automatically insert information into the text field and click the search button, retrieving the information from the inserted location before closing the browser. To test this further, London in  searchField.sendKeys("London") which is seen below can be changed to any string value.

```java
                // chromedriver.exe is located in the resources folder
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		// Chrome driver opens up localhost which is already running by the "WeatherServiceApplication"
		driver.get("http://localhost:8080/");
		// Maximizes the chrome window
		driver.manage().window().maximize();
		// Thread.sleeps put in place to allow time for each action to be viewable
		Thread.sleep(1000);
		WebElement searchField = driver.findElement(By.id("locationSearchField"));
		// Send the location "London" to the search box (this can be changed to test out different inputs)
		searchField.sendKeys("London");
		Thread.sleep(2000);
		WebElement searchButton = driver.findElement(By.id("searchButton"));
		// Click the search button
		searchButton.click();
		Thread.sleep(5000);
		// Close the browser session
		driver.quit();
```


