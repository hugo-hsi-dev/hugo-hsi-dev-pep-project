package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    ObjectMapper om;
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        om = new ObjectMapper();
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("register", this::registerHandler);

        app.post("login", this::loginHandler);

        app.post("messages", this::postMessageHandler);

        app.get("messages", this::getMessagesHandler);

        
        app.get("messages/{id}", this::getMessageByIdHandler);

        app.delete("messages/{id}", this::deleteMessageByIdHandler);

        app.patch("messages/{id}", this::updateMessageByIdHandler);

        app.get("accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        Account account = om.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.register(account);
        if (registeredAccount == null) {
            ctx.status(400);
            return;
        }

        ctx.json(registeredAccount);
    }

    private void loginHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        Account account = om.readValue(ctx.body(), Account.class);
        Account authenticatedAccount = accountService.login(account);
        if (authenticatedAccount == null) {
            ctx.status(401);
            return;
        }

        ctx.json(authenticatedAccount);
    }

    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        Message message = om.readValue(ctx.body(), Message.class);
        Message postedMessage = messageService.post(message);

        if (postedMessage == null) {
            ctx.status(400);
            return;
        }

        ctx.json(postedMessage);
    }

    private void getMessagesHandler(Context ctx) throws JsonMappingException, JsonProcessingException {

        List<Message> messages = messageService.getAllMessages();

        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        String stringId = ctx.pathParam("id");
        int id = Integer.parseInt(stringId);
        Message message = messageService.getMessageById(id);

        if (message == null) {
            ctx.status(200);
            return;
        }

        ctx.json(message);
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        String stringId = ctx.pathParam("id");
        int id = Integer.parseInt(stringId);
        Message message = messageService.deleteMessageById(id);

        if (message == null) {
            ctx.status(200);
            return;
        }

        ctx.json(message);
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        String stringId = ctx.pathParam("id");
        int id = Integer.parseInt(stringId);
        Message message = om.readValue(ctx.body(), Message.class);

        Message updatedMessage = messageService.updateMessageById(id, message.getMessage_text());

        if (updatedMessage == null) {
            ctx.status(400);
            return;
        }

        ctx.json(updatedMessage);
    }

    private void getMessagesByAccountIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        String stringId = ctx.pathParam("account_id");
        int id = Integer.parseInt(stringId);

        List<Message> messages = messageService.getMessagesByUserId(id);

        if (messages == null) {
            ctx.status(200);
            return;
        }
        ctx.json(messages);
    }
}