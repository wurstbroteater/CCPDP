package de.uulm.in.ccpdp.bank;

/** Class that represents a clerk. */
public class BankWorker extends Thread {

  private static final int MAX_BETRAG = 1_000;
  private static final int MAX_KONTONR = 100;
  private static final int BUCHUNGEN = 100_000;
  private static final int CHECK_INTERVAL = 1_000;
  private Bank bank;

  /** public simple constructor. */
  public BankWorker(String name, Bank bank) {
    super(name);
    this.bank = bank;
    start();
  }

  @Override
  public void run() {
    // 100.000 Buchungen durchführen
    for (int i = 0; i < BUCHUNGEN; i++) {

      /*
      * Check consistency every CHECK_INTERVAL transactions
      */
      if (i % CHECK_INTERVAL == 0)
        if (!bank.checkConsistency())
          System.err.println("Consistency check failed!");

      /*
       * Kontonummer einlesen; simuliert durch Wahl einer Zufallszahl zwischen 0 und 99
       */
      int from = (int) (Math.random() * MAX_KONTONR);
      int to = (int) (Math.random() * MAX_KONTONR);

      /*
       * Überweisungsbetrag einlesen; simuliert durch Wahl einer Zufallszahl zwischen -500 und +499
       */
      long amount = (int) (Math.random() * MAX_BETRAG);

      bank.transfer(from, to, amount);
    }
  }
}
