package com.ly.info;

import java.util.HashMap;
import java.util.Map;

import com.ly.http.utils.HttpKit;

public class LaoDaoInfoUtils {
	
	public static String getMusicList(String musicName){
		Map<String, String> headers = new HashMap();
		headers.put("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
		headers.put("Accept","*/*");
		headers.put("Referer","https://y.qq.com/portal/search.html");
		headers.put("Accept-language","zh-CN,zh;q=0.9");
		headers.put("Cookie","yqq_stat=0; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221660f51e07b31a-0b736491a58a07-661f1574-4953600-1660f51e07daf8%22%2C%22%24device_id%22%3A%221660f51e07b31a-0b736491a58a07-661f1574-4953600-1660f51e07daf8%22%2C%22props%22%3A%7B%7D%7D; pgv_pvi=7200084992; pgv_si=s4972853248; pgv_info=ssid=s3453417418; pgv_pvid=9791469305; ts_uid=6808134808; player_exist=1; yq_playschange=0; yq_playdata=; qqmusic_fromtag=66; ts_last=y.qq.com/porta");
		//headers.put("Host", "c.y.qq.com");
		String url = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=sizer.yqq.song_next&searchid=26103355315496177&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=1&n=10&w="+musicName+"&g_tk=949041503&jsonpCallback=MusicJsonCallback49338224494681415&loginUin=3409304997&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
		String str = HttpKit.get(url,null,headers);
		if(str!=null){
			str = str.substring(str.indexOf("(")+1,str.length()-1);
			return str;
		}else{
			return null;
		}
	}
	
	

	public static String getMusicLrc(String url){
		Map<String, String> params = new HashMap();
		Map<String, String> headers = new HashMap();
		
		//https://blog.csdn.net/qq_20545367/article/details/79538530
		//https://blog.csdn.net/hhzzcc_/article/details/79769386
		headers.put("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
		headers.put("Accept","*/*");
		headers.put("Referer","https://y.qq.com/portal/player.html");
		headers.put("Accept-language","zh-CN,zh;q=0.9");
		headers.put("Cookie","yqq_stat=0; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221660f51e07b31a-0b736491a58a07-661f1574-4953600-1660f51e07daf8%22%2C%22%24device_id%22%3A%221660f51e07b31a-0b736491a58a07-661f1574-4953600-1660f51e07daf8%22%2C%22props%22%3A%7B%7D%7D; pgv_pvi=7200084992; pgv_si=s4972853248; pgv_info=ssid=s3453417418; pgv_pvid=9791469305; ts_uid=6808134808; player_exist=1; yq_playschange=0; yq_playdata=; qqmusic_fromtag=66; ts_last=y.qq.com/porta");
		headers.put("Host", "c.y.qq.com");
		String str = HttpKit.get(url,null,headers);
		if(str!=null){
			str = str.substring(str.indexOf("(")+1,str.length()-1);
			return str;
		}else{
			return null;
		}
	}
	
	
	
	public static void main(String[] args) {
		
	}
	
}
