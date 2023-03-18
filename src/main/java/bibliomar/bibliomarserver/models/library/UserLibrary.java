package bibliomar.bibliomarserver.models.library;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import bibliomar.bibliomarserver.models.user.User;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("userLibrary")
    private User user;

    @Transient
    @JsonProperty
    private int pagesRead;


    public void removeEntry(UserLibraryEntry entryToRemove) {
        String MD5 = entryToRemove.getMD5();
        String category = entryToRemove.getCategory();
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
     * <br>
     * The target category is determined by the entry's category field.
     *
     * @param entryToAdd The entry to add.
     */
    public void appendEntry(UserLibraryEntry entryToAdd) {
        String metadataMD5 = entryToAdd.getMD5();
        String category = entryToAdd.getCategory();

        if (entryToAdd.getCategory() == null || entryToAdd.getCategory().isBlank()) {
            throw new IllegalArgumentException("Entry category must not be null or blank.");
        }

        UserLibraryEntry possibleExistingEntry = getEntry(metadataMD5);
        if (possibleExistingEntry != null) {
            throw new IllegalArgumentException("A entry with the same MD5 is already in the user's library.");
        }

        entryToAdd.setAddedOnLibraryAt(Instant.now());

        switch (category) {
            case "reading" -> reading.put(metadataMD5, entryToAdd);
            case "toRead" -> toRead.put(metadataMD5, entryToAdd);
            case "finished" -> finished.put(metadataMD5, entryToAdd);
            case "backlog" -> backlog.put(metadataMD5, entryToAdd);
            case "dropped" -> dropped.put(metadataMD5, entryToAdd);
            default ->
                    throw new IllegalArgumentException("Category must be one of the following: reading, toRead, finished, backlog, dropped.");
        }

    }

    public void moveEntry(UserLibraryEntry entryToMove, String targetCategory) {
        if (entryToMove.getCategory() == null || entryToMove.getCategory().isBlank()) {
            throw new IllegalArgumentException("Entry category must not be null or blank.");
        }
        if (targetCategory == null || targetCategory.isBlank()) {
            throw new IllegalArgumentException("Target category must not be null or blank.");
        }
        if (entryToMove.getCategory().equals(targetCategory)) {
            throw new IllegalArgumentException("Entry is already in the target category.");
        }
        removeEntry(entryToMove);
        entryToMove.setCategory(targetCategory);
        appendEntry(entryToMove);
    }


    /**
     * Returns a HashMap containing all categories.
     * Useful for cases where it's needed to iterate over all categories.
     * <br>
     * If you want to get a book from any category, use the getBook method from this class.
     *
     * @return A HashMap containing all categories.
     */
    @JsonIgnore
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
                if (entry.getCategory() == null || entry.getCategory().isBlank()) {
                    entry.setCategory(currentCategory);
                }
                return entry;
            }
        }
        return null;
    }

    private void buildPagesRead(){
        int internalPagesRead = 0;
        for (UserLibraryEntry entry : this.getFinished().values()){
            try {
                int pagesAsInt = Integer.parseInt(entry.getPages());
                internalPagesRead += pagesAsInt;
            } catch (NumberFormatException e){
                continue;
            }
        }

        this.pagesRead = internalPagesRead;
    }

    @JsonGetter("pagesRead")
    private int getPagesRead(){
        buildPagesRead();
        return this.pagesRead;
    }

}
