package mail.storage.repository;

import mail.storage.domain.Message;
import mail.storage.domain.Message.MessageType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Optional<Message> findByNumber(Long number);

    List<Message> findByTopic(String topic);

    List<Message> findByType(MessageType type);

    @Query("{'date': {$gte: ?0, $lte: ?1} }")
    List<Message> findByDateRange(Date beginDate, Date endDate);
    boolean existsByNumber(Long number);
}
