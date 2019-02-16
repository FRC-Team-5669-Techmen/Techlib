package edu.boscotech.techlib.subsystems;

import java.util.ArrayList;
import java.util.List;

import edu.boscotech.techlib.components.StreamedCamera;

public class CameraSubsystem extends BetterSubsystem {
  private List<StreamedCamera> cameras = new ArrayList<>();

  public CameraSubsystem(String... cameraNames) {
    super("Cameras", "cameras", "Cameras");
    for (String name : cameraNames) {
      cameras.add(new StreamedCamera(name));
    }
  }

  @Override
  protected void setup() { }

  public void startCameraStreams() {
    for (StreamedCamera camera : cameras) {
      camera.startCameraStream();
    }
  }
}