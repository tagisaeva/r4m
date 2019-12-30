import org.testng.annotations.Test;

import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UITests {

	private static String driverPath = ".\\driver\\chromedriver.exe";
	private static String url = "ht" +"tps://r"+ "oute4me." + "com/";
	private static String correctLogin = "<<type here before running>>";
	private static String correctPassword = "<<type here before running>>";

	/*
	 * Login
	 * 
	 */
	@DataProvider
	public Object[][] credentialsData() {
		return new Object[][] { 
			{ correctLogin, correctPassword, true },
			{ correctLogin, "wrongpassword", false }, 
			{ "wrongemail@gmail.com", correctPassword, false }, 
		};
	}

	@Test(dataProvider = "credentialsData")
	public static void ExecuteLoginTest(String mylogin, String mypassword, boolean expresult)
			throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver();
		driver.get(url);

		WebDriverWait pagelogin = new WebDriverWait(driver, 10);
		pagelogin.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[@class='right-side']//a[@href='/login']")));
		driver.findElement(By.xpath("//div[@class='right-side']//a[@href='/login']")).click();

		WebElement enteremail = driver.findElement(By.id("strEmail"));
		enteremail.sendKeys(mylogin);

		WebElement password = driver.findElement(By.id("strPassword"));
		password.sendKeys(mypassword);

		driver.findElement(By.xpath("//*[@type='submit' and contains(text(),'Log In')]")).click();
		Thread.sleep(2000L);

		boolean haslogout;
		try {

			driver.findElement(By.xpath("//*[@href='" + url +"logout']"));
			haslogout = true;

		} catch (NoSuchElementException e) {
			haslogout = false;
		}

		driver.close();

		assertEquals(haslogout, expresult);
	}

	/*
	 * Search
	 * 
	 */

	public static boolean checksearch(String strsearch, Map<String, Boolean> expmyHashMapsixthroute)
			throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver();
		driver.get(url);

		WebDriverWait pagelogin = new WebDriverWait(driver, 10);
		pagelogin.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[@class='right-side']//a[@href='/login']")));
		driver.findElement(By.xpath("//div[@class='right-side']//a[@href='/login']")).click();

		WebElement enteremail = driver.findElement(By.id("strEmail"));
		enteremail.sendKeys(correctLogin);

		WebElement password = driver.findElement(By.id("strPassword"));
		password.sendKeys(correctPassword);

		driver.findElement(By.xpath("//*[@type='submit' and contains(text(),'Log In')]")).click();
		Thread.sleep(2000L);

		WebElement search = driver.findElement(By.xpath("//input[@placeholder='Search in My Routes']"));
		search.sendKeys(strsearch);

		Thread.sleep(5000L);

		List<WebElement> results = driver
				.findElements(By.xpath("//*[@class='td_route_name']//a[@class='darkLink']//span"));

		Map<String, Boolean> myHashMap = new HashMap<String, Boolean>();
		myHashMap.put("Monday 23rd of December 2019 02:10 PM (+01:00)", false);
		myHashMap.put("Tuesday 24th of December 2019 02:23 PM (+01:00)", false);
		myHashMap.put("Wednesday 25th of December 2019 04:08 PM (+01:00)", false);
		myHashMap.put("\"Route fourth\"", false);
		myHashMap.put("fifth route", false);
		myHashMap.put("ROUTEseventh", false);
		myHashMap.put("sixthroute", false);
		myHashMap.put("Monday 23rd of December 2019 057455", false);
		myHashMap.put("Route 8", false);
		myHashMap.put("r9", false);

		for (WebElement element : results) {
			myHashMap.put(element.getAttribute("innerHTML"), true);
		}

		System.out.println(myHashMap);

		boolean result = myHashMap.equals(expmyHashMapsixthroute);
		driver.close();
		return result;
	}

	@Test
	public static void ExecuteSearchTest_ByRouteName() {
		Map<String, Boolean> exptest = new HashMap<String, Boolean>();

		exptest.put("Monday 23rd of December 2019 02:10 PM (+01:00)", false);
		exptest.put("Tuesday 24th of December 2019 02:23 PM (+01:00)", false);
		exptest.put("Wednesday 25th of December 2019 04:08 PM (+01:00)", false);
		exptest.put("\"Route fourth\"", false);
		exptest.put("fifth route", false);
		exptest.put("ROUTEseventh", false);
		exptest.put("sixthroute", true);
		exptest.put("Monday 23rd of December 2019 057455", false);
		exptest.put("Route 8", false);
		exptest.put("r9", false);
		String searchwordtest = "sixthroute";
		
		boolean testresult = false;
		
		try {
			testresult = checksearch(searchwordtest, exptest);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(testresult);
	}

	@Test
	public static void ExecuteSearchTest_ByAddress() {
		Map<String, Boolean> exptest = new HashMap<String, Boolean>();

		exptest.put("Monday 23rd of December 2019 02:10 PM (+01:00)", false);
		exptest.put("Tuesday 24th of December 2019 02:23 PM (+01:00)", false);
		exptest.put("Wednesday 25th of December 2019 04:08 PM (+01:00)", true);
		exptest.put("\"Route fourth\"", false);
		exptest.put("fifth route", false);
		exptest.put("ROUTEseventh", false);
		exptest.put("sixthroute", false);
		exptest.put("Monday 23rd of December 2019 057455", false);
		exptest.put("Route 8", false);
		exptest.put("r9", false);
		String searchwordtest = "6431 Bandini Blvd, Commerce, CA 90040, USA";
		
		boolean testresult = false;
		
		try {
			testresult = checksearch(searchwordtest, exptest);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(testresult);
	}
}
