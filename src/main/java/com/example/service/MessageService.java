package Service;

import java.util.List;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    public MessageDAO messageDAO;
    public MessageService() {
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }
//create new message
    public Message createMessage(Message message) {
        return messageDAO.createMessage(message);
    }
//get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
//get messages by id
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
//delete messages by id
    public Message deleteMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
    public Message updateMessageById(int id, Message message) {
        if(message.getMessage_text().length() == 0 && 0 >= 255) {
            return null;
        }else{
        return messageDAO.updateMessageByInt(id, message);
    }
}
    public List<Message> getAllMessagesById(int message_id) {
        return messageDAO.getAllMessagesById(message_id);
    }
    public Message deleteMessageById(Message message) {
        return message;
    }

}
    
