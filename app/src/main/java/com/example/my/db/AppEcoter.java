package com.example.my.db;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppEcoter {
    private static AppEcoter instance;
    private final Executor mainIo;
    private final Executor sunIo;

    public AppEcoter(Executor mainIo, Executor sunIo) {
        this.mainIo = mainIo;
        this.sunIo = sunIo;
    }

    public static AppEcoter getInstance() {
        // создоет новую инстаию если ее нет
        if (instance == null)
            instance = new AppEcoter(new MainThreandler(), Executors.newSingleThreadExecutor());
        return instance;
    }

    public static class MainThreandler implements Executor {
        private Handler mainHendler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainHendler.post(command);
        }
    }

    public Executor getMainIo() {
        return mainIo;
    }

    public Executor getSunIo() {
        return sunIo;
    }
}
