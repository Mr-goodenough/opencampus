package yang.opencampus.opencampusback.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import yang.opencampus.opencampusback.entity.UncheckedComment;

public interface UncheckedCommentRepository extends MongoRepository<UncheckedComment,String>{
    UncheckedComment findBy_id(String id);
    void deleteBy_id(String _id);
}
