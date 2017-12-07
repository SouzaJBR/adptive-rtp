package br.ufjf.dcc082.Client;

public class ErrorCounter {

    private Object key = new Object();
    private volatile int errorCount = 0;
    private int goodThreshold;
    private int badThreshold;
    private int goodCount;
    private int badCount;
    private int minToGood;
    private int minToBad;

    public ErrorCounter(int goodThreshold, int badThreshold, int minToGood, int minToBad) {
        this.goodThreshold = goodThreshold;
        this.badThreshold = badThreshold;
        this.minToGood = minToGood;
        this.minToBad = minToBad;
        this.goodCount = 0;
        this.badCount = 0;
    }

    public int checkThreshold()
    {
        synchronized (key) {
            int currentErrorCount = this.errorCount;

            System.out.println("Nos últimos 30s, " + currentErrorCount + " erros");

            this.errorCount = 0;

            if(currentErrorCount >= badThreshold) {
                this.badCount++;

                if(this.badCount >= this.minToBad) {
                    this.badCount = 0;
                    return -1;
                }
            }

            if(currentErrorCount <= goodThreshold)
                goodCount++;
                if(this.goodCount >= this.minToGood) {
                    this.goodCount = 0;
                    return 1;
                }

            return 0;
        }
    }

    public void addError() {
        synchronized (key) {
            errorCount++;
        }
    }

    public int getAndResetErrorCount() {
        synchronized (key) {
            int oldErrorCount = errorCount;
            errorCount = 0;
            return oldErrorCount;
        }
    }
}
