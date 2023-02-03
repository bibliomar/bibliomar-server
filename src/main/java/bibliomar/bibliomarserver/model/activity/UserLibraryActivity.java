package bibliomar.bibliomarserver.model.activity;

import bibliomar.bibliomarserver.model.library.UserLibraryEntry;
import bibliomar.bibliomarserver.utils.contants.Topics;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLibraryActivity extends ActivityEntryBase {
    @NotNull
    private String MD5;
    @NotNull

    private Topics topic;
    @NotNull
    private String category;

    /**
     * Prefered way to build a UserLibraryActivity.
     *
     * @param UserLibraryEntry entry
     * @return UserLibraryActivity
     */
    public static UserLibraryActivity build(UserLibraryEntry entry) {
        UserLibraryActivity activity = new UserLibraryActivity();
        activity.setMD5(entry.getMD5());
        activity.setTopic(entry.getTopic());
        activity.setCategory(entry.getCategory());
        return activity;
    }
}
