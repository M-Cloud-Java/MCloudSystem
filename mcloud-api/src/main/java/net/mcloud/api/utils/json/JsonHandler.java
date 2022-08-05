package net.mcloud.api.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class JsonHandler {
    private String storagePath;
    private Gson gson;

    public JsonHandler(String path) {
        this.storagePath = path;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void parseObject(Object object, String fileName) throws IOException {
        String jsonString = this.gson.toJson(object);
        File file  = new File(this.storagePath + "/" + fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonString);
        fileWriter.flush();
        fileWriter.close();
    }

    public <T> Object getObject(String fileName, Class<T> typeOf) throws FileNotFoundException {
        File file = new File(this.storagePath + "/" + fileName);
        FileReader fileReader = new FileReader(file);
        return this.gson.fromJson(fileReader, typeOf);
    }

    public String getStoragePath() {
        return storagePath;
    }
}
