package com.example.demo.service;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

/**
 * .
 */

public interface AccessBlobService {

    ArrayList<CloudBlockBlob> retrieveBlobFilesProperties(String path) throws URISyntaxException, StorageException, InvalidKeyException;

    Boolean isBlobTypeDir(ListBlobItem item);

}
