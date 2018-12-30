package com.otus.alexshr.home.customviews;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;

import com.otus.alexshr.home.customviews.customviews.BuildConfig;

import timber.log.Timber;

/**
 * Created by alexshr on 20.12.2018.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @SuppressLint("DefaultLocale")
                @Override
                protected String createStackElementTag(@NonNull StackTraceElement element) {
                    return String.format("%s: %s:%d (%s)",
                            createTag(element),
                            element.getMethodName(),
                            element.getLineNumber(),
                            Thread.currentThread().getName());
                }

                private String createTag(@NonNull StackTraceElement element) {
                    return "timber:" + super.createStackElementTag(element);
                }
            });
        }
    }
}
