package com.cico.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MockInterviewScheduler {
	
	@Scheduled(cron = "0 0 6 * * 1-6") // this scheduler is execute MON-SAT 6AM
	public void selectStudentForMock() {
		System.out.println("select student for mock");
	}
	
	@Scheduled(cron = "0 0 22 * * 1-6") // this scheduler is execute MON-SAT 10PM
	public void checkStudentMockCompleteOrNot() {
		System.out.println("check student mock is complete or not");
	}

}
