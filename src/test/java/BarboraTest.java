import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

public class BarboraTest {

    private BarboraPage page;


    public BarboraTest() {
        System.setProperty("webdriver.chrome.driver", "../chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        this.page = new BarboraPage(driver, wait, executor);
    }

    @BeforeEach()
    public void setup() {
        page.openBarboraPage();
    }

    /*
    New user registers;
    User searches for a random product from the pre-defined list (Apple, Chicken, Chips, Cola);
     */
    @Test
    public void register() {
        page.openRegistrationForm();
        Assertions.assertTrue(page.isFormNameDisplayed());
        String name = "Barbora";
        String surname = "Testimonia";
        List<String> userData = Arrays.asList(page.getEmail(), page.getPassword(), name, surname);
        page.fillFormData(userData);
        Assertions.assertTrue(page.isRegistrationSuccessful(userData));
        List<String> shoppingList = Arrays.asList("Apple", "Chicken", "Chips", "Cola");
        String shoppingListItem = page.getRandomListItem(shoppingList);
        page.searchForProduct(shoppingListItem);
        Assertions.assertTrue(page.isSearchHeaderCorrect(shoppingListItem));
        Assertions.assertTrue(page.isSearchResultsDisplayed());
    }

    /*
    User adds beer to his cart;
    User selects a delivery time
    */
    @Test
    public void buySomeBeer() {
        String email = "barboratestimonia@test.com";
        String password = "barboratest";
        String searchPhrase = "beer";
        page.login(email, password);
        page.searchForProduct(searchPhrase);
        Assertions.assertTrue(page.isSearchHeaderCorrect(searchPhrase));
        Assertions.assertTrue(page.isSearchResultsDisplayed());
        page.addProductToCart();
        page.selectDeliveryTime();
        Assertions.assertTrue(page.wasReservationMade());
    }

    @AfterEach
    public void teardown() {
        page.quit();
    }
}
