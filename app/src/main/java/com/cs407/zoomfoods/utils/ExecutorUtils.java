package com.cs407.zoomfoods.utils;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtils {
    public static final ExecutorService execService = Executors.newFixedThreadPool(10);
    public static final ListeningExecutorService DEFAULT_EXECUTOR_SERVICE = MoreExecutors.listeningDecorator(execService);

}
