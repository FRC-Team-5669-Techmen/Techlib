package edu.boscotech.techlib.components;

import edu.boscotech.techlib.config.Config;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class StreamedCamera {
  UsbCamera camera;
  String name;

  public StreamedCamera(String name) {
    this.name = name;
  }

  public void startCameraStream() {
    Config cfg = Config.getInstance();
    int 
      port = cfg.getInt("cameras", name, "port"),
      width = cfg.getInt("cameras", name, "width"),
      height = cfg.getInt("cameras", name, "height"),
      fps = cfg.getInt("cameras", name, "fps"),
      quality = cfg.getInt("cameras", name, "quality");

    CameraServer cameraServer = CameraServer.getInstance();
    MjpegServer mjpegServer 
      = cameraServer.addServer("server for " + name + " camera");
    camera = new UsbCamera(name + " camera", port);
    cameraServer.addCamera(camera);
    mjpegServer.setSource(camera);
    
    mjpegServer.getProperty("compression").set(quality);
    mjpegServer.getProperty("default_compression").set(quality);
    mjpegServer.getProperty("width").set(width);
    mjpegServer.getProperty("height").set(height);
    camera.setResolution(width, height);
    mjpegServer.getProperty("fps").set(fps);
    camera.setFPS(fps);
  }
}