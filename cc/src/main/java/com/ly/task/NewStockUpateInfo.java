package com.ly.task;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ly.dao.HolidayDao;
import com.ly.dao.StockDao;
import com.ly.dao.StockDayDetailDao;
import com.ly.pinyin.PinyinUtilsPro;
import com.ly.pojo.Holiday;
import com.ly.pojo.Stock;
import com.ly.pojo.StockDayDetail;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 股票数据调用示例代码 － 聚合数据 在线接口文档：http://www.juhe.cn/docs/21
 **/

@Component
public class NewStockUpateInfo {
	private static Logger logger = Logger.getLogger(NewStockUpateInfo.class);

	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 配置您申请的KEY
	public static final String APPKEY = "c0fef72d8ef0a9462ff96e9ffe9615b8";

	@Autowired
	private StockDao stockDao;

	@Autowired
	private StockDayDetailDao stockDayDetailDao;

	@Autowired
	private HolidayDao holidayDao;

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
		flag = hour == 11 && minute >= 30 || hour == 9 && minute <= 29 || hour == 14 && minute >= 57;
		return flag;
	}

	@Scheduled(cron = "0/5 * *  * * ? ")
	public void updateInfoByNullName() {
		logger.info("更新新增的内容开始====> ");
		List<Stock> stock = stockDao.selectNullNameStock();
		if (stock != null && stock.size() > 0) {
			for (Stock sts : stock) {
				String code = null;
				if (sts.getCode().indexOf("6") == 0) {
					code = "sh" + sts.getCode();
				} else {
					code = "sz" + sts.getCode();
				}

				String str = getRequest1(code);
				JSONArray arrays = JSONArray.fromObject(str);

				JSONObject obj = arrays.getJSONObject(0);

				JSONObject obj2 = (JSONObject) obj.get("data");

				sts.setName(obj2.getString("name"));
				sts.setCurrent_price(obj2.getString("nowPri"));
				sts.setMaxprice(obj2.getString("todayMax"));
				sts.setMinprice(obj2.getString("todayMin"));
				sts.setPrevclose(obj2.getString("yestodEndPri"));
				sts.setOpen_price(obj2.getString("todayStartPri"));
				sts.setRisePrice(obj2.getString("increase"));

				// dr xd 除权除红的没有加入

				if (sts.getName().contains("Ａ")) {
					PinyinUtilsPro pro = new PinyinUtilsPro();
					pro.convertChineseToPinyin(sts.getName().substring(0, sts.getName().indexOf("Ａ")));
					String headP = pro.getHeadPinyin();
					sts.setStockPinYin(headP);
				} else if (!sts.getName().contains("Ａ") && sts.getName().contains("ST")) {
					PinyinUtilsPro pro = new PinyinUtilsPro();
					pro.convertChineseToPinyin(
							sts.getName().substring(sts.getName().indexOf("ST") + 2, sts.getName().length()));
					String headP = pro.getHeadPinyin();
					sts.setStockPinYin(headP);
				} else {
					// System.out.println("====" +sts.getName());
					PinyinUtilsPro pro = new PinyinUtilsPro();
					pro.convertChineseToPinyin(sts.getName());
					String headP = pro.getHeadPinyin();
					sts.setStockPinYin(headP);
				}

				stockDao.save(sts);

			}
		}
	}

	@Scheduled(cron = "0 10 15 ? * MON-FRI")
	public void updateFixInfo() {
		List<Stock> stock = stockDao.selectALlStocks(null);
		boolean flag = this.getIsBegin();
		boolean isHoliday = this.getIsHoliday();
		if (!isHoliday) {
			if (!flag) {
				for (Stock sts : stock) {
					String code = null;
					if (sts.getCode().indexOf("6") == 0) {
						code = "sh" + sts.getCode();
					} else {
						code = "sz" + sts.getCode();
					}
					String str = getRequest1(code);
					JSONArray arrays = JSONArray.fromObject(str);

					JSONObject obj = arrays.getJSONObject(0);

					JSONObject obj2 = (JSONObject) obj.get("data");

					sts.setName(obj2.getString("name"));
					sts.setCurrent_price(obj2.getString("nowPri"));
					sts.setMaxprice(obj2.getString("todayMax"));
					sts.setMinprice(obj2.getString("todayMin"));
					sts.setPrevclose(obj2.getString("yestodEndPri"));
					sts.setOpen_price(obj2.getString("todayStartPri"));
					sts.setRisePrice(obj2.getString("increase"));

					StockDayDetail stockDetail = new StockDayDetail();
					stockDetail.setCode(sts.getCode());
					stockDetail.setName(obj2.getString("name"));
					stockDetail.setCloseprice(obj2.getString("nowPri"));
					stockDetail.setMaxprice(obj2.getString("todayMax"));
					stockDetail.setMinprice(obj2.getString("todayMin"));
					stockDetail.setPrevclose(obj2.getString("yestodEndPri"));
					stockDetail.setOpen_price(obj2.getString("todayStartPri"));
					stockDetail.setRisePrice(obj2.getString("increase"));

					// dr xd 除权除红的没有加入

					if (sts.getName().contains("Ａ")) {
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(sts.getName().substring(0, sts.getName().indexOf("Ａ")));
						String headP = pro.getHeadPinyin();
						sts.setStockPinYin(headP);
						stockDetail.setStockPinYin(headP);
					} else if (!sts.getName().contains("Ａ") && sts.getName().contains("ST")) {
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(
								sts.getName().substring(sts.getName().indexOf("ST") + 2, sts.getName().length()));
						String headP = pro.getHeadPinyin();
						sts.setStockPinYin(headP);
						stockDetail.setStockPinYin(headP);
					} else {
						// System.out.println("====" +sts.getName());
						PinyinUtilsPro pro = new PinyinUtilsPro();
						pro.convertChineseToPinyin(sts.getName());
						String headP = pro.getHeadPinyin();
						sts.setStockPinYin(headP);
						stockDetail.setStockPinYin(headP);
					}

					stockDayDetailDao.save(stockDetail);
					stockDao.save(sts);
				}
			}
		}
	}

	@Scheduled(cron = "0 10 15 * * ? ")
	public void updateFixInfo2() {
		List<Stock> stock = stockDao.selectALlStocks(null);
		for (Stock sts : stock) {
			String code = null;
			if (sts.getCode().indexOf("6") == 0) {
				code = "sh" + sts.getCode();
			} else {
				code = "sz" + sts.getCode();
			}
			String str = getRequest1(code);
			JSONArray arrays = JSONArray.fromObject(str);

			JSONObject obj = arrays.getJSONObject(0);

			JSONObject obj2 = (JSONObject) obj.get("data");

			sts.setName(obj2.getString("name"));
			sts.setCurrent_price(obj2.getString("nowPri"));
			sts.setMaxprice(obj2.getString("todayMax"));
			sts.setMinprice(obj2.getString("todayMin"));
			sts.setPrevclose(obj2.getString("yestodEndPri"));
			sts.setOpen_price(obj2.getString("todayStartPri"));
			sts.setRisePrice(obj2.getString("increase"));

			StockDayDetail stockDetail = new StockDayDetail();
			stockDetail.setCode(sts.getCode());
			stockDetail.setName(obj2.getString("name"));
			stockDetail.setCloseprice(obj2.getString("nowPri"));
			stockDetail.setMaxprice(obj2.getString("todayMax"));
			stockDetail.setMinprice(obj2.getString("todayMin"));
			stockDetail.setPrevclose(obj2.getString("yestodEndPri"));
			stockDetail.setOpen_price(obj2.getString("todayStartPri"));
			stockDetail.setRisePrice(obj2.getString("increase"));

			// dr xd 除权除红的没有加入
			if (sts.getName().contains("Ａ")) {
				PinyinUtilsPro pro = new PinyinUtilsPro();
				pro.convertChineseToPinyin(sts.getName().substring(0, sts.getName().indexOf("Ａ")));
				String headP = pro.getHeadPinyin();
				sts.setStockPinYin(headP);
				stockDetail.setStockPinYin(headP);
			} else if (!sts.getName().contains("Ａ") && sts.getName().contains("ST")) {
				PinyinUtilsPro pro = new PinyinUtilsPro();
				pro.convertChineseToPinyin(
						sts.getName().substring(sts.getName().indexOf("ST") + 2, sts.getName().length()));
				String headP = pro.getHeadPinyin();
				sts.setStockPinYin(headP);
				stockDetail.setStockPinYin(headP);
			} else {
				// System.out.println("====" +sts.getName());
				PinyinUtilsPro pro = new PinyinUtilsPro();
				pro.convertChineseToPinyin(sts.getName());
				String headP = pro.getHeadPinyin();
				sts.setStockPinYin(headP);
				stockDetail.setStockPinYin(headP);
			}
			stockDao.save(sts);
		}
	}

	public static String getRequest1(String code) {
		String ret = null;
		String result = null;
		String url = "http://web.juhe.cn:8080/finance/stock/hs";// 请求接口地址
		Map params = new HashMap();// 请求参数
		// params.put("gid", "sz002552");// 股票编号，上海股市以sh开头，深圳股市以sz开头如：sh601009
		params.put("gid", code);// 股票编号，上海股市以sh开头，深圳股市以sz开头如：sh601009
		params.put("key", APPKEY);// APP Key
		try {
			result = net(url, params, "GET");
			JSONObject object = JSONObject.fromObject(result);
			if (object.getInt("error_code") == 0) {
				ret = object.get("result").toString();
				// System.out.println(object.get("result"));
			} else {
				System.out.println(object.get("error_code") + ":" + object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String net(String strUrl, Map params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
