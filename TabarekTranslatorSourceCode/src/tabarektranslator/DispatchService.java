/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabarektranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javax.swing.SwingUtilities;
import org.jnativehook.GlobalScreen;
 

 
 public class DispatchService extends AbstractExecutorService   {
         private boolean running = false;

	public DispatchService() {
		running = true;
	}

	public void shutdown() {
		running = false;
	}

	public List<Runnable> shutdownNow() {
		running = false;
		return new ArrayList<Runnable>(0);
	}

	public boolean isShutdown() {
		return !running;
	}

	public boolean isTerminated() {
		return !running;
	}

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return true;
	}

	public void execute(Runnable r) { 
            r.run(); 
           
	}
    }