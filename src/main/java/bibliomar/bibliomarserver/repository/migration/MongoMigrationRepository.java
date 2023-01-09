package bibliomar.bibliomarserver.repository.migration;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import bibliomar.bibliomarserver.model.migration.UserMongoModel;

public interface MongoMigrationRepository extends MongoRepository<UserMongoModel, String> {
    public List<UserMongoModel> findByUsername(String username);

    public List<UserMongoModel> findAll();

}
