package yang.opencampus.opencampusback.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import yang.opencampus.opencampusback.entity.UncheckedQuestion;


public interface UncheckedQuestionRepository extends MongoRepository<UncheckedQuestion,String>{
    public UncheckedQuestion  findBy_id(String _id);
}
