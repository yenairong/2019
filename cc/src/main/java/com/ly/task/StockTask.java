package com.ly.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ly.dao.HolidayDao;
import com.ly.dao.StockDao;
import com.ly.dao.StockDealDao;
import com.ly.getinfo.PingAn;
import com.ly.pinyin.PinyinUtilsPro;
import com.ly.pojo.Holiday;
import com.ly.pojo.Stock;
import com.ly.pojo.StockDayDealsRecord;


/*


DROP TRIGGER IF EXISTS upd_check02;
CREATE TRIGGER upd_check02 BEFORE update ON stock
 FOR EACH ROW
BEGIN
if new.`code` LIKE '000%' THEN
set new.marketType ='4609';
elseif new.`code` LIKE '002%' THEN
set new.marketType ='4614';
elseif new.`code` LIKE '300%' THEN
set new.marketType ='4621';
elseif new.`code` LIKE '60%' THEN
set new.marketType ='4353';
end if;
end



-----新增
DROP TRIGGER IF EXISTS insert_check;
CREATE TRIGGER insert_check BEFORE insert ON stock
 FOR EACH ROW
BEGIN
if new.`code` LIKE '000%' THEN
set new.marketType ='4609';
elseif new.`code` LIKE '002%' THEN
set new.marketType ='4614';
elseif new.`code` LIKE '300%' THEN
set new.marketType ='4621';
elseif new.`code` LIKE '60%' THEN
set new.marketType ='4353';
end if;
end


*/

@Component
public class StockTask {
	
	@Autowired
	private PingAn pingan;
	
	
	@Autowired
	private HolidayDao holidayDao;	
	
	@Autowired
	private StockDao stockDao;
	
	@Autowired
	private StockDealDao stockDealDao;

	private static Logger logger = Logger.getLogger(StockTask.class); // 获取logger实例

	public boolean getIsHoliday() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		logger.debug("当前日期=============>" + df.format(new Date()));
		Holiday h = holidayDao.getIsHoliday();
		if (h != null) {
			logger.debug("当前节日" + h.getHolidayName() + ",系统不监控 ");
			return true;
		} else {
			return false;
		}
	}
	
	

	public boolean getIsBegin() {
		boolean flag = false;

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY); // http://blog.csdn.net/jiangeeq/article/details/53103069
		int minute = c.get(Calendar.MINUTE);
		flag = hour == 11 && minute >= 30 || hour == 9 && minute <=29||hour==14&&minute>=57;
		return flag;
	}
	
	
	
	
	
	
	
	@SuppressWarnings({ "unchecked","rawtypes" })
    @Scheduled(cron = "0/20 * 9,10,11,13,14 ? * MON-FRI")
	public void job01() throws JsonParseException, JsonMappingException, IOException {
		boolean flag = this.getIsBegin();
		boolean isHoliday = this.getIsHoliday();
		if (!isHoliday) {
			if (!flag) {

				List<Stock> list = stockDao.selectALlStocks(null);
				List<Map<String, String>> list2 = pingan.getStockInfoByBatch(list);
				for (Map map : list2) {
					//logger.info(map);
					Stock stock = new Stock();
					stock.setName((String) map.get("stockName"));
					stock.setCode((String) map.get("code"));
					stock.setCurrent_price((String) map.get("newPrice"));
					stock.setMarketType((String) map.get("codeType"));
					stock.setMaxprice((String) map.get("maxPrice"));
					stock.setMinprice((String) map.get("minPrice"));
					stock.setPrevclose((String) map.get("prevClose"));
					stock.setOpen_price((String) map.get("open"));
					stock.setRisePrice((String) map.get("risePrice"));
					
					//dr xd 除权除红的没有加入
					
					if(stock.getName().contains("Ａ")){
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(stock.getName().substring(0,stock.getName().indexOf("Ａ")));
						String headP = pro.getHeadPinyin();
						stock.setStockPinYin(headP);
					}else if(!stock.getName().contains("Ａ")&&stock.getName().contains("ST")){
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(stock.getName().substring(stock.getName().indexOf("ST")+2, stock.getName().length()));
						String headP = pro.getHeadPinyin();
						stock.setStockPinYin(headP);
					}else{
						//System.out.println("====" +sts.getName());
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(stock.getName());
						String headP = pro.getHeadPinyin();
						stock.setStockPinYin(headP);
					}
					
					stockDao.save(stock);

					// 保存每日成交明细
					StockDayDealsRecord recorde = new StockDayDealsRecord();
					recorde.setName((String) map.get("stockName"));
					recorde.setCode((String) map.get("code"));
					recorde.setPrice((String) map.get("newPrice"));
					recorde.setMarketType((String) map.get("codeType"));
					if(recorde.getName().contains("Ａ")){
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(recorde.getName().substring(0,recorde.getName().indexOf("Ａ")));
						String headP = pro.getHeadPinyin();
						recorde.setStockPinYin(headP);
					}else if(!recorde.getName().contains("Ａ")&&recorde.getName().contains("ST")){
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(recorde.getName().substring(recorde.getName().indexOf("ST")+2, recorde.getName().length()));
						String headP = pro.getHeadPinyin();
						recorde.setStockPinYin(headP);
					}else{
						//System.out.println("====" +sts.getName());
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(recorde.getName());
						String headP = pro.getHeadPinyin();
						recorde.setStockPinYin(headP);
					}
					
					stockDealDao.save(recorde);
				}
			}
		}
	}
	
	
	
	
	 	@Scheduled(cron = "0 16 22 ? * MON-FRI")
		public void job02() throws JsonParseException, JsonMappingException, IOException {
			boolean flag = this.getIsBegin();
			boolean isHoliday = this.getIsHoliday();
			if (!isHoliday) {
				if (!flag) {
					List<Stock> list = stockDao.selectALlStocks(null);
					List<Map<String, String>> list2 = pingan.getStockInfoByBatch(list);
					for (Map map : list2) {
						//logger.info(map);
						Stock stock = new Stock();
						stock.setName((String) map.get("stockName"));
						stock.setCode((String) map.get("code"));
						stock.setCurrent_price((String) map.get("newPrice"));
						stock.setMarketType((String) map.get("codeType"));
						stock.setMaxprice((String) map.get("maxPrice"));
						stock.setMinprice((String) map.get("minPrice"));
						stock.setPrevclose((String) map.get("prevClose"));
						stock.setOpen_price((String) map.get("open"));
						stock.setRisePrice((String) map.get("risePrice"));
						
						//dr xd 除权除红的没有加入
						
						if(stock.getName().contains("Ａ")){
							PinyinUtilsPro pro = new PinyinUtilsPro();
							pro.convertChineseToPinyin(stock.getName().substring(0,stock.getName().indexOf("Ａ")));
							String headP = pro.getHeadPinyin();
							stock.setStockPinYin(headP);
						}else if(!stock.getName().contains("Ａ")&&stock.getName().contains("ST")){
							PinyinUtilsPro pro = new PinyinUtilsPro();
							pro.convertChineseToPinyin(stock.getName().substring(stock.getName().indexOf("ST")+2, stock.getName().length()));
							String headP = pro.getHeadPinyin();
							stock.setStockPinYin(headP);
						}else{
							//System.out.println("====" +sts.getName());
							PinyinUtilsPro pro = new PinyinUtilsPro();
							pro.convertChineseToPinyin(stock.getName());
							String headP = pro.getHeadPinyin();
							stock.setStockPinYin(headP);
						}
						
						stockDao.save(stock);

					}
				}
			}
		}

}
