package bibliomar.bibliomarserver.model.activity;

import bibliomar.bibliomarserver.model.user.User;
import bibliomar.bibliomarserver.utils.contants.ActivityTypes;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "activity")
public class Activity {
    @Column(name = "activity_type", nullable = false)
    private ActivityTypes activityType;
    @Column(name = "activity", nullable = false, columnDefinition = "json")
    @Type(JsonType.class)
    private ActivityEntryBase activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @Id
    private User user;

    @Column(name = "time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime time;

    public static Activity build(User user, ActivityTypes activityType, ActivityEntryBase activity) {
        Activity newActivity = new Activity();
        newActivity.setUser(user);
        newActivity.setActivityType(activityType);
        newActivity.setActivity(activity);
        return newActivity;
    }

}
