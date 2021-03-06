package com.ly.task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ly.dao.EmailDao;
import com.ly.email.MailSenderTool;
import com.ly.pojo.Email;



@Component
public class EmailTask {
	
	@Autowired
	private MailSenderTool mailTool;
	
	
	@Autowired
	private EmailDao emailDao;
	
	private static Logger logger = Logger.getLogger(EmailTask.class); // 获取logger实例
	
	@Scheduled(cron = "0 0/5  *  * * ? ") // 每5秒执行一次
	public void task01() throws MessagingException, FileNotFoundException, IOException {
		logger.info("普通Info信息");
		// logger.error("报错信息", new IllegalArgumentException("非法参数"));
		
		List<Email> emails = emailDao.selectAll(null);
		
		for (int i = 0; i < emails.size(); i++) {
			mailTool.creatTemplate(emails.get(i).getEmailAddress(), "日程提醒");
		}
		
	}
	

}
