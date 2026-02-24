package ngv.vn.naascccd.service;

import ngv.vn.naascccd.entity.Message;

import java.util.Optional;

public interface MessageService {
    public Optional<Message> createMessage(Message message);
    
    public Optional<Message> updateMessage(Message message);
}
