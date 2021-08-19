package com.example.weatherService;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
class WeatherServiceApplicationTests {

	@Test
	void contextLoads() throws InterruptedException {
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
	}

}
