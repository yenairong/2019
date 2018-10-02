package com.ly.task;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTask {

	@Autowired
	private JavaMailSender mailSender;

	private static Logger logger = Logger.getLogger(JobTask.class); // 获取logger实例

	@Scheduled(cron = "0/15 *  *  * * ? ") // 每5秒执行一次
	public void task01() throws MessagingException {
		StringBuffer emailBf = new StringBuffer();
		logger.info("普通Info信息");
		// logger.error("报错信息", new IllegalArgumentException("非法参数"));

		/*MimeMessage mimeMsg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, false, "utf-8");
		mimeMsg.setContent("333", "text/html");
		helper.setTo("1039288191@qq.com");
		helper.setSubject("333");
		helper.setFrom("1039288191@qq.com");
		mailSender.send(mimeMsg);*/
		
		
		
		
		
		//JavaMailSender  javaMailSender = mailSender.createMimeMessage();
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mime, true, "utf-8");
            helper.setTo("1039288191@qq.com");// 收件人邮箱地址
            helper.setFrom("1039288191@qq.com");// 收件人
            helper.setSubject("SpringMailTest");// 主题
            helper.setText("测试Spring自带邮件发送功能");// 正文
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        mailSender.send(mime);
		
		
		
		
		
		
		
		
		
		

		System.out.println("111");
	}

}
