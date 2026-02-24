package ngv.vn.naascccd.service.impl;

import lombok.RequiredArgsConstructor;
import ngv.vn.naascccd.entity.Message;
import ngv.vn.naascccd.repository.MessageRepository;
import ngv.vn.naascccd.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    
    private final MessageRepository messageRepository;
    
    
    @Override
    public Optional<Message> createMessage(Message message) {
        Message messageEntity = messageRepository.save(message);
        return Optional.of(messageEntity);
    }
    
    @Override
    public Optional<Message> updateMessage(Message message) {
        Message messageEntity = messageRepository.save(message);
        return Optional.of(messageEntity);
    }
}
