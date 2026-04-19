package naukri_automation.Service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class SeleniumService {

    public void updateProfile(String email, String password) {

        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {

            // =========================
            // STEP 1: LOGIN
            // =========================
            driver.get("https://www.naukri.com/nlogin/login");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameField")))
                    .sendKeys(email);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("passwordField")))
                    .sendKeys(password);

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit']")
            )).click();

            Thread.sleep(5000);

            if (driver.getCurrentUrl().contains("login")) {
                throw new RuntimeException("Login Failed");
            }

            // =========================
            // STEP 2: PROFILE PAGE
            // =========================
            driver.get("https://www.naukri.com/mnjuser/profile");

            WebElement editBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//span[text()='Key skills']/following-sibling::span[contains(@class,'edit')]")
                    )
            );

            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("arguments[0].scrollIntoView(true);", editBtn);
            Thread.sleep(2000);
            js.executeScript("arguments[0].click();", editBtn);

            System.out.println("Edit clicked ✅");

            // =========================
            // STEP 3: READ SKILLS
            // =========================
            List<WebElement> skills = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.xpath("//span[contains(@class,'chip')]")
                    )
            );

            boolean hasJwt = false;

            for (WebElement skill : skills) {
                if (skill.getText().trim().equalsIgnoreCase("Jwt")) {
                    hasJwt = true;
                }
            }

            // =========================
            // STEP 4: REMOVE FIRST SKILL
            // =========================
            WebElement removeBtn = skills.get(0).findElement(
                    By.xpath("./following-sibling::span")
            );

            js.executeScript("arguments[0].click();", removeBtn);

            // =========================
            // STEP 5: ADD SKILL (FIXED)
            // =========================
            WebElement inputBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[contains(@placeholder,'Add')]")
                    )
            );

            inputBox.click();

            String skillToAdd = hasJwt ? "SOAP" : "Jwt";

            System.out.println("Adding: " + skillToAdd);

            inputBox.sendKeys(skillToAdd);

            // 🔥 FINAL FIX: flexible dropdown selection
            try {
                WebElement suggestion = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[contains(text(),'" + skillToAdd + "')]")
                        )
                );
                suggestion.click();
            } catch (Exception e) {
                // fallback
                inputBox.sendKeys(Keys.DOWN);
                inputBox.sendKeys(Keys.ENTER);
            }

            // =========================
            // STEP 6: CONFIRM SKILL ADDED
            // =========================
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[contains(@class,'chip') and text()='" + skillToAdd + "']")
            ));

            Thread.sleep(2000);

            // =========================
            // STEP 7: SAVE
            // =========================
            WebElement saveBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("saveKeySkills"))
            );

            js.executeScript("arguments[0].scrollIntoView(true);", saveBtn);
            Thread.sleep(2000);
            js.executeScript("arguments[0].click();", saveBtn);

            System.out.println("✅ DONE SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}