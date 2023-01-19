package bibliomar.bibliomarserver.service.migration;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bibliomar.bibliomarserver.model.library.UserLibrary;
import bibliomar.bibliomarserver.model.migration.UserMongoModel;
import bibliomar.bibliomarserver.model.user.User;
import bibliomar.bibliomarserver.repository.library.UserLibraryRepository;
import bibliomar.bibliomarserver.repository.migration.UserMongoRepository;
import bibliomar.bibliomarserver.repository.user.UserRepository;

/*
 * Migrates data from MongoDB to MySQL, with the correct SpringData definition.
 */

@Service
public class MigrationService {

    @Autowired
    UserLibraryRepository userLibraryRepository;

    @Autowired
    UserMongoRepository migrationRepository;

    @Autowired
    UserRepository userRepository;

    private HashMap<String, Object> listAsHashMap(List<LinkedHashMap<Object, String>> list) {
        HashMap<String, Object> categoryHashtable = new HashMap<>();
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

            HashMap<String, Object> reading = this.listAsHashMap(user.getReading());
            HashMap<String, Object> toRead = this.listAsHashMap(user.getToRead());
            HashMap<String, Object> backlog = this.listAsHashMap(user.getBacklog());
            boolean preMigration = true;

            User newUser = new User(username, password, email, preMigration);
            UserLibrary newLibrary = new UserLibrary();
            newLibrary.setUsername(username);
            newLibrary.setReading(reading);
            newLibrary.setToRead(toRead);
            newLibrary.setBacklog(backlog);

            newUser.setUserLibrary(newLibrary);
            userRepository.save(newUser);

            User shouldBeSavedUser = userRepository.findByUsername(username);
            if (shouldBeSavedUser != null) {
                System.out.println("Migrated user:");
                System.out.println(shouldBeSavedUser.getUsername());
            } else {
                System.out.println("Failed to migrate user: " + username);
            }

        });
    }

}
