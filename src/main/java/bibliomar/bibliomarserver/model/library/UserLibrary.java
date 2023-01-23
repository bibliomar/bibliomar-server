package bibliomar.bibliomarserver.model.library;

import java.util.HashMap;
import java.util.Map;

import bibliomar.bibliomarserver.model.metadata.Metadata;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_library")
public class UserLibrary {
    @Id
    private String username;

    @NotNull
    @Column(name = "reading", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, UserLibraryEntry> reading = new HashMap<>();
    @NotNull
    @Column(name = "to_read", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, UserLibraryEntry> toRead = new HashMap<>();
    @NotNull
    @Column(name = "finished", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, UserLibraryEntry> finished = new HashMap<>();
    @NotNull
    @Column(name = "backlog", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, UserLibraryEntry> backlog = new HashMap<>();
    @NotNull
    @Column(name = "dropped", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, UserLibraryEntry> dropped = new HashMap<>();

    public void removeFromCategory(String category, String MD5) {
        switch (category) {
            case "reading" -> reading.remove(MD5);
            case "toRead" -> toRead.remove(MD5);
            case "finished" -> finished.remove(MD5);
            case "backlog" -> backlog.remove(MD5);
            case "dropped" -> dropped.remove(MD5);
            default ->
                    throw new IllegalArgumentException("Category must be one of the following: reading, toRead, finished, backlog, dropped.");
        }
    }

    /**
     * Adds an entry to a category.
     * Always prefer this method over a custom implementation.
     *
     * @param category         The category in which to add the book.
     * @param userLibraryEntry The entry to add.
     */
    public void addToCategory(String category, @Valid UserLibraryEntry userLibraryEntry) {
        String metadataMD5 = userLibraryEntry.getMD5();
        UserLibraryEntry possibleExistingEntry = getEntry(metadataMD5);
        if (possibleExistingEntry != null) {
            throw new IllegalArgumentException("A entry with the same MD5 is already in the user's library.");
        }

        switch (category) {
            case "reading" -> reading.put(metadataMD5, userLibraryEntry);
            case "toRead" -> toRead.put(metadataMD5, userLibraryEntry);
            case "finished" -> finished.put(metadataMD5, userLibraryEntry);
            case "backlog" -> backlog.put(metadataMD5, userLibraryEntry);
            case "dropped" -> dropped.put(metadataMD5, userLibraryEntry);
            default ->
                    throw new IllegalArgumentException("Category must be one of the following: reading, toRead, finished, backlog, dropped.");
        }

    }


    /**
     * Returns a HashMap containing all categories.
     * Useful for cases where it's needed to iterate over all categories.
     * <br>
     * If you want to get a book from any category, use the getBook method from this class.
     *
     * @return A HashMap containing all categories.
     */
    public HashMap<String, HashMap<String, UserLibraryEntry>> getAllCategories() {
        HashMap<String, HashMap<String, UserLibraryEntry>> allCategories = new HashMap<>();
        allCategories.put("reading", reading);
        allCategories.put("toRead", toRead);
        allCategories.put("finished", finished);
        allCategories.put("backlog", backlog);
        allCategories.put("dropped", dropped);
        return allCategories;
    }

    /**
     * Searches for a book in all categories.
     * Always prefer this method over a custom implementation.
     *
     * @param MD5 The MD5 hash of the book to search for.
     * @return The book object, or null if it was not found.
     */
    public UserLibraryEntry getEntry(String MD5) {
        String md5Regex = "[a-fA-F0-9]{32}";
        if (MD5 == null || !MD5.matches(md5Regex)) {
            throw new IllegalArgumentException("MD5 must be a 32-character hexadecimal string.");
        }

        for (Map.Entry<String, HashMap<String, UserLibraryEntry>> category : getAllCategories().entrySet()) {
            String currentCategory = category.getKey();
            HashMap<String, UserLibraryEntry> currentCategoryMap = category.getValue();
            if (currentCategoryMap.containsKey(MD5)) {
                UserLibraryEntry entry = currentCategoryMap.get(MD5);
                return entry;
            }
        }
        return null;
    }

}
