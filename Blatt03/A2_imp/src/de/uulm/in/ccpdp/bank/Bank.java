package de.uulm.in.ccpdp.bank;

import java.util.Arrays;

public class Bank {

    private Account[] accounts;

    public Bank() {
        accounts = new Account[100];
        Arrays.fill(accounts, new Account(0));
    }

    public String transfer(int from, int to, long amount) {
        String out = String.format("transferring %d from %s to %s",amount, from, to);
        accounts[from].transfer(-amount);
        accounts[to].transfer(amount);
        return out;
    }

    public boolean checkConsistency() {
        return Arrays.stream(accounts)
                .reduce(0l,
                        (Long sum, Account a) -> sum + a.getBalance(),
                        Long::sum) == 0l;
    }

    public static void main(String[] args) {
        Bank spassskasse = new Bank();
        BankWorker mueller = new BankWorker("Andreas Müller", spassskasse);
        BankWorker schmitt = new BankWorker("Petra Schmitt", spassskasse);
        BankWorker meyer = new BankWorker("Robert Meyer", spassskasse);
        BankWorker raschke = new BankWorker("Alexander Raschke", spassskasse);

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
