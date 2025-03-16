// TigerHelpers v1.1.0 (REQUIRES LLOS 2025.0 OR LATER)

package com.titaniumtigers4829;

import com.titaniumtigers4829.data.fiducial.FiducialConstants;
import com.titaniumtigers4829.data.fiducial.RawFiducial;
import com.titaniumtigers4829.data.imu.IMUData;
import com.titaniumtigers4829.data.imu.IMUData.IMUMode;
import com.titaniumtigers4829.data.imu.IMUDataConstants;
import com.titaniumtigers4829.data.networktables.NetworkTablesEntries;
import com.titaniumtigers4829.data.pose.Botpose;
import com.titaniumtigers4829.data.pose.PoseEstimate;
import com.titaniumtigers4829.data.pose.PoseEstimateConstants;
import com.titaniumtigers4829.utils.DataUtils;
import com.titaniumtigers4829.utils.FiducialDownscaleUtils;
import com.titaniumtigers4829.utils.NTUtils;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * TigerHelpers is built on top of LimelightHelpers, providing a set of static methods and classes
 * for interfacing with Limelights, with a dedicated focus on AprilTag pose estimation via
 * Networktables.
 *
 * <p>It includes additional helper methods to simplify common tasks, improve integration with unit
 * testing, and enhance usability while staying up to date with new Limelight features.
 */
public class TigerHelpers {

  /**
   * Gets if the Limelight have a valid target?
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return True if a valid target is present, false otherwise
   */
  public static boolean getTV(String limelightName) {
    return 1.0 == NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TV);
  }

  /**
   * Gets the horizontal offset from the crosshair to the target in degrees.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Horizontal offset angle in degrees
   */
  public static double getTX(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TX);
  }

  /**
   * Gets the vertical offset from the crosshair to the target in degrees.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Vertical offset angle in degrees
   */
  public static double getTY(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TY);
  }

  /**
   * Gets the horizontal offset from the principal pixel/point to the target in degrees. This is the
   * most accurate 2d metric if you are using a calibrated camera and you don't need adjustable
   * crosshair functionality.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Horizontal offset angle in degrees
   */
  public static double getTXNC(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TXNC);
  }

  /**
   * Gets the vertical offset from the principal pixel/point to the target in degrees. This is the
   * most accurate 2d metric if you are using a calibrated camera and you don't need adjustable
   * crosshair functionality.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Vertical offset angle in degrees
   */
  public static double getTYNC(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TYNC);
  }

  /**
   * Gets the target area as a percentage of the image (0-100%).
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Target area percentage (0-100%)
   */
  public static double getTA(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TA);
  }

  /**
   * Gets the pipeline's processing latency contribution.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pipeline latency in milliseconds
   */
  public static double getLatencyPipeline(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TL);
  }

  /**
   * Gets the capture latency.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Capture latency in milliseconds
   */
  public static double getLatencyCapture(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.CL);
  }

  /**
   * Gets the latest raw fiducial/AprilTag detection results from NetworkTables.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Array of RawFiducial objects containing detection details
   */
  public static RawFiducial[] getRawFiducials(String limelightName) {
    NetworkTableEntry entry =
        NTUtils.getLimelightNetworkTableEntry(limelightName, NetworkTablesEntries.RAW_FIDUCIALS);
    double[] rawFiducialArray = entry.getDoubleArray(new double[0]);
    if (rawFiducialArray.length % FiducialConstants.FIDUCIAL_DATA_SIZE != 0) {
      return new RawFiducial[0];
    }

    int numFiducials = rawFiducialArray.length / FiducialConstants.FIDUCIAL_DATA_SIZE;
    RawFiducial[] rawFiducials = new RawFiducial[numFiducials];

    for (int i = 0; i < numFiducials; i++) {
      int baseIndex = i * FiducialConstants.FIDUCIAL_DATA_SIZE;
      int id = (int) DataUtils.extractArrayEntry(rawFiducialArray, baseIndex);
      double txnc = DataUtils.extractArrayEntry(rawFiducialArray, baseIndex + 1);
      double tync = DataUtils.extractArrayEntry(rawFiducialArray, baseIndex + 2);
      double ta = DataUtils.extractArrayEntry(rawFiducialArray, baseIndex + 3);
      double distToCamera = DataUtils.extractArrayEntry(rawFiducialArray, baseIndex + 4);
      double distToRobot = DataUtils.extractArrayEntry(rawFiducialArray, baseIndex + 5);
      double ambiguity = DataUtils.extractArrayEntry(rawFiducialArray, baseIndex + 6);

      rawFiducials[i] = new RawFiducial(id, txnc, tync, ta, distToCamera, distToRobot, ambiguity);
    }

    return rawFiducials;
  }

  // TODO: deprecate these methods (all the way down to the enum)
  public static double[] getBotPose(String limelightName) {
    return NTUtils.getLimelightNetworkTableDoubleArray(limelightName, NetworkTablesEntries.BOTPOSE);
  }

  public static double getFiducialID(String limelightName) {
    return NTUtils.getLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.TID);
  }

  public static Pose3d getBotPose3d(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(limelightName, NetworkTablesEntries.BOTPOSE);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * (Not Recommended) Gets the robot's 3D pose in the WPILib Red Alliance Coordinate System.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pose3d object representing the robot's position and orientation in Red Alliance field
   *     space
   */
  @Deprecated
  public static Pose3d getBotPose3d_wpiRed(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(
            limelightName, NetworkTablesEntries.BOTPOSE_WPIRED);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * (Recommended) Gets the robot's 3D pose in the WPILib Blue Alliance Coordinate System.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pose3d object representing the robot's position and orientation in Blue Alliance field
   *     space
   */
  @Deprecated
  public static Pose3d getBotPose3d_wpiBlue(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(
            limelightName, NetworkTablesEntries.BOTPOSE_WPIBLUE);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * Gets the robot's 3D pose with respect to the currently tracked target's coordinate system.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pose3d object representing the robot's position and orientation relative to the target
   */
  @Deprecated
  public static Pose3d getBotPose3d_TargetSpace(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(
            limelightName, NetworkTablesEntries.BOTPOSE_TARGETSPACE);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * Gets the camera's 3D pose with respect to the currently tracked target's coordinate system.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pose3d object representing the camera's position and orientation relative to the target
   */
  @Deprecated
  public static Pose3d getCameraPose3d_TargetSpace(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(
            limelightName, NetworkTablesEntries.CAMERAPOSE_TARGETSPACE);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * Gets the target's 3D pose with respect to the camera's coordinate system.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pose3d object representing the target's position and orientation relative to the camera
   */
  @Deprecated
  public static Pose3d getTargetPose3d_CameraSpace(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(
            limelightName, NetworkTablesEntries.TARGETPOSE_CAMERASPACE);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * Gets the target's 3D pose with respect to the robot's coordinate system.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pose3d object representing the target's position and orientation relative to the robot
   */
  @Deprecated
  public static Pose3d getTargetPose3d_RobotSpace(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(
            limelightName, NetworkTablesEntries.TARGETPOSE_ROBOTSPACE);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * Gets the camera's 3D pose with respect to the robot's coordinate system.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return Pose3d object representing the camera's position and orientation relative to the robot
   */
  @Deprecated
  public static Pose3d getCameraPose3d_RobotSpace(String limelightName) {
    double[] poseArray =
        NTUtils.getLimelightNetworkTableDoubleArray(
            limelightName, NetworkTablesEntries.CAMERAPOSE_ROBOTSPACE);
    return DataUtils.toPose3D(poseArray);
  }

  /**
   * Gets the MegaTag1 Pose2d and timestamp for use with WPILib pose estimator
   * (addVisionMeasurement) in the WPILib Blue alliance coordinate system.
   *
   * @param limelightName
   * @return
   */
  @Deprecated
  public static PoseEstimate getBotPoseEstimate_wpiBlue(String limelightName) {
    return DataUtils.unpackBotPoseEstimate(
        limelightName, NetworkTablesEntries.BOTPOSE_WPIBLUE, false);
  }

  /**
   * Gets the MegaTag2 Pose2d and timestamp for use with WPILib pose estimator
   * (addVisionMeasurement) in the WPILib Blue alliance coordinate system. Make sure you are calling
   * {setRobotOrientation()} before calling this method.
   *
   * @param limelightName
   * @return
   */
  @Deprecated
  public static PoseEstimate getBotPoseEstimate_wpiBlue_MegaTag2(String limelightName) {
    return DataUtils.unpackBotPoseEstimate(
        limelightName, NetworkTablesEntries.BOTPOSE_ORB_WPIBLUE, true);
  }

  /**
   * Gets the Pose2d and timestamp for use with WPILib pose estimator (addVisionMeasurement) when
   * you are on the RED alliance
   *
   * @param limelightName
   * @return
   */
  @Deprecated
  public static PoseEstimate getBotPoseEstimate_wpiRed(String limelightName) {
    return DataUtils.unpackBotPoseEstimate(
        limelightName, NetworkTablesEntries.BOTPOSE_WPIRED, false);
  }

  /**
   * Gets the Pose2d and timestamp for use with WPILib pose estimator (addVisionMeasurement) when
   * you are on the RED alliance
   *
   * @param limelightName
   * @return
   */
  @Deprecated
  public static PoseEstimate getBotPoseEstimate_wpiRed_MegaTag2(String limelightName) {
    return DataUtils.unpackBotPoseEstimate(
        limelightName, NetworkTablesEntries.BOTPOSE_ORB_WPIRED, true);
  }

  /**
   * Gets the specified Pose2d for easy use with Odometry vision pose estimator
   * (addVisionMeasurement).
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param botpose the type of botpose to get
   * @return the Pose2d of the robot relative to the origin specified by the botpose
   */
  public static Pose2d getBotPose2d(String limelightName, Botpose botpose) {
    double[] result =
        NTUtils.getLimelightNetworkTableDoubleArray(limelightName, botpose.getEntryName());
    return DataUtils.toPose2D(result);
  }

  /**
   * Gets the Pose2d with the blue-side origin for use with Odometry vision pose estimator
   * (addVisionMeasurement).
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return the Pose2d of the robot relative to the blue-side origin
   */
  public static Pose2d getBotPose2d(String limelightName) {
    return getBotPose2d(limelightName, Botpose.BLUE_MEGATAG1);
  }

  /**
   * Gets the PoseEstimate for the specified {@link Botpose} type.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param botpose the type of botpose to get
   * @return the PoseEstimate object
   */
  public static PoseEstimate getBotPoseEstimate(String limelightName, Botpose botpose) {
    return DataUtils.unpackBotPoseEstimate(
        limelightName, botpose.getEntryName(), botpose.isMegaTag2());
  }

  /**
   * Gets the PoseEstimate with the blue-side origin using MegaTag1.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return the PoseEstimate object
   */
  public static PoseEstimate getBotPoseEstimate(String limelightName) {
    return getBotPoseEstimate(limelightName, Botpose.BLUE_MEGATAG1);
  }

  /**
   * Sets the network table entry for the botpose data. This is useful for setting values for unit
   * testing. The {@link PoseEstimate} does not contain values for the z coordinate, roll, and
   * pitch, so these will be set to 0.
   *
   * @param poseEstimate the pose estimate to set
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param botpose the type of botpose to set (e.g., BLUE_MEGATAG1)
   */
  public static void setBotPoseEstimate(
      PoseEstimate poseEstimate, String limelightName, Botpose botpose) {
    // Because network tables don't support 2D arrays, we need to flatten the data into a 1D array
    // Calculates array size: 11 (for PoseEstimate data) + 7 * number of fiducials (for each raw
    // fiducial)
    int fiducialCount = poseEstimate.rawFiducials().length;
    double[] data =
        new double
            [PoseEstimateConstants.POSE_ESTIMATE_DATA_SIZE
                + FiducialConstants.FIDUCIAL_DATA_SIZE * fiducialCount];

    // Populates the PoseEstimate, matching DataUtils.unpackBotPoseEstimate
    data[0] = poseEstimate.pose().getX(); // x
    data[1] = poseEstimate.pose().getY(); // y
    data[2] = 0.0; // z (Pose2d doesn't use this)
    data[3] = 0.0; // roll (not used)
    data[4] = 0.0; // pitch (not used)
    data[5] = poseEstimate.pose().getRotation().getDegrees(); // yaw
    data[6] = poseEstimate.latency(); // latency
    data[7] = fiducialCount; // tagCount (must match fiducials length)
    data[8] = poseEstimate.tagSpan(); // tagSpan
    data[9] = poseEstimate.avgTagDist(); // avgTagDist
    data[10] = poseEstimate.avgTagArea(); // avgTagArea

    // Add data for each fiducial
    for (int i = 0; i < fiducialCount; i++) {
      int baseIndex =
          PoseEstimateConstants.POSE_ESTIMATE_DATA_SIZE
              + (i * FiducialConstants.FIDUCIAL_DATA_SIZE);
      RawFiducial fid = poseEstimate.rawFiducials()[i];
      data[baseIndex] = fid.id(); // id (cast to double)
      data[baseIndex + 1] = fid.txnc(); // txnc
      data[baseIndex + 2] = fid.tync(); // tync
      data[baseIndex + 3] = fid.ta(); // ta
      data[baseIndex + 4] = fid.distToCamera(); // distToCamera
      data[baseIndex + 5] = fid.distToRobot(); // distToRobot
      data[baseIndex + 6] = fid.ambiguity(); // ambiguity
    }

    // Write the array to NetworkTables
    NTUtils.setLimelightNetworkTableDoubleArray(limelightName, botpose.getEntryName(), data);
  }

  /**
   * Sets the network table entry for the blue-side, MegaTag1 botpose data. This is useful for
   * setting values for unit testing. The {@link PoseEstimate} does not contain values for the z
   * coordinate, roll, and pitch, so these will be set to 0.
   *
   * @param poseEstimate the pose estimate to set
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   */
  public static void setBotPoseEstimate(PoseEstimate poseEstimate, String limelightName) {
    setBotPoseEstimate(poseEstimate, limelightName, Botpose.BLUE_MEGATAG1);
  }

  /**
   * Sets the network table entry for the raw fiducials. This is useful for setting values for unit
   * testing.
   *
   * @param rawFiducials the array of raw fiducials to set
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   */
  public static void setRawFiducials(RawFiducial[] rawFiducials, String limelightName) {
    NetworkTableEntry entry =
        NTUtils.getLimelightNetworkTableEntry(limelightName, NetworkTablesEntries.RAW_FIDUCIALS);
    double[] data = new double[rawFiducials.length * FiducialConstants.FIDUCIAL_DATA_SIZE];

    for (int i = 0; i < rawFiducials.length; i++) {
      int baseIndex = i * FiducialConstants.FIDUCIAL_DATA_SIZE;
      RawFiducial fid = rawFiducials[i];
      data[baseIndex] = (double) fid.id();
      data[baseIndex + 1] = fid.txnc();
      data[baseIndex + 2] = fid.tync();
      data[baseIndex + 3] = fid.ta();
      data[baseIndex + 4] = fid.distToCamera();
      data[baseIndex + 5] = fid.distToRobot();
      data[baseIndex + 6] = fid.ambiguity();
    }

    entry.setDoubleArray(data);
  }

  /**
   * Gets the current IMU data from NetworkTables. IMU data is formatted as [robotYaw, roll, pitch,
   * yaw, gyroX, gyroY, gyroZ, accelX, accelY, accelZ]. Returns all zeros if data is invalid or
   * unavailable.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @return IMUData object containing all current IMU data
   */
  public static IMUData getIMUData(String limelightName) {
    double[] imuData =
        NTUtils.getLimelightNetworkTableDoubleArray(limelightName, NetworkTablesEntries.IMU);
    IMUMode imuMode =
        IMUMode.fromValue(
            (int)
                NTUtils.getLimelightNetworkTableDouble(
                    limelightName, NetworkTablesEntries.IMU_MODE_SET));
    if (imuData == null || imuData.length < IMUDataConstants.IMU_DATA_SIZE) {
      return new IMUData(); // Returns object with all zeros
    }
    return new IMUData(
        imuData[0],
        imuData[1],
        imuData[2],
        imuData[3],
        imuData[4],
        imuData[5],
        imuData[6],
        imuData[7],
        imuData[8],
        imuData[9],
        imuMode);
  }

  /**
   * Sets the priority tag ID for the Limelight camera. This is used to prioritize a specific tag
   * for detection, which can be useful for tracking a specific target.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param ID ID of the tag to prioritize
   */
  public static void setPriorityTagID(String limelightName, int ID) {
    NTUtils.setLimelightNetworkTableDouble(limelightName, NetworkTablesEntries.PRIORITY_ID, ID);
  }

  /**
   * Sets the crop window for the camera. The crop window in the UI must be completely open. This
   * can be useful for increasing the Limelight's frame rate as the camera will only process the
   * cropped area.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param cropXMin Minimum X value (-1 to 1)
   * @param cropXMax Maximum X value (-1 to 1)
   * @param cropYMin Minimum Y value (-1 to 1)
   * @param cropYMax Maximum Y value (-1 to 1)
   */
  public static void setCropWindow(
      String limelightName, double cropXMin, double cropXMax, double cropYMin, double cropYMax) {
    double[] entries = new double[4];
    entries[0] = cropXMin;
    entries[1] = cropXMax;
    entries[2] = cropYMin;
    entries[3] = cropYMax;
    NTUtils.setLimelightNetworkTableDoubleArray(limelightName, NetworkTablesEntries.CROP, entries);
  }

  /**
   * Sets the offset of the april tag tracking point,
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param offsetX Offset in the X direction in meters
   * @param offsetY Offset in the Y direction in meters
   * @param offsetZ Offset in the Z direction in meters
   */
  public static void setFiducial3DOffset(
      String limelightName, double offsetX, double offsetY, double offsetZ) {
    double[] entries = new double[3];
    entries[0] = offsetX;
    entries[1] = offsetY;
    entries[2] = offsetZ;
    NTUtils.setLimelightNetworkTableDoubleArray(
        limelightName, NetworkTablesEntries.FIDUCIAL_OFFSET_SET, entries);
  }

  /**
   * Sets robot orientation values used by MegaTag2 localization algorithm.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param yaw Robot yaw in degrees. 0 = robot facing red alliance wall in FRC
   * @param yawRate (Unnecessary) Angular velocity of robot yaw in degrees per second
   * @param pitch (Unnecessary) Robot pitch in degrees
   * @param pitchRate (Unnecessary) Angular velocity of robot pitch in degrees per second
   * @param roll (Unnecessary) Robot roll in degrees
   * @param rollRate (Unnecessary) Angular velocity of robot roll in degrees per second
   */
  public static void setRobotOrientation(
      String limelightName,
      double yaw,
      double yawRate,
      double pitch,
      double pitchRate,
      double roll,
      double rollRate) {
    double[] entries = new double[6];
    entries[0] = yaw;
    entries[1] = yawRate;
    entries[2] = pitch;
    entries[3] = pitchRate;
    entries[4] = roll;
    entries[5] = rollRate;
    NTUtils.setLimelightNetworkTableDoubleArray(
        limelightName, NetworkTablesEntries.ROBOT_ORIENTATION_SET, entries);
    NTUtils.flushNetworkTable();
  }

  /**
   * Sets robot yaw used by MegaTag2 localization algorithm. Puts 0 for all other orientation
   * values.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param yaw Robot yaw in degrees. 0 = robot facing red alliance wall in FRC
   */
  public static void setRobotOrientation(String limelightName, double yaw) {
    setRobotOrientation(limelightName, yaw, 0, 0, 0, 0, 0);
  }

  /**
   * Configures the IMU mode for MegaTag2 Localization. This method is deprecated, use {@link
   * #setIMUMode(String, IMUMode)} instead.
   *
   * @param limelightName Name/identifier of the Limelight
   * @param imuMode IMU mode, 0-4.
   */
  @Deprecated
  public static void setIMUMode(String limelightName, int imuMode) {
    NTUtils.setLimelightNetworkTableDouble(
        limelightName, NetworkTablesEntries.IMU_MODE_SET, imuMode);
  }

  /**
   * Configures the IMU mode for MegaTag2 Localization
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param imuMode The IMU mode to set, uses the {@link IMUMode} enum.
   */
  public static void setIMUMode(String limelightName, IMUMode imuMode) {
    NTUtils.setLimelightNetworkTableDouble(
        limelightName, NetworkTablesEntries.IMU_MODE_SET, imuMode.getModeValue());
  }

  /**
   * Sets the throttle for the Limelight camera. This is used to reduce the camera's processing
   * load, which can be useful for decreasing temperatures.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param throttle Number of frames to skip between each processed frame. 0 = process every frame,
   *     1 = process every other frame, etc.
   */
  public static void setLimelightThrottle(String limelightName, int throttle) {
    NTUtils.setLimelightNetworkTableDouble(
        limelightName, NetworkTablesEntries.THROTTLE_SET, throttle);
  }

  /**
   * Configures the complementary filter alpha value for IMU Assist Modes (Modes 3 and 4)
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param alpha Defaults to .001. Higher values will cause the internal IMU to converge onto the
   *     assist source more rapidly.
   */
  public static void setIMUAssistAlpha(String limelightName, double alpha) {
    NTUtils.setLimelightNetworkTableDouble(
        limelightName, NetworkTablesEntries.IMU_ASSIST_ALPHA_SET, alpha);
  }

  /**
   * Sets the 3D point-of-interest offset for the current fiducial pipeline.
   * https://docs.limelightvision.io/docs/docs-limelight/pipeline-apriltag/apriltag-3d#point-of-interest-tracking
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param x X offset in meters
   * @param y Y offset in meters
   * @param z Z offset in meters
   */
  public static void setFidcuial3DOffset(String limelightName, double x, double y, double z) {
    double[] entries = new double[3];
    entries[0] = x;
    entries[1] = y;
    entries[2] = z;
    NTUtils.setLimelightNetworkTableDoubleArray(
        limelightName, NetworkTablesEntries.FIDUCIAL_OFFSET_SET, entries);
  }

  /**
   * Overrides the valid AprilTag IDs that will be used for localization. Tags not in this list will
   * be ignored for robot pose estimation.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param validIDs Array of valid AprilTag IDs to track
   */
  public static void setFiducialIDFiltersOverride(String limelightName, int[] validIDs) {
    double[] validIDsDouble = new double[validIDs.length];
    for (int i = 0; i < validIDs.length; i++) {
      validIDsDouble[i] = validIDs[i];
    }
    NTUtils.setLimelightNetworkTableDoubleArray(
        limelightName, NetworkTablesEntries.FIDUCIAL_ID_FILTERS_SET, validIDsDouble);
  }

  /**
   * Sets the downscaling factor for AprilTag detection. Increasing downscale can improve
   * performance at the cost of potentially reduced detection range.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param downscale Downscale factor. Valid values: 1.0 (no downscale), 1.5, 2.0, 3.0, 4.0. Set to
   *     0 for pipeline control.
   */
  public static void setFiducialDownscalingOverride(String limelightName, float downscale) {
    NTUtils.setLimelightNetworkTableDouble(
        limelightName,
        NetworkTablesEntries.FIDUCIAL_DOWNSCALE_SET,
        FiducialDownscaleUtils.convertDownscale(downscale));
  }

  /**
   * Sets the camera pose relative to the robot.
   *
   * @param limelightName The name of the Limelight set in the UI ("" for default)
   * @param forward Forward offset in meters
   * @param side Side offset in meters
   * @param up Up offset in meters
   * @param roll Roll angle in degrees
   * @param pitch Pitch angle in degrees
   * @param yaw Yaw angle in degrees
   */
  public static void setCameraPoseRobotSpace(
      String limelightName,
      double forward,
      double side,
      double up,
      double roll,
      double pitch,
      double yaw) {
    double[] entries = new double[6];
    entries[0] = forward;
    entries[1] = side;
    entries[2] = up;
    entries[3] = roll;
    entries[4] = pitch;
    entries[5] = yaw;
    NTUtils.setLimelightNetworkTableDoubleArray(
        limelightName, NetworkTablesEntries.CAMERAPOSE_ROBOTSPACE_SET, entries);
  }
}
