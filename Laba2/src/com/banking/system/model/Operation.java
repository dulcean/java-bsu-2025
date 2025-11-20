package com.banking.system.model;

import java.util.UUID;

public class Operation {
    public enum Type { ADD_FUNDS, CASH_OUT, SEND_MONEY, BLOCK_ACC, UNBLOCK_ACC }
    public enum Status { NEW, DONE, FAIL }

    private final UUID uuid;
    private final long timeCreated;
    private final Type type;
    private final double amount;
    private final UUID fromAcc;
    private final UUID toAcc;

    private Status status;
    private String error;

    public Operation(UUID uuid, long time, Type type, double amount, UUID from, UUID to, Status status, String error) {
        this.uuid = uuid;
        this.timeCreated = time;
        this.type = type;
        this.amount = amount;
        this.fromAcc = from;
        this.toAcc = to;
        this.status = status;
        this.error = error;
    }

    public static Operation createDeposit(UUID to, double amt) {
        return new Operation(UUID.randomUUID(), System.currentTimeMillis(), Type.ADD_FUNDS, amt, null, to, Status.NEW, null);
    }

    public static Operation createWithdraw(UUID from, double amt) {
        return new Operation(UUID.randomUUID(), System.currentTimeMillis(), Type.CASH_OUT, amt, from, null, Status.NEW, null);
    }

    public static Operation createTransfer(UUID from, UUID to, double amt) {
        return new Operation(UUID.randomUUID(), System.currentTimeMillis(), Type.SEND_MONEY, amt, from, to, Status.NEW, null);
    }

    public static Operation createBlock(UUID target) {
        return new Operation(UUID.randomUUID(), System.currentTimeMillis(), Type.BLOCK_ACC, 0, target, null, Status.NEW, null);
    }

    // НОВЫЙ метод для разблокировки
    public static Operation createUnblock(UUID target) {
        return new Operation(UUID.randomUUID(), System.currentTimeMillis(), Type.UNBLOCK_ACC, 0, target, null, Status.NEW, null);
    }

    public void success() { this.status = Status.DONE; }
    public void fail(String msg) { this.status = Status.FAIL; this.error = msg; }

    public UUID getUuid() { return uuid; }
    public Type getType() { return type; }
    public double getAmount() { return amount; }
    public UUID getFromAcc() { return fromAcc; }
    public UUID getToAcc() { return toAcc; }
    public Status getStatus() { return status; }
    public String getError() { return error; }
    public long getTimeCreated() { return timeCreated; }
}