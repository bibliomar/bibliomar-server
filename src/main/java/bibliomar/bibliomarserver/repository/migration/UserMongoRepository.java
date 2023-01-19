package bibliomar.bibliomarserver.repository.migration;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


import bibliomar.bibliomarserver.model.migration.UserMongoModel;

public interface UserMongoRepository extends MongoRepository<UserMongoModel, String> {
    public List<UserMongoModel> findByUsername(String username);

    public List<UserMongoModel> findAll();

}
