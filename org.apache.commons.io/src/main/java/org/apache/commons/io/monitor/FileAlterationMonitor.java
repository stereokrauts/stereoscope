/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.monitor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;

/**
 * A runnable that spawns a monitoring thread triggering any
 * registered {@link FileAlterationObserver} at a specified interval.
 * 
 * @see FileAlterationObserver
 * @version $Id: FileAlterationMonitor.java 1304052 2012-03-22 20:55:29Z ggregory $
 * @since 2.0
 */
public final class FileAlterationMonitor implements Runnable {

    private final long interval;
    private final List<FileAlterationObserver> observers = new CopyOnWriteArrayList<FileAlterationObserver>();
    private Thread thread = null;
    private ThreadFactory threadFactory;
    private volatile boolean running = false;

    /**
     * Construct a monitor with a default interval of 10 seconds.
     */
    public FileAlterationMonitor() {
        this(10000);
    }

    /**
     * Construct a monitor with the specified interval.
     *
     * @param interval The amount of time in miliseconds to wait between
     * checks of the file system
     */
    public FileAlterationMonitor(final long interval) {
        this.interval = interval;
    }

    /**
     * Construct a monitor with the specified interval and set of observers.
     *
     * @param interval The amount of time in miliseconds to wait between
     * checks of the file system
     * @param observers The set of observers to add to the monitor.
     */
    public FileAlterationMonitor(final long interval, final FileAlterationObserver... observers) {
        this(interval);
        if (observers != null) {
            for (final FileAlterationObserver observer : observers) {
                this.addObserver(observer);
            }
        }
    }

    /**
     * Return the interval.
     *
     * @return the interval
     */
    public long getInterval() {
        return this.interval;
    }

    /**
     * Set the thread factory.
     *
     * @param threadFactory the thread factory
     */
    public synchronized void setThreadFactory(final ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    /**
     * Add a file system observer to this monitor.
     *
     * @param observer The file system observer to add
     */
    public void addObserver(final FileAlterationObserver observer) {
        if (observer != null) {
            this.observers.add(observer);
        }
    }

    /**
     * Remove a file system observer from this monitor.
     *
     * @param observer The file system observer to remove
     */
    public void removeObserver(final FileAlterationObserver observer) {
        if (observer != null) {
            while (this.observers.remove(observer)) {
            }
        }
    }

    /**
     * Returns the set of {@link FileAlterationObserver} registered with
     * this monitor. 
     *
     * @return The set of {@link FileAlterationObserver}
     */
    public Iterable<FileAlterationObserver> getObservers() {
        return this.observers;
    }

    /**
     * Start monitoring.
     *
     * @throws Exception if an error occurs initializing the observer
     */
    public synchronized void start() throws Exception {
        if (this.running) {
            throw new IllegalStateException("Monitor is already running");
        }
        for (final FileAlterationObserver observer : this.observers) {
            observer.initialize();
        }
        this.running = true;
        if (this.threadFactory != null) {
            this.thread = this.threadFactory.newThread(this);
        } else {
            this.thread = new Thread(this);
        }
        this.thread.start();
    }

    /**
     * Stop monitoring.
     *
     * @throws Exception if an error occurs initializing the observer
     */
    public synchronized void stop() throws Exception {
        this.stop(this.interval);
    }

    /**
     * Stop monitoring.
     *
     * @param stopInterval the amount of time in milliseconds to wait for the thread to finish.
     * A value of zero will wait until the thread is finished (see {@link Thread#join(long)}).
     * @throws Exception if an error occurs initializing the observer
     * @since 2.1
     */
    public synchronized void stop(final long stopInterval) throws Exception {
        if (this.running == false) {
            throw new IllegalStateException("Monitor is not running");
        }
        this.running = false;
        try {
            this.thread.join(stopInterval);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for (final FileAlterationObserver observer : this.observers) {
            observer.destroy();
        }
    }

    /**
     * Run.
     */
    @Override
	public void run() {
        while (this.running) {
            for (final FileAlterationObserver observer : this.observers) {
                observer.checkAndNotify();
            }
            if (!this.running) {
                break;
            }
            try {
                Thread.sleep(this.interval);
            } catch (final InterruptedException ignored) {
            }
        }
    }
}
