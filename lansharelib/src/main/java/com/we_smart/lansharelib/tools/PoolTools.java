package com.we_smart.lansharelib.tools;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by zhaol on 2018/1/15.
 */

public class PoolTools {
    public static Executor MAIN_EXECUTOR = Executors.newFixedThreadPool(5);
    private final static String TAG = PoolTools.class.getSimpleName();

    public static final int
            TASK_POOL_MODE_ASYNC_RANDOM = 0,
            TASK_POOL_MODE_ASYNC_SEQUENTIAL = 1,

    TASK_POOL_MODE_DEFAULT = TASK_POOL_MODE_ASYNC_RANDOM,
            MAX_TASK_POOL_MODE = 2;

    public PoolTools() {
    }

    public PoolTools(int mode) {
        poolMode = mode;
    }

    private static final PoolTools defRandPool = new PoolTools(TASK_POOL_MODE_ASYNC_RANDOM);
    private static final PoolTools defSeqPool = new PoolTools(TASK_POOL_MODE_ASYNC_SEQUENTIAL);

    public static PoolTools DefRandTaskPool() {
        return defRandPool;
    }

    public static PoolTools DefSeqTaskPool() {
        return defSeqPool;
    }

    public static PoolTools DefTaskPool() {
        return defRandPool;
    }

    private static final Runnable seqLoopRun = new Runnable() {
        public void run() {
            Looper.prepare();
            // seqLoop = Looper.myLooper();
            seqLoopHdlr = new Handler();        // Bind handle to current-running-thread's looper.

            synchronized (seqLoopThread) {
                threadInitSucc = true;
                Log.i(TAG, "Thread init complete!");
                seqLoopThread.notifyAll();
            }

            while (true) {
                try {
                    Looper.loop();
                } catch (Throwable e) {
                    Log.e(TAG, "SeqLoop execute failed with exception(" + e.getClass() + "): " + e.getLocalizedMessage());

                }
            }
        }
    };

    private static boolean threadInitSucc = false;
    private static Thread seqLoopThread = new Thread(seqLoopRun, "TaskPool's seqLook task.");
    private static Handler seqLoopHdlr = null;
    private static ThreadPoolExecutor randLoop = (ThreadPoolExecutor) (AsyncTask.THREAD_POOL_EXECUTOR);
    private static int randLoopPurgeGap = 0;

    static {
        seqLoopThread.start();
    }

    private static Handler seqLoopHandler() {
        if (seqLoopHdlr == null) {
            synchronized (seqLoopThread) {
                try {
                    while (!threadInitSucc) {
                        Log.i(TAG, "Thread init not complete, waiting!");
                        seqLoopThread.wait();
                    }
                } catch (InterruptedException e) {
                    Log.d(TAG, e.toString());
                }
            }
            ;
        }

        return seqLoopHdlr;
    }

    private void LogNullTask() {
        Log.e(TAG, "Pushed a null task!");
        new Throwable().printStackTrace();
    }

    public boolean PushTask(final Runnable t, long delay) {
        if (t != null)
            switch (poolMode) {
                case TASK_POOL_MODE_ASYNC_RANDOM:
                    return seqLoopHandler().postDelayed(new Runnable() {
                        public void run() {
                            PushTask(t);
                        }
                    }, delay);
                // break;
                case TASK_POOL_MODE_ASYNC_SEQUENTIAL:
                    return seqLoopHandler().postDelayed(t, delay);
            }
        else
            LogNullTask();

        return false;
    }

    public final boolean RandExec(final Runnable r) {
        try {
            randLoop.execute(new Runnable() {
                public void run() {
                    try {
                        r.run();
                        randLoop.remove(this);
                    } catch (Throwable e) {
                        Log.e(TAG, "RandLoop execute failed with exception(" + e.getClass() + "): " + e.getLocalizedMessage());

                    }
                }
            });

            if (randLoopPurgeGap++ % 128 == 0) randLoop.purge();

            return true;
        } catch (Throwable e) {
            Log.e(TAG, "TaskPool rand loop push a task failed(" + e.getClass() + "): " + e.getLocalizedMessage());

            return false;
        }
    }

    public boolean PushTask(final Runnable t) {
        if (t != null)
            switch (poolMode) {
                case TASK_POOL_MODE_ASYNC_RANDOM:
                    return RandExec(t);
                case TASK_POOL_MODE_ASYNC_SEQUENTIAL:
                    return seqLoopHandler().post(t);
            }
        else
            LogNullTask();

        return true;
    }

    // DelayTaskMap, is only used for cancel delayed randLoopTask..
//    private static final Map<Runnable, Runnable>     delayTaskMap = new HashMap<Runnable, Runnable>();
    public boolean CancelTask(final Runnable t) {
        if (t != null)
            switch (poolMode) {
                case TASK_POOL_MODE_ASYNC_RANDOM:
                    // Cannot cancel randLoop, but if it is a delay task, we can cancel it.
//                    seqLoopHdlr.removeCallbacks(t);
                    return false;
                case TASK_POOL_MODE_ASYNC_SEQUENTIAL:
                    seqLoopHandler().removeCallbacks(t);
                    break;
            }
        else
            LogNullTask();

        return true;
    }

    private static final Map<Runnable, TimerTask> timerTaskMap = new HashMap<Runnable, TimerTask>();
    private static final Timer scheduler = new Timer(true /* is a daemon thread */);
    private static int purgeGap = 0;

    public boolean PushCycTask(final Runnable t, final long millGap, final long millDelay) {
        if (t != null) {
            CancelCycTask(t);

            TimerTask tt = new TimerTask() {
                public void run() {
                    try {
                        if (!PushTask(t)) {         // Avoid block timer thread...
                            Log.w(TAG, "Timer task excutor is full, task runs in timer thread(may cause timer blocked)...");

                            t.run();
                        }
                    } catch (Throwable e) {
                        Log.e(TAG, "Timer task run with exception" + "(" + e.getClass() + "): " + e.getLocalizedMessage());

                    }
                }
            };

            try {
                timerTaskMap.put(t, tt);
                try {
                    scheduler.schedule(tt, millDelay, millGap);
                    return true;
                } catch (Throwable e) {
                    timerTaskMap.remove(t);
                }
            } catch (Throwable e) {
                Log.d(TAG, e.toString());
            }
            return false;
        } else
            LogNullTask();

        return true;
    }

    public boolean CancelCycTask(final Runnable t) {
        TimerTask tt = timerTaskMap.remove(t);
        if (tt != null) {
            boolean canceled = tt.cancel();
            if (purgeGap++ % 128 == 0) scheduler.purge();
            return canceled;
        }
        return true;
    }

    private int poolMode = TASK_POOL_MODE_DEFAULT;

    public int TaskMode() {
        return poolMode;
    }

}
