package naukri_automation.Service;

import naukri_automation.Entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class UpdateScheduler {

    @Autowired
    private NaukriService naukriService;

    @Scheduled(cron = "0 0 8 * * ?", zone = "Asia/Kolkata") // daily 8 AM
    public void autoUpdate() {

        UserAccount user = new UserAccount("email", "password");

        naukriService.runUpdate(user);
    }
}
