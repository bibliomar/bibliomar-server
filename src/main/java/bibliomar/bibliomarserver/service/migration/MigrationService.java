package bibliomar.bibliomarserver.service.migration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import bibliomar.bibliomarserver.model.library.UserLibraryEntry;
import bibliomar.bibliomarserver.model.metadata.FictionMetadata;
import bibliomar.bibliomarserver.model.metadata.Metadata;
import bibliomar.bibliomarserver.model.metadata.ScitechMetadata;
import bibliomar.bibliomarserver.repository.metadata.FictionMetadataRepository;
import bibliomar.bibliomarserver.repository.metadata.ScitechMetadataRepository;
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
    FictionMetadataRepository fictionMetadataRepository;

    @Autowired
    ScitechMetadataRepository scitechMetadataRepository;

    @Autowired
    UserMongoRepository migrationRepository;

    @Autowired
    UserRepository userRepository;

    private void appendListToLibrary(UserLibrary userLibrary,
                                     String targetCategory,
                                     List<LinkedHashMap<Object, Object>> mongoList) {

        for (LinkedHashMap<Object, Object> mongoItem : mongoList) {
            String mongoMD5 = (String) mongoItem.get("md5");
            String mongoTopic = (String) mongoItem.get("topic");
            /**
             * Important: Use the MD5 value stored in the
             * respective metadatas to avoid inconsistency in users' libraries.
             * e.g. Prefer to use metadata.getMD5() instead of the MD5 value stored in MongoDB library.
             */
            if (mongoTopic == "fiction") {
                FictionMetadata fictionMetadata = fictionMetadataRepository.findByMD5(mongoMD5);
                if (fictionMetadata != null) {
                    UserLibraryEntry entry = new UserLibraryEntry(fictionMetadata);
                    entry.setCategory(targetCategory);
                    userLibrary.appendEntry(entry);
                } else {
                    System.out.println("No metadata found for MD5: " + mongoMD5);
                }
            } else {
                ScitechMetadata scitechMetadata = scitechMetadataRepository.findByMD5(mongoMD5);
                if (scitechMetadata != null) {
                    UserLibraryEntry entry = new UserLibraryEntry(scitechMetadata);
                    entry.setCategory(targetCategory);
                    userLibrary.appendEntry(entry);
                } else {
                    System.out.println("No metadata found for MD5: " + mongoMD5);
                }
            }
        }
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

            boolean preMigration = true;

            User newUser = new User(username, password, email, preMigration);
            UserLibrary newLibrary = new UserLibrary();
            newLibrary.setUsername(username);
            this.appendListToLibrary(newLibrary, "reading", user.getReading());
            this.appendListToLibrary(newLibrary, "toRead", user.getToRead());
            this.appendListToLibrary(newLibrary, "backlog", user.getBacklog());

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
