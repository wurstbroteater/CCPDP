package de.uulm.in.ccpdp.bank;

/**
 * data class for an account.
 */
public class Account {
    private Object lockSetBalance;
    private Object lockgetBalance;
    private Object lockTransfer;
    public Account(long balance) {
        this.balance = balance;
        this.lockSetBalance = new Object();
        this.lockgetBalance = new Object();
        this.lockTransfer  = new Object();
    }

    private long balance;

    public void setBalance(long balance) {
        synchronized (lockSetBalance) {
            this.balance = balance;
        }
    }

    public long getBalance() {
        synchronized (lockgetBalance) {
            return balance;
        }
    }

    public void transfer(long amount) {
        synchronized (lockTransfer) {
            balance += amount;
        }
    }
}
