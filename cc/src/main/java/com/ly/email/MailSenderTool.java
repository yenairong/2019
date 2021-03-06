package com.ly.email;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ly.dao.StockDao;
import com.ly.pojo.Stock;


@Component
public class MailSenderTool{
	private static Logger logger = Logger.getLogger(MailSenderTool.class); // 获取logger实例
	@Autowired
	private StockDao stockDao;
	@Autowired
	private EmailUtil emailUtil;
	
	public void creatTemplate(String to,String title){
		
		StringBuffer bf = new StringBuffer();
		bf.append("<table style='width:100%; border-collapse:collapse; margin:0 0 10px' cellpadding='0' cellspacing='0' border='0'><tbody>");
		bf.append("<tr><td  style='background:url(https://rescdn.qqmail.com/zh_CN/htmledition/images/xinzhi/bg/a_03.jpg) no-repeat #fbf7f4; min-height:550px; padding: 150px 80px 200px;'>");
		StringBuffer contents = new StringBuffer();
	
		
		Map map = new HashMap();
		map.put("orderType", 1);
		List<Stock> list = stockDao.getStockInfo(map);
		
		map.put("orderType", 0);
		List<Stock> list1 = stockDao.getStockInfo(map);
		map.put("orderType", -1);
		List<Stock> list2 = stockDao.getStockInfo(map);
		
		contents.append("<table class='tbl1' style='width:100%; border-collapse:collapse; margin:0 0 10px' cellpadding='0' cellspacing='0' border='0'>");
		contents.append("<tr><td colspan='6' style='text-align:center;;font-weight:bold;padding:5px 10px;border:1px solid #C1D9F3;background:#C1D9F3'>监控宝</td></tr>");
		contents.append("<tr><td colspan='6' style='text-align:center;;font-weight:bold;padding:5px 10px;border:1px solid #C1D9F3;background:#F2F2F2'>自选当天涨</td></tr>");
		contents.append("<tr><td style='text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>序号</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>代码</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>名称</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>当前价</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>昨收</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>涨跌幅</td></tr>");
		
		for (int i = 0; i < list.size(); i++) {
			if(i%2==0){
				contents.append("<tr><td style='text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:red'>"+i+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:red'>"+list.get(i).getCode()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:red'>"+list.get(i).getName()+"</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:red'>"+list.get(i).getCurrent_price()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:red'>"+list.get(i).getPrevclose()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:red'>"+list.get(i).getRisePrice()+"</td></tr>");
			}else{
				contents.append("<tr><td style='text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:red'>"+i+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:red'>"+list.get(i).getCode()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:red'>"+list.get(i).getName()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:red'>"+list.get(i).getCurrent_price()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:red'>"+list.get(i).getPrevclose()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:red'>"+list.get(i).getRisePrice()+"</td></tr>");
			}
		}
		contents.append("</table>");
		
		contents.append("<table class='tbl2' style='width:100%; border-collapse:collapse; margin:0 0 10px' cellpadding='0' cellspacing='0' border='0'>");
		contents.append("<tr><td colspan='6' style='text-align:center;;font-weight:bold;padding:5px 10px;border:1px solid #C1D9F3;background:#F2F2F2;color:#ccc'>自选当天稳定</td></tr>");
		contents.append("<tr><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>序号</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>代码</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>名称</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>当前价</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>昨收</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>涨跌幅</td></tr>");
		for (int i = 0; i < list1.size(); i++) {
			if(i%2==0){
				contents.append("<tr><td style='text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;'>"+i+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;'>"+list1.get(i).getCode()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;'>"+list1.get(i).getName()+"</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;'>"+list1.get(i).getCurrent_price()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;'>"+list1.get(i).getPrevclose()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>"+list1.get(i).getRisePrice()+"</td></tr>");
			}else{
				contents.append("<tr><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;'>"+i+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;'>"+list1.get(i).getCode()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;'>"+list1.get(i).getName()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;'>"+list1.get(i).getCurrent_price()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;'>"+list1.get(i).getPrevclose()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3'>"+list1.get(i).getRisePrice()+"</td></tr>");
			}
		}
		contents.append("</table>");
		
		contents.append("<table class='tbl3' style='width:100%; border-collapse:collapse; margin:0 0 10px' cellpadding='0' cellspacing='0' border='0'>");
		contents.append("<tr><td colspan='6' style='text-align:center;font-weight:bold;padding:5px 10px;border:1px solid #C1D9F3;background:#F2F2F2;color:green'>自选当天跌</td></tr>");
		contents.append("<tr><td style='text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>序号</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>代码</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>名称</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>当前价</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>昨收</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3'>涨跌幅</td></tr>");
		for (int i = 0; i < list2.size(); i++) {
			if(i%2==0){
				contents.append("<tr><td style='text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:green'>"+i+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getCode()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getName()+"</td><td style='wont-size:14px;text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getCurrent_price()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getPrevclose()+"</td><td style=';text-align:center;background:#EFF5FB;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getRisePrice()+"</td></tr>");
			}else{
				contents.append("<tr><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:green'>"+i+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getCode()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getName()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getCurrent_price()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getPrevclose()+"</td><td style=';text-align:center;background:#B2D7EA;border:1px solid #C1D9F3;color:green'>"+list2.get(i).getRisePrice()+"</td></tr>");
			}
		}
		contents.append("</table>");
		
		
		bf.append("<div>"+contents.toString()+"</div>");//内容div
		bf.append("</td></tr></tbody></table>");
		
		
		try {
			logger.info("邮件发送内容为===> " +bf.toString());
			emailUtil.sendEmail( to,title, bf.toString(), true);
		} catch (IOException | MessagingException e) {
			logger.debug("发送邮件失败，异常为  ===>  " + e.getMessage());
			e.printStackTrace();
		}
		
	}
}
