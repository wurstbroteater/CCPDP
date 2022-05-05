package de.uulm.in.ccpdp.bank;

import java.util.Arrays;

public class Bank {

    private Account[] accounts;

    public Bank() {
        accounts = new Account[100];
        Arrays.fill(accounts, new Account(0));
    }

    public synchronized void transfer(int from, int to, long amount) {
         accounts[from].transfer(-amount);
         accounts[to].transfer(amount);
    }

    public synchronized boolean checkConsistency() {
        return Arrays.stream(accounts)
                     .reduce(0l,
                             (Long sum, Account a) -> sum + a.getBalance(),
                             Long::sum) == 0l;
    }

    public static void main(String[] args) {
        Bank sparkasse = new Bank();
        BankWorker mueller = new BankWorker("Andreas Müller", sparkasse);
        BankWorker schmitt = new BankWorker("Petra Schmitt", sparkasse);
        BankWorker meyer   = new BankWorker("Robert Meyer", sparkasse);
        BankWorker raschke = new BankWorker("Alexander Raschke", sparkasse);

        try {
            mueller.join();
            schmitt.join();
            meyer.join();
            raschke.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
