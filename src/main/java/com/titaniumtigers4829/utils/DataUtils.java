package com.titaniumtigers4829.utils;

import com.titaniumtigers4829.data.fiducial.RawFiducial;
import com.titaniumtigers4829.data.pose.Botpose;
import com.titaniumtigers4829.data.pose.PoseEstimate;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.TimestampedDoubleArray;

public class DataUtils {

  /**
   * Takes a 6-length array of pose data and converts it to a Pose3d object. Array format: [x, y, z,
   * roll, pitch, yaw] where angles are in degrees.
   *
   * @param inData Array containing pose data [x, y, z, roll, pitch, yaw]
   * @return Pose3d object representing the pose, or empty Pose3d if invalid data
   */
  public static Pose3d toPose3D(double[] inData) {
    if (inData.length < 6) {
      return new Pose3d();
    }
    return new Pose3d(
        new Translation3d(inData[0], inData[1], inData[2]),
        new Rotation3d(
            Units.degreesToRadians(inData[3]),
            Units.degreesToRadians(inData[4]),
            Units.degreesToRadians(inData[5])));
  }

  /**
   * Takes a 6-length array of pose data and converts it to a Pose2d object. Uses only x, y, and yaw
   * components, ignoring z, roll, and pitch. Array format: [x, y, z, roll, pitch, yaw] where angles
   * are in degrees.
   *
   * @param inData Array containing pose data [x, y, z, roll, pitch, yaw]
   * @return Pose2d object representing the pose, or empty Pose2d if invalid data
   */
  public static Pose2d toPose2D(double[] inData) {
    if (inData.length < 6) {
      return new Pose2d();
    }
    Translation2d tran2d = new Translation2d(inData[0], inData[1]);
    Rotation2d r2d = new Rotation2d(Units.degreesToRadians(inData[5]));
    return new Pose2d(tran2d, r2d);
  }

  /**
   * Converts a Pose3d object to an array of doubles in the format [x, y, z, roll, pitch, yaw].
   * Translation components are in meters, rotation components are in degrees.
   *
   * @param pose The Pose3d object to convert
   * @return A 6-element array containing [x, y, z, roll, pitch, yaw]
   */
  public static double[] pose3dToArray(Pose3d pose) {
    double[] result = new double[6];
    result[0] = pose.getTranslation().getX();
    result[1] = pose.getTranslation().getY();
    result[2] = pose.getTranslation().getZ();
    result[3] = Units.radiansToDegrees(pose.getRotation().getX());
    result[4] = Units.radiansToDegrees(pose.getRotation().getY());
    result[5] = Units.radiansToDegrees(pose.getRotation().getZ());
    return result;
  }

  /**
   * Converts a Pose2d object to an array of doubles in the format [x, y, z, roll, pitch, yaw].
   * Translation components are in meters, rotation components are in degrees. Note: z, roll, and
   * pitch will be 0 since Pose2d only contains x, y, and yaw.
   *
   * @param pose The Pose2d object to convert
   * @return A 6-element array containing [x, y, 0, 0, 0, yaw]
   */
  public static double[] pose2dToArray(Pose2d pose) {
    double[] result = new double[6];
    result[0] = pose.getTranslation().getX();
    result[1] = pose.getTranslation().getY();
    result[2] = 0;
    result[3] = Units.radiansToDegrees(0);
    result[4] = Units.radiansToDegrees(0);
    result[5] = Units.radiansToDegrees(pose.getRotation().getRadians());
    return result;
  }

  public static double extractArrayEntry(double[] inData, int position) {
    if (inData.length < position + 1) {
      return 0;
    }
    return inData[position];
  }

  public static PoseEstimate unpackBotPoseEstimate(
      String limelightName, String entryName, boolean isMegaTag2) {
    DoubleArrayEntry poseEntry = NTUtils.getLimelightDoubleArrayEntry(limelightName, entryName);
    Botpose botpose = Botpose.fromEntryName(entryName);

    TimestampedDoubleArray tsValue = poseEntry.getAtomic();
    double[] poseArray = tsValue.value;
    long timestamp = tsValue.timestamp;

    if (poseArray.length == 0) {
      // Handle the case where no data is available
      return new PoseEstimate();
    }

    Pose2d pose = toPose2D(poseArray);
    double latency = extractArrayEntry(poseArray, 6);
    int tagCount = (int) extractArrayEntry(poseArray, 7);
    double tagSpan = extractArrayEntry(poseArray, 8);
    double tagDist = extractArrayEntry(poseArray, 9);
    double tagArea = extractArrayEntry(poseArray, 10);

    // Convert server timestamp from microseconds to seconds and adjust for latency
    double adjustedTimestamp = (timestamp / 1000000.0) - (latency / 1000.0);

    RawFiducial[] rawFiducials = new RawFiducial[tagCount];
    int valsPerFiducial = 7;
    int expectedTotalVals = 11 + valsPerFiducial * tagCount;

    if (poseArray.length != expectedTotalVals) {
      // Don't populate fiducials
    } else {
      for (int i = 0; i < tagCount; i++) {
        int baseIndex = 11 + (i * valsPerFiducial);
        int id = (int) poseArray[baseIndex];
        double txnc = poseArray[baseIndex + 1];
        double tync = poseArray[baseIndex + 2];
        double ta = poseArray[baseIndex + 3];
        double distToCamera = poseArray[baseIndex + 4];
        double distToRobot = poseArray[baseIndex + 5];
        double ambiguity = poseArray[baseIndex + 6];
        rawFiducials[i] = new RawFiducial(id, txnc, tync, ta, distToCamera, distToRobot, ambiguity);
      }
    }

    return new PoseEstimate(
        pose,
        adjustedTimestamp,
        latency,
        tagCount,
        tagSpan,
        tagDist,
        tagArea,
        rawFiducials,
        isMegaTag2,
        botpose);
  }
}
