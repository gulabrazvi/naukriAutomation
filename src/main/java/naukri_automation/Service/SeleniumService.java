package naukri_automation.Service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SeleniumService {

    public void updateProfile(String email, String password) {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {

            // ================= LOGIN =================
            driver.get("https://www.naukri.com/nlogin/login");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameField")))
                    .sendKeys(email);

            driver.findElement(By.id("passwordField")).sendKeys(password);

            driver.findElement(By.xpath("//button[@type='submit']")).click();

            Thread.sleep(5000);

            if (driver.getCurrentUrl().contains("login")) {
                throw new RuntimeException("❌ Login Failed");
            }

            System.out.println("✅ Login Success");

            // ================= PROFILE =================
            driver.get("https://www.naukri.com/mnjuser/profile");

            // ================= OPEN RESUME UPDATE =================
            WebElement updateLink = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//span[text()='Resume']/following::a[text()='Update']")
                    )
            );

            js.executeScript("arguments[0].click();", updateLink);
            System.out.println("Resume Update opened ✅");

            Thread.sleep(3000);

            // ================= DELETE RESUME =================
            try {
                WebElement deleteIcon = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//i[@data-title='delete-resume']")
                        )
                );

                js.executeScript("arguments[0].click();", deleteIcon);
                System.out.println("🗑 Delete icon clicked");

                // 🔥 WAIT FOR MODAL
                WebElement modal = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//div[contains(@class,'confirmationBox')]")
                        )
                );

                // 🔥 FIND DELETE BUTTON INSIDE MODAL ONLY
                WebElement deleteBtn = modal.findElement(
                        By.xpath(".//button[text()='Delete']")
                );

                wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));

                js.executeScript("arguments[0].click();", deleteBtn);

                System.out.println("✅ Resume Deleted");

            } catch (Exception e) {
                System.out.println("⚠️ No resume to delete");
            }

            Thread.sleep(3000);

            // ================= UPLOAD RESUME =================
            WebElement fileInput = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//input[@type='file']")
                    )
            );

            // 🔥 MAKE INPUT VISIBLE
            js.executeScript(
                    "arguments[0].style.display='block'; arguments[0].style.visibility='visible';",
                    fileInput
            );

            String filePath = "D:\\R\\Md_Gulab_Jdev_2.1_YoE_Resume.pdf";

            fileInput.sendKeys(filePath);

            System.out.println("📤 Resume Uploaded");

            Thread.sleep(5000);

            System.out.println("✅ Profile Updated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}