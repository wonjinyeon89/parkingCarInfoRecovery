package com.example.demo.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ModelConvData;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.microsoft.azure.storage.blob.CloudBlockBlob;


@Service
public class EventHub01ServiceImpl implements EventHub01Service {

	@Autowired
    AccessBlobService accessBlobService;
	
	@Autowired
	AvroService avroService;
	
	@Value("${config.fileUrl}")
	private String fileUrl;
	
	static final Logger logger = LoggerFactory.getLogger(EventHub01ServiceImpl.class);
	
	@Override
	public void eventHub01(String blobPath, String localPath, String reqId) throws Exception {
		
		// 1. find blob
		ArrayList<CloudBlockBlob> cloudBlockBlobs = accessBlobService.retrieveBlobFilesProperties(blobPath);
		
		for(CloudBlockBlob cloudBlockBlob : cloudBlockBlobs) {
			// 2. download blob
			String blobName = avroService.downloadAvroFile(cloudBlockBlob, localPath);
			JsonObject jsonObject =null;
			JsonObject eventData = null;
			String requestId = null;
			
			if (blobName != null) {
				ArrayList<String> jsonList = avroService.readAvroFile(localPath);
				if (!jsonList.isEmpty()) {
					for(String json : jsonList) {
						try {
							jsonObject = new Gson().fromJson(json, JsonObject.class);
						}catch(Exception e) {
							logger.info("jsonObject Excepton[{}]", json);
						}
						
						try {
							eventData = jsonObject.get("eventData").getAsJsonObject();
						}catch(Exception e) {
							logger.info("eventData Excepton[{}]", jsonObject.toString());
						}
						
						if(ModelConvData.findBy(jsonObject.get("eventType").getAsString()) == null) {
							continue;
						}
						
						if(Strings.isNullOrEmpty(reqId)) {
							eventHub01FileWrite(json);
						}else {
							requestId = eventData.get("requestId").getAsString();
							if(reqId.equals(requestId)) {
								eventHub01FileWrite(json);
							}
						}
					}
				}
			}
		}
	}
	
	public void eventHub01FileWrite(String json) {
		
		File file = new File(fileUrl);
		FileWriter writer = null;
		BufferedWriter bWriter = null;
		try {
          writer = new FileWriter(file, true);
          bWriter = new BufferedWriter(writer);
                                    
          bWriter.write(json);
          bWriter.flush();
          bWriter.newLine();
		} catch(IOException e) {
          e.printStackTrace();
		} finally {
			try {
				if(bWriter != null) bWriter.close();
				if(writer != null) writer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
