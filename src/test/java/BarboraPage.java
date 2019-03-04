import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class BarboraPage {
    private WebDriver driver;
    private WebDriverWait wait;
    public JavascriptExecutor executor;
    private Random rand = new Random();

    private String randomString = RandomStringUtils.randomAlphanumeric(10);
    private String randomNumber = RandomStringUtils.randomNumeric(8);

    public BarboraPage(WebDriver driver, WebDriverWait wait, JavascriptExecutor executor) {
        this.driver = driver;
        this.wait = wait;
        this.executor = executor;
    }

    public void openBarboraPage() {
        driver.manage().window().maximize();
        driver.navigate().to("https://www.barbora.lt/");
    }

    public void openRegistrationForm() {
        driver.findElement(By.className("b-login-register--register")).click();
    }

    public void fillFormData(List<String> userData) {
        String emailSelector = "#b-user-register > form > div:nth-child(1) > div > input";
        String passwordSelector = "#b-user-register > form > div:nth-child(2) > div > input";
        String register = "#b-user-register > form > div:nth-child(15) > div > button";
        WebElement registrationForm = driver.findElement(By.cssSelector(".b-login-register--modal"));
        fillInputByCssSelector(emailSelector, userData.get(0));
        fillInputByCssSelector(passwordSelector, userData.get(1));
        fillInputByName("street", "aug");
        waitForAddressLoader(wait, false);
        waitForAddressLoader(wait, true);
        driver.findElement(By.name("street")).sendKeys(Keys.DOWN);
        fillInputByName("name", userData.get(2));
        fillInputByName("flat", "5");
        fillInputByName("surname", userData.get(3));
        fillInputByName("phone", getPhoneNumber());
        driver.findElement(By.name("agreetorules")).click();
        registrationForm.findElement(By.cssSelector(register)).click();
    }


    public String getPassword() {
        return randomString;
    }

    public String getEmail() {
        return randomString + "@" + randomString + ".com";
    }

    public void login(String email, String password) {
        driver.findElement(By.className("b-login-register--login")).click();
        driver.findElement(By.id("b-login-email")).sendKeys(email);
        driver.findElement(By.id("b-login-password")).sendKeys(password);
        driver.findElement(By.className("b-login-form--login-button")).click();
        waitForElement(wait, By.className("b-search--large"));
    }

    public boolean isFormNameDisplayed() {
        return driver.findElement(By.cssSelector("[aria-controls='b-user-register']")).isDisplayed();
    }


    private void waitForElement(WebDriverWait wait, By by) {
        wait.ignoring(NoSuchElementException.class);
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(by).isDisplayed();
            }
        });
    }

    public void searchForProduct(String product) {
        driver.findElement(By.className("b-search--large")).sendKeys(product);
        driver.findElement(By.className("b-search--initiate")).click();
    }

    public String getRandomListItem(List<String> shoppingList) {
        return shoppingList.get(rand.nextInt(shoppingList.size()));
    }


    public void selectDeliveryTime() {
        driver.findElement(By.className("b-sidebar-menu--calendar")).click();
        availableDeliveryTimeSlots().get(rand.nextInt(availableDeliveryTimeSlots().size())).click();
    }

    public void addProductToCart() {
        WebElement addToCartButton = addToCartButtons().get(rand.nextInt(addToCartButtons().size()));
        executor.executeScript("arguments[0].click();", addToCartButton);
        if (isElementPresent(By.className("b-alert--modal"))) {
            driver.findElement(By.cssSelector("div.modal-footer > div > div:nth-child(2) > button")).click();
        }
    }

    public boolean isRegistrationSuccessful(List<String> userData) {
        WebElement loggedInUser = driver.findElement(By.className("b-logout"));
        return loggedInUser.getText().contains(userData.get(2) + " " + userData.get(3));
    }

    public boolean isSearchResultsDisplayed() {
        List<WebElement> searchResults = driver.findElements(By.className("b-product--desktop-grid"));
        return !searchResults.isEmpty();
    }

    public boolean isSearchHeaderCorrect(String shoppingListItem) {
        WebElement searchPageHeader = driver.findElement(By.className("b-search--page-heading-wrap"));
        return searchPageHeader.getText().contains(shoppingListItem);
    }


    public boolean wasReservationMade() {
        return driver.findElement(By.className("b-sidebar-menu--countdown")).isDisplayed();
    }

    private String getPhoneNumber() {
        return "+370" + randomNumber;
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    private List<WebElement> addToCartButtons() {
        return driver.findElements(By.className("b-add-to-cart"));
    }

    private List<WebElement> availableDeliveryTimeSlots() {
        return driver.findElements(By.className("b-deliverytime--slot-available"));
    }

    private void fillInputByCssSelector(String selector, String value) {
        driver.findElement(By.cssSelector(selector)).sendKeys(value);
    }

    private void fillInputByName(String name, String value) {
        driver.findElement(By.name(name)).sendKeys(value);
    }

    private void waitForAddressLoader(WebDriverWait wait, boolean loaded) {
        wait.ignoring(NoSuchElementException.class);
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String streetClasses = driver.findElement(By.name("street")).getAttribute("class");
                return loaded == streetClasses.contains("ui-autocomplete-loading");
            }
        });
    }
}

