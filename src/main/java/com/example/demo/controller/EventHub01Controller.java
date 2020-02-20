package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.service.EventHub01Service;
import com.google.common.base.Strings;

@RestController
@RequestMapping(value="/eventHub01")
public class EventHub01Controller {
	
	static final Logger logger = LoggerFactory.getLogger(EventHub01Controller.class);
	
	@Autowired
	EventHub01Service eventHub01Service;
	
	@Value("${config.storageUrl}")
	private String storageUrl;
	
	String[] hours = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    String[] minutes = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
            "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
	
	@PostMapping("/req")
	public ApiResponse eventHub01 (@RequestParam String dir, @RequestParam String localPath, @RequestParam(required=false) String times, @RequestParam(required=false) String reqId) {
		
		System.out.println("revert 테스트 진행");
		
		logger.info("########## Event Start ##########");
		
		StopWatch stopWatch = new StopWatch();
        stopWatch.start();
		
        StringBuffer stringBuffer = null;
        String prefix = storageUrl;
        String suffix = "/" + dir;
        String time = null;
        String blobPath = null; 
        
        for(int i=0; i<=31; i++) {
        	if(!Strings.isNullOrEmpty(times)) {
            	stringBuffer = new StringBuffer(times);
            	stringBuffer.insert(2, "/");
            	time = stringBuffer.toString();
            	
            	blobPath = prefix + String.valueOf(i) + suffix + time;
            	
            	try {
					eventHub01Service.eventHub01(blobPath, localPath, reqId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }else {
            	for(String hour : hours) {
            		for(String minute : minutes) {
            			blobPath = prefix + String.valueOf(i) + suffix + hour + "/" + minute; 
            			try {
							eventHub01Service.eventHub01(blobPath, localPath, reqId);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}
            	}
            }
        }
        
        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
        logger.info("########## Event End ##########");
        
        return new ApiResponse();
	}
	
}
