package com.cico.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cico.service.IStudentService;

@Component
public class MockAndCounsellingInterviewScheduler {
	
	@Autowired
	private IStudentService studentService;

	@Scheduled(cron = "0 0 6 * * 1-6") // this scheduler is execute MON-SAT 6AM
	//@Scheduled(cron = "* * * * * *")
	public void selectStudentForCounselling() {		
		//studentService.fetchRandomStudentForCounselling();
		//studentService.fetchRandomStudentForMockInterview();
		System.out.println("select student for counselling");
	}
	
	@Scheduled(cron = "0 0 22 * * 1-6") // this scheduler is execute MON-SAT 10PM
//	@Scheduled(cron = "*/10 * * * * *")
	public void checkStudentCounsellingCompleteOrNot() {
		//studentService.checkCounsellingkIsCompleteOrNot();
		//studentService.checkMockIsCompleteOrNot();
		System.out.println("check student counselling is complete or not");
	}
}
