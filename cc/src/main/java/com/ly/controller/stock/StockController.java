package com.ly.controller.stock;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ly.dao.StockDao;

@Controller
@RequestMapping("/stock")
public class StockController {

	@Autowired
	private StockDao stockDao;
	
	@RequestMapping("/getStock")
	@ResponseBody
	public List getStock(String code){
		HashMap map = new HashMap();
		map.put("code",code);
		List list = stockDao.selectStockByParam(map);
		return list;
	}
	
	
	
	@RequestMapping("/moImport")
	@ResponseBody
	public List moImport(String code){
		HashMap map = new HashMap();
		map.put("isMon", 1);
		List list = stockDao.selectStockByParam(map);
		return list;
	}
	
	
}
