package com.bank.game;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.ActionType;
import com.bank.service.BankService;
import com.bank.patterns.AuditVisitor;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BankInvokerGame extends Application {

    private final InvokerEngine engine = new InvokerEngine();
    private final BankService bankService = BankService.getInstance();
    private Account currentAccount;
    private Stage primaryStage;

    private Scene mainMenuScene;
    private Scene levelSelectScene;
    private Scene gameScene;

    private enum GameMode {
        STANDARD, PUZZLE
    }

    private GameMode currentMode = GameMode.STANDARD;
    private int currentLevelIndex = 0;
    private List<Level> allLevels;
    private List<Transaction> sessionHistory = new ArrayList<>();

    private HBox orbsContainer;
    private Label invokedSpellLabel;
    private Label balanceLabel;
    private Label statusLabel;
    private ListView<String> combatLog;
    private ObservableList<String> logData;
    private StackPane gameRoot;
    private ImageView portraitView;
    private VBox rightPanel;

    private Image gifIdle;
    private static final String PATH_QUAS = "/images/quas.png";
    private static final String PATH_WEX = "/images/wex.png";
    private static final String PATH_EXORT = "/images/exort.png";
    private static final String PATH_INVOKE = "/images/invoke.png";
    private static final String PATH_GIF_IDLE = "/images/first.gif";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        currentAccount = bankService.createDemoAccountIfNeeded();
        gifIdle = safeLoad(PATH_GIF_IDLE);
        initLevels();
        createMainMenu();
        primaryStage.setTitle("Invoker Bank");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private void createMainMenu() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #050505;");

        VBox center = new VBox(40);
        center.setAlignment(Pos.CENTER);

        Label title = new Label("INVOKER BANK");
        title.setFont(Font.font("Cinzel", FontWeight.BOLD, 64));
        title.setTextFill(Color.WHITE);
        title.setEffect(new Glow(0.8));

        Button btnStandard = new Button("STANDARD MODE");
        styleBigButton(btnStandard, Color.CYAN);
        btnStandard.setOnAction(e -> startStandardMode());

        Button btnPuzzle = new Button("PUZZLE MODE");
        styleBigButton(btnPuzzle, Color.ORANGE);
        btnPuzzle.setOnAction(e -> openLevelSelect());

        center.getChildren().addAll(title, btnStandard, btnPuzzle);
        root.setCenter(center);
        mainMenuScene = new Scene(root, 1250, 850);
    }

    private void startStandardMode() {
        currentMode = GameMode.STANDARD;
        currentAccount.setBalance(BigDecimal.ZERO);
        currentAccount.setFrozen(false);
        sessionHistory.clear();
        createGameScene("STANDARD MODE", "Full Arsenal Available");
        updateRightPanelForStandard();
        primaryStage.setScene(gameScene);
        logAction("System", "Session Started. Waiting for input...");
    }

    private void openLevelSelect() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #080808;");
        VBox center = new VBox(30);
        center.setAlignment(Pos.CENTER);
        Label title = new Label("SELECT LEVEL");
        title.setFont(Font.font("Cinzel", FontWeight.BOLD, 48));
        title.setTextFill(Color.WHITE);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        for (int i = 0; i < allLevels.size(); i++) {
            Level lvl = allLevels.get(i);
            Button btn = new Button(String.valueOf(i + 1));
            btn.setPrefSize(80, 80);
            styleLevelButton(btn);
            int index = i;
            btn.setOnAction(e -> startPuzzleLevel(index));
            VBox btnBox = new VBox(5, btn, createLabelSmall(lvl.name));
            btnBox.setAlignment(Pos.CENTER);
            grid.add(btnBox, i % 5, i / 5);
        }
        Button backBtn = new Button("BACK");
        styleButton(backBtn);
        backBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));
        center.getChildren().addAll(title, grid, backBtn);
        root.setCenter(center);
        levelSelectScene = new Scene(root, 1250, 850);
        primaryStage.setScene(levelSelectScene);
    }

    private void startPuzzleLevel(int index) {
        currentMode = GameMode.PUZZLE;
        currentLevelIndex = index;
        Level lvl = allLevels.get(index);
        currentAccount.setBalance(new BigDecimal(lvl.startBalance));
        currentAccount.setFrozen(false);
        sessionHistory.clear();
        createGameScene(lvl.name, "Target: " + lvl.targetBalance);
        updateRightPanelForPuzzle(lvl);
        primaryStage.setScene(gameScene);
    }

    private void createGameScene(String headerTitle, String subTitle) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0a0a;");

        VBox leftPanel = createSidePanel("CONTROLS");

        VBox keysBox = new VBox(15);
        keysBox.getChildren().addAll(
                createKeyImage("Q", "QUAS", Color.CYAN, PATH_QUAS),
                createKeyImage("W", "WEX", Color.MAGENTA, PATH_WEX),
                createKeyImage("E", "EXORT", Color.ORANGE, PATH_EXORT),
                createKeyImage("R", "INVOKE", Color.WHITE, PATH_INVOKE));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox systemBox = new VBox(10);
        systemBox.setPadding(new Insets(20, 0, 0, 0));
        Separator sep = new Separator();

        Button restartBtn = new Button("RESTART");
        styleButton(restartBtn);
        restartBtn.setOnAction(e -> {
            if (currentMode == GameMode.PUZZLE) {
                Level lvl = allLevels.get(currentLevelIndex);
                currentAccount.setBalance(new BigDecimal(lvl.startBalance));
            } else {
                currentAccount.setBalance(BigDecimal.ZERO);
            }
            currentAccount.setFrozen(false);
            sessionHistory.clear();
            refreshBalance();
            logAction("System", "Reset Performed.");
        });

        Button menuBtn = new Button("EXIT");
        styleButton(menuBtn);
        menuBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));

        systemBox.getChildren().addAll(sep, restartBtn, menuBtn);
        leftPanel.getChildren().addAll(keysBox, spacer, systemBox);

        rightPanel = createSidePanel("SPELL LIST");

        gameRoot = new StackPane();
        gameRoot.setStyle("-fx-background-color: #000000;");
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);

        Label lvlTitle = new Label(headerTitle);
        lvlTitle.setTextFill(Color.WHITE);
        lvlTitle.setFont(Font.font("Cinzel", FontWeight.BOLD, 36));
        lvlTitle.setEffect(new Glow(0.6));
        Label lvlSub = new Label(subTitle);
        lvlSub.setTextFill(Color.GOLD);
        lvlSub.setFont(Font.font("Monospace", FontWeight.BOLD, 20));

        StackPane portrait = createPortraitFrame();

        balanceLabel = new Label("Balance: " + currentAccount.getBalance());
        balanceLabel.setTextFill(Color.GOLD);
        balanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        statusLabel = new Label("");
        statusLabel.setTextFill(Color.CYAN);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        statusLabel.setEffect(new Glow(0.8));

        VBox balanceBox = new VBox(5, balanceLabel, statusLabel);
        balanceBox.setAlignment(Pos.CENTER);

        orbsContainer = new HBox(10);
        orbsContainer.setAlignment(Pos.CENTER);
        orbsContainer.setPrefHeight(60);
        orbsContainer.setTranslateY(-15);
        updateOrbsDisplay();
        invokedSpellLabel = new Label("Ready");
        invokedSpellLabel.setTextFill(Color.LIGHTGRAY);
        invokedSpellLabel.setFont(Font.font("Arial", 20));

        logData = FXCollections.observableArrayList();
        combatLog = new ListView<>(logData);
        combatLog.setMaxHeight(120);
        combatLog.setMaxWidth(500);
        combatLog.setStyle(
                "-fx-background-color: #111; -fx-control-inner-background: #111; -fx-border-color: #333; -fx-border-width: 1;");
        combatLog.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setTextFill(Color.web("#AAAAAA"));
                    setFont(Font.font("Consolas", 12));
                    if (item.contains("VICTORY"))
                        setTextFill(Color.LIME);
                    if (item.contains("Error"))
                        setTextFill(Color.SALMON);
                    if (item.contains("FROZEN"))
                        setTextFill(Color.CYAN);
                }
            }
        });

        contentBox.getChildren().addAll(lvlTitle, lvlSub, portrait, balanceBox, orbsContainer,
                combatLog);
        gameRoot.getChildren().add(contentBox);

        root.setLeft(leftPanel);
        root.setCenter(gameRoot);
        root.setRight(rightPanel);
        gameScene = new Scene(root, 1250, 850);
        gameScene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            String text = e.getText().toLowerCase();
            if (code == KeyCode.Q || text.equals("й")) {
                addOrb(InvokerEngine.Orb.QUAS);
            } else if (code == KeyCode.W || text.equals("ц")) {
                addOrb(InvokerEngine.Orb.WEX);
            } else if (code == KeyCode.E || text.equals("у")) {
                addOrb(InvokerEngine.Orb.EXORT);
            } else if (code == KeyCode.R || text.equals("к")) {
                performInvoke();
            }
        });

        refreshBalance();
    }

    private void updateRightPanelForStandard() {
        rightPanel.getChildren().clear();
        rightPanel.getChildren().add(createLabelSmall("--- SPELL LIST ---"));

        rightPanel.getChildren().addAll(
                createSpellInfo("QQQ", "Cold Freez", "Freeze/Unfreeze Acc", Color.CYAN),
                createSpellInfo("QQW", "Ghost Audit", "Audit (Log Balance)", Color.LIGHTBLUE),
                createSpellInfo("QQE", "Secure Wall", "Set Withdraw Limit", Color.CYAN),
                createSpellInfo("WWW", "EMP Transfer", "Transfer Funds", Color.MAGENTA),
                createSpellInfo("WWQ", "Fast Withdraw", "Withdraw (Small)", Color.VIOLET),
                createSpellInfo("WWE", "Speed Deposit", "Deposit (Small)", Color.PINK),
                createSpellInfo("EEE", "Sun Deposit", "Deposit (Large)", Color.ORANGE),
                createSpellInfo("EEQ", "Forge Account", "Create New Account", Color.ORANGE),
                createSpellInfo("EEW", "Credit Meteor", "Request Loan", Color.RED),
                createSpellInfo("QWE", "Deaf. Report", "Visitor Report", Color.WHITE));
    }

    private void updateRightPanelForPuzzle(Level lvl) {
        rightPanel.getChildren().clear();
        rightPanel.getChildren().add(createLabelSmall("--- AVAILABLE SPELLS ---"));
        for (Map.Entry<String, String> entry : lvl.spells.entrySet()) {
            Color c = Color.WHITE;
            if (entry.getKey().contains("E"))
                c = Color.ORANGE;
            if (entry.getKey().contains("W"))
                c = Color.MAGENTA;
            if (entry.getKey().contains("Q"))
                c = Color.CYAN;
            rightPanel.getChildren().add(createSpellInfo(entry.getKey(), entry.getValue(), "", c));
        }
        Label hint = new Label("GOAL: " + lvl.targetBalance);
        hint.setTextFill(Color.LIME);
        hint.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        hint.setPadding(new Insets(20, 0, 0, 0));
        rightPanel.getChildren().add(hint);
    }

    private void addOrb(InvokerEngine.Orb orb) {
        engine.addOrb(orb);
        updateOrbsDisplay();
    }

    private void performInvoke() {
        List<InvokerEngine.Orb> orbs = engine.getCurrentOrbs();
        long q = orbs.stream().filter(o -> o == InvokerEngine.Orb.QUAS).count();
        long w = orbs.stream().filter(o -> o == InvokerEngine.Orb.WEX).count();
        long e = orbs.stream().filter(o -> o == InvokerEngine.Orb.EXORT).count();
        String combo = getComboKey(q, w, e);

        if (currentMode == GameMode.STANDARD)
            executeStandardMode(combo);
        else
            executePuzzleMode(combo);
    }

    private String getComboKey(long q, long w, long e) {
        if (q == 3)
            return "QQQ";
        if (w == 3)
            return "WWW";
        if (e == 3)
            return "EEE";
        if (q == 2 && w == 1)
            return "QQW";
        if (q == 2 && e == 1)
            return "QQE";
        if (w == 2 && q == 1)
            return "WWQ";
        if (w == 2 && e == 1)
            return "WWE";
        if (e == 2 && q == 1)
            return "EEQ";
        if (e == 2 && w == 1)
            return "EEW";
        if (q == 1 && w == 1 && e == 1)
            return "QWE";
        return "UNKNOWN";
    }

    private void executeStandardMode(String combo) {
        Transaction tx = null;
        String name = combo;
        Color color = Color.WHITE;
        switch (combo) {
            case "EEE":
                tx = new Transaction(ActionType.DEPOSIT, new BigDecimal(10000), currentAccount.getId(), null);
                name = "SUN STRIKE";
                color = Color.ORANGE;
                break;
            case "EEW":
                tx = new Transaction(ActionType.DEPOSIT, new BigDecimal(5000), currentAccount.getId(), null);
                name = "METEOR LOAN";
                color = Color.RED;
                break;
            case "EEQ":
                logAction("Factory", "New Account Strategy Executed");
                showFloatingText("FORGE SPIRIT", Color.ORANGE);
                return;
            case "WWE":
                tx = new Transaction(ActionType.DEPOSIT, new BigDecimal(100), currentAccount.getId(), null);
                name = "ALACRITY";
                color = Color.PINK;
                break;
            case "WWQ":
                tx = new Transaction(ActionType.WITHDRAW, new BigDecimal(100), currentAccount.getId(), null);
                name = "TORNADO";
                color = Color.VIOLET;
                break;
            case "WWW":
                tx = new Transaction(ActionType.TRANSFER, new BigDecimal(50), currentAccount.getId(),
                        currentAccount.getId());
                name = "EMP TRANSFER";
                color = Color.MAGENTA;
                break;

            case "QWE":
                if (sessionHistory.isEmpty())
                    logAction("Visitor", "No history to report.");
                else {
                    try {
                        AuditVisitor visitor = new AuditVisitor();
                        for (Transaction t : sessionHistory)
                            t.accept(visitor);
                        logAction("VISITOR", visitor.getReport().replace("\n", " | "));
                    } catch (Exception ex) {
                        logAction("Visitor", "Report Generated.");
                    }
                }
                showFloatingText("DEAFENING REPORT", Color.WHITE);
                return;

            case "QQQ":
                tx = new Transaction(ActionType.FREEZE, null, currentAccount.getId(), null);
                name = "COLD SNAP";
                color = Color.CYAN;
                break;

            case "QQW":
                logAction("Observer", "Audit: Bal=" + currentAccount.getBalance());
                showFloatingText("GHOST WALK", Color.LIGHTBLUE);
                return;
            case "QQE":
                logAction("Security", "Withdraw Limits Updated.");
                showFloatingText("ICE WALL", Color.CYAN);
                return;
            default:
                name = "FIZZLE";
                color = Color.GRAY;
        }
        processTx(tx, name, color);
    }

    private void executePuzzleMode(String combo) {
        Level lvl = allLevels.get(currentLevelIndex);
        if (!lvl.spells.containsKey(combo)) {
            showFloatingText("RESTRICTED!", Color.GRAY);
            return;
        }

        long bal = currentAccount.getBalance().longValue();
        long next = bal;
        switch (currentLevelIndex) {
            case 0:
                if (combo.equals("EEE"))
                    next += 5;
                if (combo.equals("WWE"))
                    next *= 2;
                if (combo.equals("EEW"))
                    next = reverse(bal);
                break;
            case 1:
                if (combo.equals("QQQ")) {
                    if (bal % 2 == 0)
                        next /= 2;
                }
                if (combo.equals("EEE"))
                    next += 3;
                if (combo.equals("QQW"))
                    next = sumDigits(bal);
                break;
            case 2:
                if (combo.equals("QQQ")) {
                    if (bal % 2 == 0)
                        next /= 2;
                }
                if (combo.equals("EEE"))
                    next += 4;
                if (combo.equals("EEQ"))
                    next = Long.parseLong(bal + "1");
                break;
            case 3:
                if (combo.equals("EEE"))
                    next += 7;
                if (combo.equals("WWE"))
                    next *= 3;
                if (combo.equals("WWQ"))
                    next /= 10;
                break;
            case 4:
                if (combo.equals("EEE"))
                    next += 9;
                if (combo.equals("QQQ")) {
                    if (bal % 9 == 0)
                        next /= 9;
                }
                if (combo.equals("EEW"))
                    next = reverse(bal);
                break;
            case 5:
                if (combo.equals("WWW"))
                    next = bal * bal;
                if (combo.equals("WWQ"))
                    next -= 1;
                if (combo.equals("QQQ")) {
                    if (bal % 2 == 0)
                        next /= 2;
                }
                break;
            case 6:
                if (combo.equals("EEE"))
                    next += 1;
                if (combo.equals("WWE"))
                    next *= 2;
                if (combo.equals("EEW"))
                    next = reverse(bal);
                break;
            case 7:
                if (combo.equals("WWE"))
                    next *= 4;
                if (combo.equals("EEE"))
                    next += 6;
                if (combo.equals("WWQ"))
                    next /= 10;
                break;
            case 8:
                if (combo.equals("EEE"))
                    next += 3;
                if (combo.equals("WWE"))
                    next *= 4;
                if (combo.equals("EEW"))
                    next = reverse(bal);
                break;
            case 9:
                if (combo.equals("WWE"))
                    next *= 2;
                if (combo.equals("QQQ")) {
                    if (bal % 2 == 0)
                        next /= 2;
                }
                if (combo.equals("QQW"))
                    next += sumDigits(bal);
                break;
        }

        long diff = next - bal;
        Transaction tx = null;
        if (diff >= 0)
            tx = new Transaction(ActionType.DEPOSIT, new BigDecimal(diff), currentAccount.getId(), null);
        else
            tx = new Transaction(ActionType.WITHDRAW, new BigDecimal(Math.abs(diff)), currentAccount.getId(), null);

        Color c = Color.WHITE;
        if (combo.contains("E"))
            c = Color.ORANGE;
        if (combo.contains("W"))
            c = Color.MAGENTA;
        if (combo.contains("Q"))
            c = Color.CYAN;
        processTx(tx, lvl.spells.get(combo), c);
    }

    private long reverse(long n) {
        long r = 0;
        while (n != 0) {
            r = r * 10 + n % 10;
            n /= 10;
        }
        return r;
    }

    private long sumDigits(long n) {
        long s = 0;
        n = Math.abs(n);
        while (n > 0) {
            s += n % 10;
            n /= 10;
        }
        return s;
    }

    private void processTx(Transaction tx, String name, Color color) {
        if (tx != null) {
            sessionHistory.add(tx);
            flashText(invokedSpellLabel);
            bankService.processTransaction(tx).thenRun(() -> Platform.runLater(() -> {
                refreshBalance();

                if (name.equals("COLD SNAP")) {
                    String status = currentAccount.isFrozen() ? "FROZEN" : "UNFROZEN";
                    logAction("Status", "Account is now " + status);
                } else {
                    logAction("Success", name + " executed.");
                }

                if (currentMode == GameMode.PUZZLE)
                    checkPuzzleWin();
                showFloatingText(name, color);
            })).exceptionally(ex -> {
                Platform.runLater(() -> {
                    showFloatingText("FAILED", Color.RED);
                    logAction("Error", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                });
                return null;
            });
        }
    }

    private void checkPuzzleWin() {
        Level lvl = allLevels.get(currentLevelIndex);
        if (currentAccount.getBalance().compareTo(new BigDecimal(lvl.targetBalance)) == 0) {
            showFloatingText("VICTORY!", Color.LIME);
            Timeline tm = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), e -> primaryStage.setScene(levelSelectScene)));
            tm.play();
        }
    }

    private void initLevels() {
        allLevels = new ArrayList<>();
        Map<String, String> l1 = new LinkedHashMap<>();
        l1.put("EEE", "Add 5");
        l1.put("WWE", "Multiply 2");
        l1.put("EEW", "Reverse Digits");
        allLevels.add(new Level("1. Mirror Start", 1, 52, l1));
        Map<String, String> l2 = new LinkedHashMap<>();
        l2.put("QQQ", "Div 2 (if even)");
        l2.put("EEE", "Add 3");
        l2.put("QQW", "Sum Digits");
        allLevels.add(new Level("2. Digital Diet", 88, 1, l2));
        Map<String, String> l3 = new LinkedHashMap<>();
        l3.put("QQQ", "Div 2 (if even)");
        l3.put("EEE", "Add 4");
        l3.put("EEQ", "Append '1'");
        allLevels.add(new Level("3. Sticky One", 4, 25, l3));
        Map<String, String> l4 = new LinkedHashMap<>();
        l4.put("EEE", "Add 7");
        l4.put("WWE", "Multiply 3");
        l4.put("WWQ", "Del Last Digit");
        allLevels.add(new Level("4. Butterfly", 0, 30, l4));
        Map<String, String> l5 = new LinkedHashMap<>();
        l5.put("EEE", "Add 9");
        l5.put("QQQ", "Div 9 (if even)");
        l5.put("EEW", "Reverse");
        allLevels.add(new Level("5. Collapse", 99, 2, l5));
        Map<String, String> l6 = new LinkedHashMap<>();
        l6.put("WWW", "Square (x^2)");
        l6.put("WWQ", "Sub 1");
        l6.put("QQQ", "Div 2 (if even)");
        allLevels.add(new Level("6. Step Back", 3, 20, l6));
        Map<String, String> l7 = new LinkedHashMap<>();
        l7.put("EEE", "Add 1");
        l7.put("WWE", "Multiply 2");
        l7.put("EEW", "Reverse");
        allLevels.add(new Level("7. Binary Hacker", 0, 53, l7));
        Map<String, String> l8 = new LinkedHashMap<>();
        l8.put("WWE", "Multiply 4");
        l8.put("EEE", "Add 6");
        l8.put("WWQ", "Del Last Digit");
        allLevels.add(new Level("8. Truncation", 5, 14, l8));
        Map<String, String> l9 = new LinkedHashMap<>();
        l9.put("EEE", "Add 3");
        l9.put("WWE", "Multiply 4");
        l9.put("EEW", "Reverse");
        allLevels.add(new Level("9. Odd Path", 2, 41, l9));
        Map<String, String> l10 = new LinkedHashMap<>();
        l10.put("WWE", "Multiply 2");
        l10.put("QQQ", "Div 2 (if even)");
        l10.put("QQW", "Add Sum Digits");
        allLevels.add(new Level("10. Sum Power", 5, 26, l10));
    }

    private static class Level {
        String name;
        int startBalance;
        int targetBalance;
        Map<String, String> spells;

        Level(String n, int s, int t, Map<String, String> sp) {
            name = n;
            startBalance = s;
            targetBalance = t;
            spells = sp;
        }
    }

    private StackPane createPortraitFrame() {
        StackPane frame = new StackPane();
        frame.setPrefSize(350, 400);
        portraitView = new ImageView();
        portraitView.setFitHeight(350);
        portraitView.setPreserveRatio(true);
        if (gifIdle != null)
            portraitView.setImage(gifIdle);
        frame.getChildren().add(portraitView);
        return frame;
    }

    private void showFloatingText(String text, Color color) {
        Label floating = new Label(text);
        floating.setFont(Font.font("Impact", 28));
        floating.setTextFill(color);
        floating.setEffect(new DropShadow(3, Color.BLACK));
        gameRoot.getChildren().add(floating);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.0), floating);
        tt.setByY(-150);
        FadeTransition ft = new FadeTransition(Duration.seconds(1.0), floating);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(e -> gameRoot.getChildren().remove(floating));
        pt.play();
    }

    private void refreshBalance() {
        balanceLabel.setText("Balance: " + currentAccount.getBalance());
        if (currentAccount.isFrozen()) {
            balanceLabel.setTextFill(Color.CYAN);
            statusLabel.setText("[FROZEN]");
        } else {
            balanceLabel.setTextFill(Color.GOLD);
            statusLabel.setText("");
        }
    }

    private void logAction(String src, String msg) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logData.add(0, String.format("[%s] %s: %s", time, src, msg));
    }

    private void updateOrbsDisplay() {
        orbsContainer.getChildren().clear();
        List<InvokerEngine.Orb> orbs = engine.getCurrentOrbs();
        for (int i = 0; i < 3; i++) {
            StackPane stack = new StackPane();
            Circle bg = new Circle(25);
            bg.setFill(Color.TRANSPARENT);
            bg.setStroke(Color.GRAY);
            if (i < orbs.size()) {
                String path = switch (orbs.get(i)) {
                    case QUAS -> PATH_QUAS;
                    case WEX -> PATH_WEX;
                    case EXORT -> PATH_EXORT;
                };
                Color col = switch (orbs.get(i)) {
                    case QUAS -> Color.CYAN;
                    case WEX -> Color.MAGENTA;
                    case EXORT -> Color.ORANGE;
                };
                Image img = safeLoad(path);
                if (img != null) {
                    ImageView iv = new ImageView(img);
                    iv.setFitHeight(50);
                    iv.setFitWidth(50);
                    iv.setClip(new Circle(25, 25, 25));
                    iv.setEffect(new DropShadow(15, col));
                    stack.getChildren().addAll(bg, iv);
                } else
                    stack.getChildren().add(bg);
            } else
                stack.getChildren().add(bg);
            orbsContainer.getChildren().add(stack);
        }
    }

    private VBox createSidePanel(String title) {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setPrefWidth(320);
        box.setStyle("-fx-background-color: #151515; -fx-border-color: #333; -fx-border-width: 0 2 0 0;");
        Label l = new Label(title);
        l.setTextFill(Color.WHITE);
        l.setFont(Font.font("Cinzel", FontWeight.BOLD, 22));
        box.getChildren().add(l);
        return box;
    }

    private void styleLevelButton(Button b) {
        b.setStyle(
                "-fx-background-color: #222; -fx-text-fill: #00BFFF; -fx-font-size: 24px; -fx-border-color: #00BFFF; -fx-border-radius: 10;");
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: #00BFFF; -fx-text-fill: #000; -fx-font-size: 24px; -fx-border-color: #FFF; -fx-border-radius: 10; -fx-cursor: hand;"));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: #222; -fx-text-fill: #00BFFF; -fx-font-size: 24px; -fx-border-color: #00BFFF; -fx-border-radius: 10;"));
    }

    private void styleBigButton(Button b, Color glow) {
        String hex = String.format("#%02X%02X%02X", (int) (glow.getRed() * 255), (int) (glow.getGreen() * 255),
                (int) (glow.getBlue() * 255));
        b.setPrefWidth(500);
        b.setPrefHeight(70);
        b.setFont(Font.font("Cinzel", FontWeight.BOLD, 24));
        b.setStyle("-fx-background-color: #111; -fx-text-fill: white; -fx-border-color: " + hex
                + "; -fx-border-width: 2;");
        b.setOnMouseEntered(e -> {
            b.setStyle("-fx-background-color: " + hex
                    + "; -fx-text-fill: black; -fx-border-color: white; -fx-border-width: 2;");
            b.setEffect(new Glow(0.5));
        });
        b.setOnMouseExited(e -> {
            b.setStyle("-fx-background-color: #111; -fx-text-fill: white; -fx-border-color: " + hex
                    + "; -fx-border-width: 2;");
            b.setEffect(null);
        });
    }

    private void styleButton(Button b) {
        b.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: #555;");
        b.setOnMouseEntered(
                e -> b.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-border-color: #777;"));
        b.setOnMouseExited(
                e -> b.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: #555;"));
    }

    private HBox createKeyImage(String key, String name, Color color, String path) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        StackPane imgBox = new StackPane();
        Image img = safeLoad(path);
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(40);
            iv.setFitHeight(40);
            iv.setEffect(new DropShadow(10, color));
            imgBox.getChildren().add(iv);
        } else
            imgBox.getChildren().add(new Rectangle(40, 40, color));
        Label k = new Label("[" + key + "]");
        k.setTextFill(color);
        k.setFont(Font.font(20));
        Label n = new Label(name);
        n.setTextFill(Color.LIGHTGRAY);
        row.getChildren().addAll(k, imgBox, n);
        return row;
    }

    private HBox createSpellInfo(String combo, String name, String desc, Color c) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        Label cmb = new Label(combo);
        cmb.setTextFill(c);
        cmb.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        cmb.setPrefWidth(40);
        Label nm = new Label(name);
        nm.setTextFill(Color.WHITE);
        nm.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nm.setPrefWidth(90);
        Label ds = new Label(desc);
        ds.setTextFill(Color.GRAY);
        ds.setFont(Font.font("Arial", 11));
        row.getChildren().addAll(cmb, nm, ds);
        return row;
    }

    private Label createLabelSmall(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.GRAY);
        l.setFont(Font.font(12));
        return l;
    }

    private void flashText(Label label) {
        FadeTransition ft = new FadeTransition(Duration.millis(100), label);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
        label.setEffect(new Glow(0.8));
    }

    private Image safeLoad(String path) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            return null;
        }
    }
}
