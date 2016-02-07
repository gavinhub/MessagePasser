package edu.cmu.ds.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import edu.cmu.ds.clock.ClockService;
import edu.cmu.ds.clock.ClockServiceFactory;
import edu.cmu.ds.message.ConfigParser;
import edu.cmu.ds.message.Message;
import edu.cmu.ds.message.MessagePasser;

public class Driver {

	public static void main(String[] args) throws ParseException, IOException{
		if (args.length < 2)
			throw 	new ParseException("Arguments error:" + args[0], 0);

		String configFile = args[0];
		String clockType = args[1];


		ClockServiceFactory factory = new ClockServiceFactory();
		ConfigParser config = new ConfigParser(configFile);

		// My part
		Logger logger = new Logger();

		ClockService clock = factory.getClockService(clockType, "logger", config.getHostArray());
		MessagePasser passer = new MessagePasser(config, "logger", clock);

		// Receive all incoming log
		Thread display = new Thread() {
			public void run() {
				while(true) {
					try {
						Message msg = passer.receive();
						TimeStampedMessage tsMsg = new TimeStampedMessage(msg.getTimestamp(), msg);
						logger.addLog(tsMsg);
					} catch (InterruptedException e) {
						System.out.println("Interrupted");
						e.printStackTrace();
						break;
					}
				}
			}
		};
		display.start();

		BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
		while (true)
			try {
				Thread.sleep(250);
				System.out.print("\033[92m>> ");
				String input = keyIn.readLine();
				if (input == null) {
					break;
				}
				if (input.trim().equals(""))
					continue;

				if (input.equals("showlog")) {
					logger.printLog();
				} 

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
	}

}
