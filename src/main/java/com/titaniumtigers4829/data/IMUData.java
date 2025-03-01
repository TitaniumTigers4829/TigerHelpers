package com.titaniumtigers4829.data;

/**
 * Encapsulates the state of an internal Limelight IMU. This includes gyro and accelerometer data.
 */
public record IMUData(
    // TODO: these javadoc comments could be incorrect, I guessed accel is in m/s^2, but could be
    // gs, also I'm not sure if roll and pitch are for the robot or the imu
    /** The yaw of the robot in degrees. */
    double robotYaw,
    /** The pitch of the IMU in degrees. */
    double roll,
    /** The roll of the IMU in degrees. */
    double pitch,
    /** The yaw of the IMU in degrees. */
    double yaw,
    /** The X-axis gyro rate in degrees per second. */
    double gyroX,
    /** The Y-axis gyro rate in degrees per second. */
    double gyroY,
    /** The Z-axis gyro rate in degrees per second. */
    double gyroZ,
    /** The X-axis acceleration in m/s^2. */
    double accelX,
    /** The Y-axis acceleration in m/s^2. */
    double accelY,
    /** The Z-axis acceleration in m/s^2. */
    double accelZ) {

  /** Initializes an "empty" IMUData record with default values */
  public IMUData() {
    this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  }

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
}
