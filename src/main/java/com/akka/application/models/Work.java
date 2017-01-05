package com.akka.application.models;

public class Work {

    private final int start;
    private final int numberOfElements;

    public Work(int start, int numberOfElements) {
        this.start = start;
        this.numberOfElements = numberOfElements;
    }

    public int getStart() {
        return start;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

}
