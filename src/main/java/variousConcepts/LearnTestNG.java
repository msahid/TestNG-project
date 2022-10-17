package variousConcepts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LearnTestNG {

	WebDriver driver;
	String browser;
	String url;

	// Element list
	By USER_NAME_FIELD = By.xpath("//input[@id='username']");
	By PASSWORD_FIELD = By.xpath("//*[@id=\"password\"]");
	By LOGIN_BUTTON_FIELD = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By DASHBOARD_HEADER_FIELD = By.xpath("//*[@id=\"page-wrapper\"]/div[2]/div/h2");
	By CUSTOMER_MENU_BUTTON_FIELD = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By ADD_CUSTOMER_MENU_BUTTON_FIELD = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By FULL_NAME_FIELD = By.xpath("//*[@id=\"account\"]");
	By COMPANY_DROPDOWN_FIELD = By.xpath("//*[@id=\"cid\"]");
	By EMAIL_FIELD = By.xpath("//*[@id=\"email\"]");
	By PHONE_NUMBER_FIELD = By.xpath("//*[@id=\"phone\"]");
	By ADDRESS_FIELD = By.xpath("//*[@id=\"address\"]");
	By CITY_FIELD = By.xpath("//*[@id=\"city\"]");
	By COUNTRY_FIELD = By.xpath("//*[@id=\"country\"]");
	

	// Test data
	String userName = "demo@techfios.com";
	String password = "abc123";
	String dashboarHeader = "Dashboard";

	@BeforeClass
	public void readConfig() {

		// InputStream //BufferedReader //Scanner //FileReader

		try {

			InputStream input = new FileInputStream("src/main/java/config/config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("Firefox")) {
			System.setProperty("webdriver.gecko.driver", "driver/geckodriver");
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("Chrome")) {

			System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
			driver = new ChromeDriver();
		}

//		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.get(url);
	}

	//@Test(priority = 1)
	public void loginTest() throws InterruptedException {
		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(LOGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), dashboarHeader,
				"Dashboard is not available");

		//Thread.sleep(3000);
	}

	//@Test(priority = 2)
	@Test
	public void addCustomer() throws InterruptedException {
		loginTest();
		
		WebDriverWait wait = new WebDriverWait(driver,10);
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\\\"side-menu\\\"]/li[3]/a/span[1]")));
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(CUSTOMER_MENU_BUTTON_FIELD));
		
		// click on the compose button as soon as the "compose" button is visible
		driver.findElement(CUSTOMER_MENU_BUTTON_FIELD).click();
		
		driver.findElement(ADD_CUSTOMER_MENU_BUTTON_FIELD).click();
		boolean fullNameField = driver.findElement(FULL_NAME_FIELD).isDisplayed();
		Assert.assertTrue(fullNameField, "Add Customer page is not available");
			
		driver.findElement(FULL_NAME_FIELD).sendKeys("Selenium"+ randomNumberGenerator(999));
		selectFromDropDown(COMPANY_DROPDOWN_FIELD, "Techfios");
		driver.findElement(EMAIL_FIELD).sendKeys("abc"+ randomNumberGenerator(9999) + "@techfios.com");
		selectFromDropDown(COUNTRY_FIELD,"American Samoa");
		
		
		Thread.sleep(3000);
	}

	

	public int randomNumberGenerator(int boundryNo) {
		
		Random rnd = new Random();
		int randomNumber = rnd.nextInt(boundryNo);
		return randomNumber;
		
	}

	public void selectFromDropDown(By byLocator, String VisibleText) {
		Select sel1 = new Select(driver.findElement(byLocator));
		sel1.selectByVisibleText(VisibleText);
	}


	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
