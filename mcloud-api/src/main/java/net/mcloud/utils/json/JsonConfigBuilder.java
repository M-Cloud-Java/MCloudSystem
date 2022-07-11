package net.mcloud.utils.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonConfigBuilder {

    private File file;
    private JSONObject jsonConfig;
    private final String name;

    public JsonConfigBuilder(String path , String fileName) {
        this.name = fileName + ".json";

        File file = new File(path + "/" + fileName + ".json");
        try {
            File folder = file.getParentFile();
            if(!folder.exists())
                folder.mkdirs();

            this.file = file;
            if(!(file.exists())) {
                file.createNewFile();
                this.jsonConfig = new JSONObject();
                saveConfig();
            } else {
                this.jsonConfig = new JSONObject(new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8".replace("\n", "")));
            }
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public JsonConfigBuilder(String fileName) {
        this.name = fileName + ".json";

        File file = new File("src/main/resources/" + fileName + ".json");
        try {
            File folder = file.getParentFile();
            if(!folder.exists())
                folder.mkdirs();

            this.file = file;
            if(!(file.exists())) {
                file.createNewFile();
                this.jsonConfig = new JSONObject();
                saveConfig();
            } else {
                this.jsonConfig = new JSONObject(new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8".replace("\n", "")));
            }
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonConfig.toString());
            fileWriter.flush();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public Object getObject(String key, Object defaultValue) {
        try {
            return this.jsonConfig.get(key);
        } catch (JSONException exception) {
            jsonConfig.put(key, defaultValue);
            saveConfig();
            return defaultValue;
        }
    }

    public void setObject(String key,Object newValue, Object defaultValue) {
        try {
            this.jsonConfig.put(key, newValue);
            saveConfig();
        } catch (JSONException exception) {
            jsonConfig.put(key, defaultValue);
            saveConfig();
        }
    }

    public List<Object> getList(String key, List<Object> defaultValue) {
        try {
            return this.jsonConfig.getJSONArray(key).toList();
        } catch (JSONException exception) {
            this.jsonConfig.put(key, defaultValue);
            saveConfig();
            return null;
        }
    }

    public List<Object> getList(String key) {
        try {
            return this.jsonConfig.getJSONArray(key).toList();
        } catch (JSONException exception) {
            saveConfig();
            return null;
        }
    }

    public void setList(String key, List<Object> newValue, List<Object> defaultValue) {
        try {
            this.jsonConfig.put(key, newValue);
            saveConfig();
        } catch (JSONException exception) {
            this.jsonConfig.put(key, defaultValue);
            saveConfig();
        }
    }

    public void setList(String key, List<Object> newValue) {
        try {
            this.jsonConfig.put(key, newValue);
            saveConfig();
        } catch (JSONException exception) {
            saveConfig();
        }
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return (Integer) getObject(key, defaultValue);
    }

    public void setInteger(String key, Integer newValue, Integer defaultValue) {
        this.setObject(key, newValue, defaultValue);
        saveConfig();
    }

    public String getString(String key, String defaultValue) {
        return (String) getObject(key, defaultValue);
    }

    public void setString(String key, String newValue, String defaultValue) {
        this.setObject(key, newValue, defaultValue);
        saveConfig();
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return (Boolean) getObject(key, defaultValue);
    }

    public void setBoolean(String key, Boolean newValue, Boolean defaultValue) {
        this.setObject(key, newValue, defaultValue);
        saveConfig();
    }

    public Double getDouble(String key, Double defaultValue) {
        return (Double) getObject(key, defaultValue);
    }

    public void setDouble(String key, Double newValue, Double defaultValue) {
        this.setObject(key, newValue, defaultValue);
        saveConfig();
    }

    public Float getFloat(String key, Float defaultValue) {
        return (Float) getObject(key, defaultValue);
    }

    public void setFloat(String key, Float newValue, Float defaultValue) {
        this.setObject(key, newValue, defaultValue);
        saveConfig();
    }

    public JSONObject getJsonConfig() {
        return jsonConfig;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }
}
