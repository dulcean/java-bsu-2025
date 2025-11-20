package com.banking.system.logic;

import com.banking.system.database.AccountRepo;
import com.banking.system.database.OperationRepo;
import com.banking.system.events.NotificationSystem;
import com.banking.system.logic.actions.*;
import com.banking.system.model.Operation;

import java.util.HashMap;
import java.util.Map;

public class BankingService {
    private final AccountRepo accRepo;
    private final OperationRepo opRepo;
    private final NotificationSystem notes;
    private final Map<Operation.Type, IBankAction> strategies = new HashMap<>();

    public BankingService(AccountRepo ar, OperationRepo or, NotificationSystem ns) {
        this.accRepo = ar;
        this.opRepo = or;
        this.notes = ns;

        strategies.put(Operation.Type.ADD_FUNDS, new DepositAction());
        strategies.put(Operation.Type.CASH_OUT, new WithdrawAction());
        strategies.put(Operation.Type.SEND_MONEY, new TransferAction());
        strategies.put(Operation.Type.BLOCK_ACC, new BlockAction());
        strategies.put(Operation.Type.UNBLOCK_ACC, new UnblockAction());
    }

    public void runOperation(Operation op) {
        try {
            if (op.getFromAcc() != null && !accRepo.exists(op.getFromAcc())) {
                throw new Exception("Счет-отправитель не найден: " + op.getFromAcc());
            }
            if (op.getToAcc() != null && !accRepo.exists(op.getToAcc())) {
                throw new Exception("Счет-получатель не найден: " + op.getToAcc());
            }

            IBankAction action = strategies.get(op.getType());
            if (action == null) throw new Exception("Неизвестная операция");

            action.perform(op, accRepo);
            op.success();
        } catch (Exception e) {
            op.fail(e.getMessage());
        } finally {
            opRepo.saveOrUpdate(op);
            notes.notifyAll(op);
        }
    }
}