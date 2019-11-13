package com.example.demo.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * .
 */
@Service
public class AvroServiceImpl implements AvroService {

    /**
     * Logger.
     */
    static final Logger logger = LoggerFactory.getLogger(AvroServiceImpl.class);

    @Override
    public String downloadAvroFile(CloudBlockBlob blob, String localPath) {
        try {
            blob.downloadToFile(localPath);
        } catch (StorageException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blob.getName();
    }

    @Override
    public ArrayList<String> readAvroFile(String localPath) throws IOException {

        ArrayList<String> stringArrayList = new ArrayList<>();
        // file read
        File file = new File(localPath);
        GenericDatumReader datum = new GenericDatumReader<>();
        DataFileReader reader = new DataFileReader(file, datum);
        GenericData.Record record = new GenericData.Record(reader.getSchema());
        while (reader.hasNext()) {
            reader.next(record);
            // ByteBuffer read
            ByteBuffer byteBuffer = ByteBuffer.wrap(record.toString().getBytes("UTF-8"));
            String converted = new String(byteBuffer.array(), "UTF-8");
            // json read
            JsonObject jsonObject = null;
            try {
                jsonObject = new Gson().fromJson(converted, JsonObject.class);
                String asString = jsonObject.get("Body").getAsJsonObject().get("bytes").getAsString();
                
                String toString = new String(asString.getBytes("8859_1"),"UTF-8");
                
                // add arryList
                stringArrayList.add(toString);
            } catch (JsonSyntaxException e) {
                logger.warn("jsonSyntaxException[{}]", jsonObject);
            } finally {
                continue;
            }
        }
        // reader close
        reader.close();
        return stringArrayList;
    }
}
