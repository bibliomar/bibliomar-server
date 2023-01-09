package bibliomar.bibliomarserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bibliomar.bibliomarserver.model.migration.UserMongoModel;
import bibliomar.bibliomarserver.repository.migration.MongoMigrationRepository;
import bibliomar.bibliomarserver.service.migration.MigrationService;

@RestController
@RequestMapping("")
public class SearchController {

    @Autowired
    private MongoMigrationRepository migrationRepository;

    @Autowired
    private MigrationService migrationService;

    @GetMapping()
    public List<UserMongoModel> getAllUsers() {
        return migrationRepository.findAll();

    }

    @GetMapping("/migrate")
    public void migrate() {
        migrationService.migrate();
    }

}
