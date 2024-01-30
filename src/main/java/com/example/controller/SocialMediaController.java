package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewUserRegistration);
        app.post("/login", this::postLogin);
        app.get("/messages", this::getAllMessages);
        app.post("/messages", this::postNewMessage);
        app.get("/messages/{message_id}", this::getMessagesById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesById);

        return app;
    }

    //User registration 
    private void postNewUserRegistration (Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        //parameters for account
        String username = account.getUsername();
        String password = account.getPassword();
        Account newAccount = accountService.insertUser(account);
        if(!username.isBlank() && password.length() >= 4 && newAccount != null){
            ctx.json(mapper.writeValueAsString(newAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }
    //Login info
    private void postLogin(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        //no account id
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account checkAccount = accountService.checkLogin(account);

        if(checkAccount != null){
            ctx.json(mapper.writeValueAsString(checkAccount));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }
    //Creating new Message
    private void postNewMessage(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        String messageParam = message.getMessage_text();

        if(!messageParam.isBlank() && messageParam.length() < 255 ) {
            message = messageService.createMessage(message); //this method pulls from service class and info from DAO class
        }
        Integer existingUser = message.getMessage_id();

        if( existingUser != 0) {
            ctx.json(mapper.writeValueAsString(message));
            ctx.status(200);
        } else{
            ctx.status(400);
        }
    }
    //Retrieving ALL Messages
    private void getAllMessages (Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }
    //Get message by message_id
    private void getMessagesById(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message targetMessage = messageService.getMessageById(message_id);

        if(targetMessage != null){
            ctx.json(mapper.writeValueAsString(targetMessage));
        }
    }
    //Delete a message with message_id
    private void deleteMessageById(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleteId = messageService.getMessageById(messageId);

        if(deleteId != null){
            ctx.json(mapper.writeValueAsString(deleteId));
            ctx.status(200);
        }else{
            ctx.status(200);
        }
    }
    //Update message by message_id
    private void updateMessageById (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updateMessage = messageService.updateMessageById(messageId, message);

        String messageParams = message.getMessage_text();
        Integer idParams = message.getMessage_id();

        if(updateMessage != null && idParams.equals(messageId) && messageParams != null && messageParams.length() < 255){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(updateMessage));
        } else{
            ctx.status(400);
        }
    }
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //get all messages by id
    private void getAllMessagesById(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesById(id);
        ctx.json(messages);
        ctx.json(200);
    }
}
