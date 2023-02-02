package mail.storage.repository;

import mail.storage.domain.Message;
import mail.storage.domain.MessageType;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Optional<Message> findByNumber(Long number);
    List<Message> findByTopic(String topic);
    List<Message> findByType(MessageType type);

    @Aggregation(value = {
            "{match:{'date':{$gte:ISODate(#{#beginDate})}}",
            "{match:{'date':{$lte:ISODate(#{#endDate})}}"
    })
    List<Message> findByDateRange(Date beginDate, Date endDate);

    void deleteByNumber(Long number);
}
