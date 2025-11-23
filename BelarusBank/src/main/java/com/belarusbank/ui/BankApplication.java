package com.belarusbank.ui;

import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import com.belarusbank.model.User;
import com.belarusbank.patterns.observer.TransactionObserver;
import com.belarusbank.service.BankService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.util.Optional;

public class BankApplication extends Application implements TransactionObserver {

    private BankService service;
    private TextArea logArea;
    private ListView<User> userList;
    private ListView<Account> accountList;
    private ComboBox<TransactionType> typeCombo;
    private ComboBox<Account> targetAccountCombo;
    private TextField amountField;
    private Button executeBtn;
    private Label auditLabel;
    private VBox targetBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        service = new BankService();
        service.addObserver(this);
        
        if(service.getAllUsers().isEmpty()) {
            service.createUser("Янка");
            service.createUser("Васіль");
            service.createUser("Алеся");
        }

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-base: #2b2b2b; -fx-font-family: 'Segoe UI';");

        VBox leftPane = new VBox(10);
        leftPane.setPrefWidth(220);
        userList = new ListView<>();
        userList.getItems().addAll(service.getAllUsers());
        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                accountList.getItems().setAll(newVal.getAccounts());
            }
        });
        
        Button addUserBtn = new Button("Новый клиент");
        addUserBtn.setMaxWidth(Double.MAX_VALUE);
        addUserBtn.setOnAction(e -> handleNewUser());

        leftPane.getChildren().addAll(new Label("Клиенты"), userList, addUserBtn);

        VBox centerPane = new VBox(10);
        accountList = new ListView<>();
        
        HBox opsBox = new HBox(10);
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(TransactionType.values());
        typeCombo.getSelectionModel().selectFirst();
        
        typeCombo.setOnAction(e -> updateTransactionUI());
        
        targetAccountCombo = new ComboBox<>();
        targetAccountCombo.setPromptText("Счет получателя");
        targetAccountCombo.setPrefWidth(200);
        
        targetBox = new VBox(5);
        targetBox.getChildren().addAll(new Label("Получатель:"), targetAccountCombo);
        targetBox.setVisible(false);
        targetBox.setManaged(false);

        amountField = new TextField();
        amountField.setPromptText("Сумма (BYN)");
        executeBtn = new Button("Выполнить");
        executeBtn.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white;");
        
        executeBtn.setOnAction(e -> handleTransaction());

        opsBox.getChildren().addAll(typeCombo, amountField, executeBtn);
        
        Button newAccBtn = new Button("Открыть счет");
        newAccBtn.setOnAction(e -> {
            User u = userList.getSelectionModel().getSelectedItem();
            if (u != null) {
                service.createAccount(u);
                refreshData();
                userList.getSelectionModel().select(u); 
            }
        });

        Button auditBtn = new Button("Глобальный Аудит");
        auditBtn.setOnAction(e -> auditLabel.setText(service.runAudit()));
        auditLabel = new Label("Ожидание аудита...");
        auditLabel.setStyle("-fx-text-fill: #8bc34a;");

        centerPane.getChildren().addAll(
            new Label("Счета пользователя"), 
            accountList, 
            newAccBtn, 
            new Separator(), 
            new Label("Операции"), 
            opsBox, 
            targetBox,
            new Separator(), 
            auditBtn, 
            auditLabel
        );

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(120);
        logArea.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: #00ff00;");

        root.setLeft(leftPane);
        root.setCenter(centerPane);
        root.setBottom(logArea);
        BorderPane.setMargin(centerPane, new Insets(0, 0, 0, 15));
        BorderPane.setMargin(logArea, new Insets(15, 0, 0, 0));

        Scene scene = new Scene(root, 900, 650);
        primaryStage.setTitle("Банковская Система Беларуси");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void handleNewUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Новый клиент");
        dialog.setHeaderText("Регистрация в системе");
        dialog.setContentText("Введите имя клиента:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                service.createUser(name);
                refreshData();
            }
        });
    }

    private void updateTransactionUI() {
        boolean isTransfer = typeCombo.getValue() == TransactionType.TRANSFER;
        targetBox.setVisible(isTransfer);
        targetBox.setManaged(isTransfer);
        if (isTransfer) {
            targetAccountCombo.getItems().setAll(service.getAllAccounts());
        }
    }

    private void handleTransaction() {
        Account selected = accountList.getSelectionModel().getSelectedItem();
        TransactionType type = typeCombo.getValue();
        String amountText = amountField.getText();

        if (selected == null) {
            log("Выберите исходный счет из списка!");
            return;
        }

        BigDecimal amount = BigDecimal.ZERO;
        if (type != TransactionType.FREEZE) {
             try {
                if (!amountText.isEmpty()) amount = new BigDecimal(amountText);
            } catch (NumberFormatException e) {
                log("Некорректная сумма!");
                return;
            }
        }

        Account target = null;
        if (type == TransactionType.TRANSFER) {
            target = targetAccountCombo.getValue();
            if (target == null) {
                log("Выберите счет получателя перевода!");
                return;
            }
            if (target.getId().equals(selected.getId())) {
                log("Нельзя перевести деньги на тот же счет!");
                return;
            }
        }

        service.processTransaction(type, selected, target, amount);
    }

    private void refreshData() {
        Platform.runLater(() -> {
            int selectedIdx = userList.getSelectionModel().getSelectedIndex();
            userList.getItems().setAll(service.getAllUsers());
            if (selectedIdx >= 0 && selectedIdx < userList.getItems().size()) {
                userList.getSelectionModel().select(selectedIdx);
                User u = userList.getSelectionModel().getSelectedItem();
                accountList.getItems().setAll(u.getAccounts());
            }
            if (typeCombo.getValue() == TransactionType.TRANSFER) {
                 targetAccountCombo.getItems().setAll(service.getAllAccounts());
            }
        });
    }

    private void log(String msg) {
        Platform.runLater(() -> logArea.appendText(msg + "\n"));
    }

    @Override
    public void onTransactionCompleted(String message) {
        log(message);
        refreshData();
    }
}
