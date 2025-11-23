package com.belarusbank.model;

public enum TransactionType {
    DEPOSIT("Пополнение"),
    WITHDRAW("Снятие"),
    TRANSFER("Перевод"),
    FREEZE("Заморозка/Разморозка");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
