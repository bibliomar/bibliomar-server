package bibliomar.bibliomarserver.controller.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bibliomar.bibliomarserver.service.migration.MigrationService;

@RestController
@RequestMapping("/migration")
public class MigrationController {

    @Autowired
    private MigrationService migrationService;

    @GetMapping(value = "/mongodb")
    public String migrateMongoDB() {
        migrationService.migrate();
        return "Migrated valid user data from MongoDB to MySQL";
    }
}
