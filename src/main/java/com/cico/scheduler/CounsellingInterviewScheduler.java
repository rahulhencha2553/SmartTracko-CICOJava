package com.cico.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CounsellingInterviewScheduler {

	@Scheduled(cron = "0 0 6 * * 1-6") // this scheduler is execute MON-SAT 6AM
	public void selectStudentForCounselling() {
		System.out.println("select student for counselling");
	}
	
	@Scheduled(cron = "0 0 22 * * 1-6") // this scheduler is execute MON-SAT 10PM
	public void checkStudentCounsellingCompleteOrNot() {
		System.out.println("check student counselling is complete or not");
	}
}
