package naukri_automation.Controller;

import naukri_automation.Entity.UserAccount;
import naukri_automation.Service.NaukriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UpdateController {

    @Autowired
    private NaukriService naukriService;


    @PostMapping("/update")
    public ResponseEntity<String> updateNow(@RequestBody UserAccount user) {

        try {
            naukriService.runUpdate(user);
            return ResponseEntity.ok("Profile Updated Successfully ✅");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
