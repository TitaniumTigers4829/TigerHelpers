package com.titaniumtigers4829.data;

/**
 * Encapsulates the state of an internal Limelight IMU. This includes gyro and accelerometer data.
 */
public class IMUData {
  public double robotYaw;
  public double roll;
  public double pitch;
  public double yaw;
  public double gyroX;
  public double gyroY;
  public double gyroZ;
  public double accelX;
  public double accelY;
  public double accelZ;

  /** Initializes an "empty" IMUData object with default values */
  public IMUData() {
    this.robotYaw = 0;
    this.roll = 0;
    this.pitch = 0;
    this.yaw = 0;
    this.gyroX = 0;
    this.gyroY = 0;
    this.gyroZ = 0;
    this.accelX = 0;
    this.accelY = 0;
    this.accelZ = 0;
  }

  /**
   * Initializes an IMUData object with the provided IMU data array.
   *
   * @param robotYaw The yaw of the robot in degrees
   * @param roll The roll of the robot in degrees
   * @param pitch The pitch of the robot in degrees
   * @param yaw The yaw of the IMU in degrees
   * @param gyroX The x-axis gyro value in degrees per second
   * @param gyroY The y-axis gyro value in degrees per second
   * @param gyroZ The z-axis gyro value in degrees per second TODO: figure out what units the
   *     accelerometer values are in (maybe Gs?)
   * @param accelX The x-axis accelerometer value in meters per second squared
   * @param accelY The y-axis accelerometer value in meters per second squared
   * @param accelZ The z-axis accelerometer value in meters per second squared
   */
  public IMUData(
      double robotYaw,
      double roll,
      double pitch,
      double yaw,
      double gyroX,
      double gyroY,
      double gyroZ,
      double accelX,
      double accelY,
      double accelZ) {
    this.robotYaw = robotYaw;
    this.roll = roll;
    this.pitch = pitch;
    this.yaw = yaw;
    this.gyroX = gyroX;
    this.gyroY = gyroY;
    this.gyroZ = gyroZ;
    this.accelX = accelX;
    this.accelY = accelY;
    this.accelZ = accelZ;
  }
}
