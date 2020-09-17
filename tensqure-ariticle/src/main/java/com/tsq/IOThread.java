package com.tsq;

public class IOThread {

    public static void main(String[] args) {
        State state = new State();

        InThread inThread = new InThread(state);
        OutThread outThread = new OutThread(state);
        Thread in = new Thread(inThread);
        Thread out = new Thread(outThread);
        in.start();
        out.start();
    }
    static class State {
        public String flag;
    }

    static class InThread implements Runnable {
        private State state;

        public InThread(State state) {
            this.state = state;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (state) {
                    if ("in".equals(state.flag)) {
                        try {
                            System.out.println("im in");
                            state.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("go in");
                    state.flag="in";
                    state.notify();
                }
            }
        }
    }
    static class OutThread implements Runnable {
        private State state;

        public OutThread(State state) {
            this.state = state;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (state) {
                    if ("out".equals(state.flag)) {
                        try {
                            System.out.println("im out");
                            state.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("go out");
                    state.flag="out";
                    state.notify();
                }
            }
        }
    }
}
