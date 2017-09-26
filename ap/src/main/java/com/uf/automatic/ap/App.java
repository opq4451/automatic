package com.uf.automatic.ap;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.uf.automatic.util.Utils;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ComponentScan({ "com.uf.automatic.controller" })

public class App 
{
    public static void main( String[] args )
    {
    		//Utils.writeHistory();
       SpringApplication.run(App.class, args);
    	   //run();
    }
    
    
    private static void run() {  
        Timer timer = new Timer();  
        NewTimerTask timerTask = new NewTimerTask();  
        //程序运行后立刻执行任务，每隔100ms执行一次  
        timer.schedule(timerTask, 0, 1000);  
    }  
    
}


class NewTimerTask extends TimerTask {  
	  
    @Override  
    public void run() {  
        System.out.println("123");
    }  
   
}  
