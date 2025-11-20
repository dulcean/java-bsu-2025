package com.banking.system.gui;

import com.banking.system.database.*;
import com.banking.system.events.BankEventListener;
import com.banking.system.logic.AsyncExecutor;
import com.banking.system.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.UUID;

public class MainWindow extends JFrame implements BankEventListener {
    private final ClientRepo clientRepo;
    private final AccountRepo accountRepo;
    private final OperationRepo operationRepo;
    private final AsyncExecutor executor;

    private DefaultTableModel userModel, accModel, opModel;

    public MainWindow(ClientRepo cr, AccountRepo ar, OperationRepo or, AsyncExecutor exe) {
        this.clientRepo = cr;
        this.accountRepo = ar;
        this.operationRepo = or;
        this.executor = exe;

        setTitle("Банк 'Надежный'");
        setSize(950, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        refreshData();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();

        userModel = new DefaultTableModel(new String[]{"UUID", "Имя"}, 0);
        JTable userTable = new JTable(userModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JScrollPane(userTable));

        JPanel userButtonPanel = new JPanel();
        JButton addUserBtn = new JButton("Новый клиент");
        JButton showUserUUIDBtn = new JButton("Показать UUID");
        JButton delUserBtn = new JButton("Удалить клиента");

        addUserBtn.addActionListener(e -> createClient());
        showUserUUIDBtn.addActionListener(e -> showSelectedUUID(userTable, 0));
        delUserBtn.addActionListener(e -> deleteSelectedClient(userTable));

        userButtonPanel.add(addUserBtn);
        userButtonPanel.add(showUserUUIDBtn);
        userButtonPanel.add(delUserBtn);
        userPanel.add(userButtonPanel, BorderLayout.SOUTH);
        tabs.addTab("Клиенты", userPanel);

        accModel = new DefaultTableModel(new String[]{"UUID Счета", "UUID Владельца", "Баланс", "Блок"}, 0);
        JTable accTable = new JTable(accModel);
        accTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel accPanel = new JPanel(new BorderLayout());
        accPanel.add(new JScrollPane(accTable));

        JPanel accButtonPanel = new JPanel();
        JButton addAccBtn = new JButton("Открыть счет");
        JButton showAccUUIDBtn = new JButton("Показать UUID счета");
        JButton delAccBtn = new JButton("Удалить счет");

        addAccBtn.addActionListener(e -> createAccount());
        showAccUUIDBtn.addActionListener(e -> showSelectedUUID(accTable, 0));
        delAccBtn.addActionListener(e -> deleteSelectedAccount(accTable));

        accButtonPanel.add(addAccBtn);
        accButtonPanel.add(showAccUUIDBtn);
        accButtonPanel.add(delAccBtn);
        accPanel.add(accButtonPanel, BorderLayout.SOUTH);
        tabs.addTab("Счета", accPanel);

        opModel = new DefaultTableModel(new String[]{"UUID операции", "Тип", "Сумма", "Статус", "Ошибка", "Откуда", "Куда"}, 0);
        JTable opTable = new JTable(opModel);
        opTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabs.addTab("История", new JScrollPane(opTable));

        add(tabs, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton depBtn = new JButton("Пополнить");
        JButton withBtn = new JButton("Снять");
        JButton transBtn = new JButton("Перевод");
        JButton blkBtn = new JButton("Блок");
        JButton unblkBtn = new JButton("Разблок."); // Кнопка разблокировки

        depBtn.addActionListener(e -> doTransaction(Operation.Type.ADD_FUNDS));
        withBtn.addActionListener(e -> doTransaction(Operation.Type.CASH_OUT));
        transBtn.addActionListener(e -> doTransaction(Operation.Type.SEND_MONEY));
        blkBtn.addActionListener(e -> doTransaction(Operation.Type.BLOCK_ACC));
        unblkBtn.addActionListener(e -> doTransaction(Operation.Type.UNBLOCK_ACC));

        actions.add(depBtn);
        actions.add(withBtn);
        actions.add(transBtn);
        actions.add(blkBtn);
        actions.add(unblkBtn);
        add(actions, BorderLayout.SOUTH);
    }

    private void showSelectedUUID(JTable table, int uuidColumn) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String uuid = table.getValueAt(selectedRow, uuidColumn).toString();
            JOptionPane.showMessageDialog(this,
                    "UUID: " + uuid + "\nСкопируйте его для операций",
                    "UUID",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Выберите строку в таблице");
        }
    }

    private void deleteSelectedClient(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите клиента");
            return;
        }
        String uuidStr = table.getValueAt(row, 0).toString();
        int conf = JOptionPane.showConfirmDialog(this,
                "Удалить клиента? Все его счета тоже удалятся!", "Подтверждение", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            clientRepo.delete(UUID.fromString(uuidStr));
            refreshData();
        }
    }

    private void deleteSelectedAccount(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите счет");
            return;
        }
        String uuidStr = table.getValueAt(row, 0).toString();
        int conf = JOptionPane.showConfirmDialog(this,
                "Удалить этот счет?", "Подтверждение", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            accountRepo.delete(UUID.fromString(uuidStr));
            refreshData();
        }
    }

    private void createClient() {
        String name = JOptionPane.showInputDialog("Введите имя клиента:");
        if (name != null && !name.trim().isEmpty()) {
            Client c = new Client(name);
            clientRepo.create(c);
            refreshData();
            JOptionPane.showMessageDialog(this,
                    "Клиент создан!\nUUID: " + c.getUuid(), "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void createAccount() {
        String uidStr = JOptionPane.showInputDialog("UUID Клиента для нового счета:");
        if (uidStr == null || uidStr.trim().isEmpty()) return;

        try {
            UUID uid = UUID.fromString(uidStr.trim());
            if (clientRepo.exists(uid)) {
                BankAccount newAccount = new BankAccount(uid);
                accountRepo.create(newAccount);
                refreshData();
                JOptionPane.showMessageDialog(this,
                        "Счет создан!\nUUID счета: " + newAccount.getUuid(), "Успех", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Клиент не найден!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат UUID!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doTransaction(Operation.Type type) {
        try {
            double amount = 0.0;
            if (type != Operation.Type.BLOCK_ACC && type != Operation.Type.UNBLOCK_ACC) {
                String amountStr = JOptionPane.showInputDialog("Сумма операции:");
                if (amountStr == null) return;
                amount = Double.parseDouble(amountStr.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Сумма должна быть положительной");
                    return;
                }
            }

            UUID src = null, dst = null;

            if (type == Operation.Type.CASH_OUT || type == Operation.Type.SEND_MONEY ||
                    type == Operation.Type.BLOCK_ACC || type == Operation.Type.UNBLOCK_ACC) {

                String srcStr = JOptionPane.showInputDialog("UUID счета (источник/цель):");
                if (srcStr == null) return;
                src = UUID.fromString(srcStr.trim());

                if (!accountRepo.exists(src)) {
                    JOptionPane.showMessageDialog(this, "Счет не найден!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (type == Operation.Type.ADD_FUNDS || type == Operation.Type.SEND_MONEY) {
                String dstStr = JOptionPane.showInputDialog("UUID счета-получателя:");
                if (dstStr == null) return;
                dst = UUID.fromString(dstStr.trim());

                if (!accountRepo.exists(dst)) {
                    JOptionPane.showMessageDialog(this, "Счет-получатель не найден!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Operation op = null;
            switch (type) {
                case ADD_FUNDS -> op = Operation.createDeposit(dst, amount);
                case CASH_OUT -> op = Operation.createWithdraw(src, amount);
                case SEND_MONEY -> op = Operation.createTransfer(src, dst, amount);
                case BLOCK_ACC -> op = Operation.createBlock(src);
                case UNBLOCK_ACC -> op = Operation.createUnblock(src);
            }

            executor.submit(op);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат суммы! Введите число.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат UUID!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }

    private void refreshData() {
        SwingUtilities.invokeLater(() -> {
            userModel.setRowCount(0);
            clientRepo.getAll().forEach(c -> userModel.addRow(new Object[]{c.getUuid(), c.getUsername()}));

            accModel.setRowCount(0);
            accountRepo.getAll().forEach(a -> accModel.addRow(new Object[]{
                    a.getUuid(), a.getClientUuid(), a.getMoney(), a.isBlocked()}));

            opModel.setRowCount(0);
            operationRepo.getAll().forEach(o -> opModel.addRow(new Object[]{
                    o.getUuid(), o.getType(), o.getAmount(), o.getStatus(),
                    o.getError(), o.getFromAcc(), o.getToAcc()}));
        });
    }

    @Override
    public void onOperationFinished(Operation op) {
        SwingUtilities.invokeLater(() -> {
            refreshData();
            if (op.getStatus() == Operation.Status.FAIL) {
                JOptionPane.showMessageDialog(this,
                        "ОШИБКА операции:\n" + op.getError(),
                        "Неудача",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Операция выполнена успешно!",
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}