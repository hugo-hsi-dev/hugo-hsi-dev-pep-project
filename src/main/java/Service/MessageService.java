package Service;

import java.util.ArrayList;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO) {
       this.messageDAO = messageDAO;
       accountDAO = new AccountDAO();
    }

    public MessageService(AccountDAO accountDAO) {
        messageDAO = new MessageDAO();
        this.accountDAO = accountDAO;
     }

     public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
     }

    public Message post(Message message) {
        String messageText = message.getMessage_text();
        if (messageText.length() == 0) {
            return null;
        }
        if (messageText.length() > 255) {
            return null;
        }

        Boolean accountExists = accountDAO.checkExistingAccountId(message.getPosted_by());

        if (!accountExists) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessageById(int id, String text) {
        Message existingMessage = messageDAO.getMessageById(id);

        if (existingMessage == null) {
            return null;
        }

        if (text.length() == 0) {
            return null;
        }

        if (text.length() > 255) {
            return null;
        }

        return messageDAO.updateMessageById(id, text);
    }

    public List<Message> getMessagesByUserId(int id) {
        return messageDAO.GetAllMessageByAccountId(id);
    }

}
