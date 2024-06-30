package com.example.poskedai.model;

import java.util.List;

public class TransactionGroup {
    private String transactionId;
    private List<TransactionItem> transactions;

    public TransactionGroup(String transactionId, List<TransactionItem> transactions) {
        this.transactionId = transactionId;
        this.transactions = transactions;
    }

    public String gettransactionId() {
        return transactionId;
    }

    public List<TransactionItem> getTransactions() {
        return transactions;
    }
}
