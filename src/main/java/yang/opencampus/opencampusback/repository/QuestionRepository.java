package yang.opencampus.opencampusback.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import yang.opencampus.opencampusback.entity.Question;

public interface QuestionRepository extends MongoRepository<Question,String>{
    public Question findBy_id(String _id);
}
