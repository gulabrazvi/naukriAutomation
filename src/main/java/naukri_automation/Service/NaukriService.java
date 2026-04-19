package naukri_automation.Service;

import naukri_automation.Entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaukriService {

    @Autowired
    private SeleniumService seleniumService;

    @Autowired
    private EmailService emailService;

    public void runUpdate(UserAccount user) {

        seleniumService.updateProfile(user.getEmail(), user.getPassword());

        emailService.sendMail(
                user.getEmail(),
                "Profile Updated",
                "Your Naukri profile was updated successfully!"
        );
    }
}
