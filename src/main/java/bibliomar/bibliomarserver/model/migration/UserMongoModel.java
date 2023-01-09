package bibliomar.bibliomarserver.model.migration;

import java.util.LinkedHashMap;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class UserMongoModel {
    @Id
    @Field("_id")
    ObjectId id;
    String username;
    String password;
    String email;
    List<LinkedHashMap> reading;
    @Field("to-read")
    List<LinkedHashMap> toRead;
    List<LinkedHashMap> backlog;
}
