package com.uf.automatic.ap;

import java.io.File;
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
@ComponentScan({ "com.uf.automatic.controller", "com.uf.automatic.ap" })

public class App {
	public static void main(String[] args) {

		String path = System.getProperty("user.dir");
		String hisFile = path + "/history.properties";
		File file = new File(hisFile);
		if (file.exists()) {
			file.delete();
			System.out.println(file.exists());
		}

		// Utils.writeHistory();
		SpringApplication.run(App.class, args);

		run();
	}

	private static void run() {
		Timer timer = new Timer();
		NewTimerTask timerTask = new NewTimerTask();
		// 程序运行后立刻执行任务，每隔100ms执行一次
		timer.schedule(timerTask, 0, 60000);
	}

}

class NewTimerTask extends TimerTask {

	@Override
	public void run() {
		Utils.writeHistory();
	}

}
