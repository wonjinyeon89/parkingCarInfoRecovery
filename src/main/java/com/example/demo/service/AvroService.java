package com.example.demo.service;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

/**
 * .
 */
public interface AvroService {

    String downloadAvroFile(CloudBlockBlob blob, String localPath);

    ArrayList<String> readAvroFile(String localPath) throws IOException, StorageException, URISyntaxException, InvalidKeyException;
}
