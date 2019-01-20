package edu.boscotech.techlib.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.wpi.first.wpilibj.Filesystem;

public class Config {
  private static Config s_instance = new Config();
  private JSONObject m_config = new JSONObject();

  private Config() {
    JSONParser parser = new JSONParser();
    String path = Filesystem.getDeployDirectory().getAbsolutePath();
    path += "/config.json";
    System.out.println("Loaded config file from " + path);
    try {
      m_config = (JSONObject) parser.parse(new FileReader(path));
    } catch (FileNotFoundException e) {
      System.out.println("Error finding config.json!");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Error reading config.json!");
      e.printStackTrace();
    } catch (ParseException e) {
      System.out.println("Error parsing config.json!");
      e.printStackTrace();
    }
  }

  public static Config getInstance() {
    if (s_instance == null) {
      s_instance = new Config();
    }
    return s_instance;
  }

  private NoSuchElementException makePathError(String[] path, int pos) {
    String message = 
      "\nThere is no data in the configuration file with the path:\n";
    message += path[0];
    for (int j = 1; j < path.length; j++) {
      message += "." + path[j];
    }
    message += "\nThere was an error retrieving the path:\n";
    message += path[0];
    for (int j = 1; j < pos + 1; j++) {
      message += "." + path[j];
    }
    return new NoSuchElementException(message);
  }

  public String getString(String... path) {
    JSONObject element = m_config;
    for (int i = 0; i < path.length - 1; i++) {
      element = (JSONObject) element.get(path[i]);
      if (element == null) {
        throw makePathError(path, i);
      }
    }
    String result = (String) element.get(path[path.length - 1]);
    if (result == null) {
      throw makePathError(path, path.length);
    } else {
      return result;
    }
  }

  public int getInt(String... path) {
    JSONObject element = m_config;
    for (int i = 0; i < path.length - 1; i++) {
      element = (JSONObject) element.get(path[i]);
      if (element == null) {
        throw makePathError(path, i);
      }
    }
    Long result = (Long) element.get(path[path.length - 1]);
    if (result == null) {
      throw makePathError(path, path.length);
    } else {
      return result.intValue();
    }
  }

  public double getDouble(String... path) {
    JSONObject element = m_config;
    for (int i = 0; i < path.length - 1; i++) {
      element = (JSONObject) element.get(path[i]);
      if (element == null) {
        throw makePathError(path, i);
      }
    }
    Double result = (Double) element.get(path[path.length - 1]);
    if (result == null) {
      throw makePathError(path, path.length);
    } else {
      return result;
    }
  }
}