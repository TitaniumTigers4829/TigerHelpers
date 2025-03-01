package com.titaniumtigers4829.data;

/**
 * Encapsulates the state of an internal Limelight IMU. This includes gyro and accelerometer data.
 */
public class IMUData {

  /**
   * Enum representing different IMU usage modes for robot orientation or localization. Defines how
   * internal and external IMUs are utilized, including seeding and convergence assistance.
   */
  public enum IMUMode {
    /**
     * Use only an external IMU orientation set by setRobotOrientation() for orientation data. No
     * interaction with the internal IMU.
     */
    EXTERNAL_IMU(0),

    /**
     * Use an external IMU orientation set by setRobotOrientation() as the primary source and seed
     * the internal IMU with its data. This should be used for "zeroing" the internal IMU. The
     * internal IMU is initialized or calibrated using external IMU values.
     */
    EXTERNAL_IMU_SEED_INTERNAL(1),

    /** Use only the internal IMU for orientation data. No reliance on an external IMU. */
    INTERNAL_IMU(2),

    /**
     * Use the internal IMU with MT1-assisted convergence. MegaTag1 provides additional data to
     * improve internal IMU accuracy or stability.
     */
    INTERNAL_MT1_ASSISTED(3),

    /**
     * Use the internal IMU with external IMU-assisted convergence. The external IMU provides
     * supplementary data to enhance internal IMU performance.
     */
    INTERNAL_EXTERNAL_ASSISTED(4);

    private final int modeValue;

    IMUMode(int modeValue) {
      this.modeValue = modeValue;
    }

    /**
     * Returns the integer value associated with this IMU mode.
     *
     * @return The mode value (0 for EXTERNAL_IMU, 1 for EXTERNAL_IMU_SEED_INTERNAL, etc.)
     */
    public int getModeValue() {
      return modeValue;
    }

    /**
     * Returns the IMUMode corresponding to the given integer value.
     *
     * @param value The integer value to convert to an IMUMode
     * @return The matching IMUMode, or null if no match is found
     */
    public static IMUMode fromValue(int value) {
      for (IMUMode mode : IMUMode.values()) {
        if (mode.modeValue == value) {
          return mode;
        }
      }
      return null;
    }
  }

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
    this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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
