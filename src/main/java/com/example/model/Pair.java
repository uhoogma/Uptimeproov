package com.example.model;

/**
 * Wrapper class for data transfer
 */
public class Pair {

    private String amount;
    private String currencyCode;

    public Pair() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "Pair [amount=" + amount + ", currencyCode=" + currencyCode + "]";
    }

}
