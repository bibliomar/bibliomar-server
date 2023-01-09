package bibliomar.bibliomarserver.service.migration;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bibliomar.bibliomarserver.model.migration.UserMongoModel;
import bibliomar.bibliomarserver.model.user.User;
import bibliomar.bibliomarserver.repository.migration.MongoMigrationRepository;
import bibliomar.bibliomarserver.repository.user.UserRepository;

/*
 * Migrates data from MongoDB to MySQL, with the correct SpringData definition.
 */

@Service
public class MigrationService {
    @Autowired
    MongoMigrationRepository migrationRepository;

    @Autowired
    UserRepository userRepository;

    private Hashtable<String, Object> listAsHashtable(List<LinkedHashMap> list) {
        Hashtable<String, Object> categoryHashtable = new Hashtable<String, Object>();
        for (LinkedHashMap<Object, String> item : list) {
            String md5 = item.get("md5");
            categoryHashtable.put(md5, item);

        }
        return categoryHashtable;
    }

    public void migrate() {
        List<UserMongoModel> users = migrationRepository.findAll();
        users.forEach(user -> {

            String username = user.getUsername();
            String password = user.getPassword();
            String email = user.getEmail();

            User existingUser = userRepository.findByUsername(username);
            if (existingUser != null) {
                System.out.println("User already exists: " + username);
                return;
            }

            Hashtable<String, Object> reading = this.listAsHashtable(user.getReading());
            Hashtable<String, Object> toRead = this.listAsHashtable(user.getToRead());
            Hashtable<String, Object> backlog = this.listAsHashtable(user.getBacklog());
            boolean preMigration = true;

            User newUser = new User(username, password, email, preMigration);
            newUser.setReading(reading);
            newUser.setToRead(toRead);
            newUser.setBacklog(backlog);
            userRepository.save(newUser);

            System.out.println("Migrated user:");
            System.out.println(userRepository.findByUsername(username));

        });
    }

}
