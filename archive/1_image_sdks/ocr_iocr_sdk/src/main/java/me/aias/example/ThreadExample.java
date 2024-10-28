package me.aias.example;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadExample {

    private ThreadExample() {
    }

    public static void main(String[] args){
        ExecutorService threadPool = Executors.newFixedThreadPool(3); // 3是线程池的大小
        for (int i = 0; i < 10; i++) {
            threadPool.execute(new Runnable() {
                public void run() {
                    // 这里是需要异步执行的代码
                    System.out.println("");
                }
            });
        }
        threadPool.shutdown(); // 当所有任务执行完毕后关闭线程池

    }

}
