package com.example.demo.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

/**
 * .
 */
@Service
public class AccessBlobServiceImpl implements  AccessBlobService{
    /**
     * Logger.
     */
    static final Logger logger = LoggerFactory.getLogger(AccessBlobServiceImpl.class);
    
    @Value("${config.storageConnectionString}")
    private String storageConnectionString;

    /**
     * retriveBlobFilesProperties.
     * @param path String
     * @throws Exception
     */
    @Override
    public ArrayList<CloudBlockBlob> retrieveBlobFilesProperties(String path) throws URISyntaxException, StorageException, InvalidKeyException {
    	
        CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
        CloudBlobContainer cloudBlobContainer = cloudBlobClient.getContainerReference("eventhub01");
        CloudBlobDirectory directory = cloudBlobContainer.getDirectoryReference(path);
        Iterable<ListBlobItem> blobItems = directory.listBlobs();
        ArrayList<CloudBlockBlob> resultList = new ArrayList<>();
        for (ListBlobItem item : blobItems) {
                CloudBlob cloudBlob = (CloudBlob) item;
                String[] strings = StringUtils.delimitedListToStringArray(cloudBlob.getName(), "/");
                int fileNameIndex = strings.length -1 ;
                String fileName = strings[fileNameIndex];
                CloudBlockBlob cloudBlockBlob  = null;
                cloudBlockBlob = directory.getBlockBlobReference(fileName);
                resultList.add(cloudBlockBlob);
        }
        return resultList;
    }

    /**
     * isBlobTypeDir.
     * @param item ListBlobItem
     * @return Boolean
     */
    @Override
    public Boolean isBlobTypeDir(ListBlobItem item) {

        boolean result = false;

        if(item instanceof CloudBlobDirectory) {
            result = true;
        }

        return result;
    }
}
