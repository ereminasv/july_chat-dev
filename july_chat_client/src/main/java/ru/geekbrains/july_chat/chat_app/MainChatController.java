package ru.geekbrains.july_chat.chat_app;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.geekbrains.july_chat.chat_app.net.ChatMessageService;
import ru.geekbrains.july_chat.chat_app.net.MessageProcessor;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainChatController implements Initializable, MessageProcessor {
    public static final String REGEX = "%&%";
    public VBox loginPanel;
    public TextField loginField;
    public PasswordField passwordField;
    public VBox changePasswordPanel;
    public PasswordField oldPassField;
    public PasswordField newPasswordField;
    public VBox changeNickPanel;
    public TextField newNickField;
    private ChatMessageService chatMessageService;
    private String nickName;
    public VBox mainChatPanel;
    public TextArea mainChatArea;
    public ListView<String> contactList;
    public TextField inputField;
    public Button btnSendMessage;
    private HistoryRecord historyRecord;


    public void mockAction(ActionEvent actionEvent) {

    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = inputField.getText();
        if (text.isEmpty()) return;
        String recipient = contactList.getSelectionModel().getSelectedItem();
        String message = "";
        if (recipient.equals("ALL")) message = "/" + recipient + REGEX + text;
        else message = "/w" + REGEX + recipient + REGEX + text;
        chatMessageService.send(message);
        historyRecord.writeHistory(String.format("[ME] %s\n", text));
        inputField.clear();
    }


    @Override
    public void processMessage(String message) {
        Platform.runLater(() -> parseMessage(message));
    }

    public void sendAuth(ActionEvent actionEvent) {
        if (loginField.getText().isBlank() || passwordField.getText().isBlank()) return;
        chatMessageService.connect();
        chatMessageService.send("/auth" + REGEX + loginField.getText() + REGEX + passwordField.getText());
    }

    public void sendRegister(ActionEvent actionEvent) {
//        if (loginField.getText().isBlank() || passwordField.getText().isBlank()) return;
//        chatMessageService.connect();
//        chatMessageService.send("/auth"+ REGEX + loginField.getText() + REGEX + passwordField.getText());
    }

    public void sendChangeNick(ActionEvent actionEvent) {
        if (newNickField.getText().isBlank()) return;
        chatMessageService.send("/change_nick" + REGEX + newNickField.getText());
    }

    public void sendChangePass(ActionEvent actionEvent) {
        if (newPasswordField.getText().isBlank() || oldPassField.getText().isBlank()) return;
        chatMessageService.send("/change_pass" + REGEX + oldPassField.getText() + REGEX + newPasswordField.getText());
    }


    public void sendEternalLogout(ActionEvent actionEvent) {
        chatMessageService.send("/remove");
    }

    private void parseMessage(String message) {
        String[] parsedMessage = message.split(REGEX);
        switch (parsedMessage[0]) {
            case "authok:":
                this.nickName = parsedMessage[1];
                loginPanel.setVisible(false);
                mainChatPanel.setVisible(true);
                this.historyRecord = new HistoryRecord(nickName);
                List<String> history = historyRecord.readHistory();
                for (String s : history) {
                    mainChatArea.appendText(s + System.lineSeparator());
                }
                break;
            case "ERROR:":
                showError(parsedMessage[1]);
                break;
            case "/list:":
                parsedMessage[0] = "ALL";
                ObservableList<String> list = FXCollections.observableArrayList(parsedMessage);
                contactList.setItems(list);
                contactList.getSelectionModel().select(0);
                break;
            case "/change_nick_ok" :
                changeNickPanel.setVisible(false);
                mainChatPanel.setVisible(true);
                break;
            case "/change_pass_ok" :
                changePasswordPanel.setVisible(false);
                mainChatPanel.setVisible(true);
                break;
            default:
                mainChatArea.appendText(parsedMessage[0] + System.lineSeparator());
                historyRecord.writeHistory(parsedMessage[0] + System.lineSeparator());
        }

    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(message);

        alert.showAndWait();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        List<String> contacts = Arrays.asList("Vasya", "Petya", "Masha", "Kolya", "Sergey");
        ObservableList<String> list = FXCollections.observableArrayList("ALL");
        contactList.setItems(list);
        contactList.getSelectionModel().select(0);
        this.chatMessageService = new ChatMessageService(this);
    }

    public void returnToChat(ActionEvent actionEvent) {
        changeNickPanel.setVisible(false);
        changePasswordPanel.setVisible(false);
        mainChatPanel.setVisible(true);
    }

    public void showChangeNick(ActionEvent actionEvent) {
        mainChatPanel.setVisible(false);
        changeNickPanel.setVisible(true);
    }

    public void showChangePass(ActionEvent actionEvent) {
        mainChatPanel.setVisible(false);
        changePasswordPanel.setVisible(true);
    }
}
