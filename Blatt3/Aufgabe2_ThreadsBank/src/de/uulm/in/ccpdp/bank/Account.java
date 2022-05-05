package de.uulm.in.ccpdp.bank;

/** data class for an account. */
public class Account {
    public Account(long balance) {
        this.balance = balance;
    }

    private long balance;

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public void transfer(long amount) {
        balance += amount;
    }
}
