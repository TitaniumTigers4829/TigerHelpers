package com.titaniumtigers4829.data;

import edu.wpi.first.math.geometry.Pose2d;
import java.util.Arrays;

/**
 * Represents a Limelight pose estimate from Limelight's NetworkTables output. This is for any
 * botpose estimate, so MegaTag1 or MegaTag2
 */
public record PoseEstimate(
    /** The estimated 2D pose of the robot, including position and rotation. */
    Pose2d pose,
    /** The timestamp of the pose estimate in seconds since the Limelight booted up. */
    double timestampSeconds,
    /** The latency of the pose estimate in milliseconds. */
    double latency,
    /** The number of april tags used to calculate the pose estimate. */
    int tagCount,
    /** The max distance, in meters, between april tags used to calculate the pose estimate. */
    double tagSpan,
    /**
     * The average distance, in meters, between the Limelight and the april tags used to calculate
     * the pose estimate.
     */
    double avgTagDist,
    /** The average area, in square meters, of april tags used to calculate the pose estimate. */
    double avgTagArea,
    /** An array of RawFiducial used to calculate the pose estimate. */
    RawFiducial[] rawFiducials,
    /** true if the pose estimate is calculated using MegaTag2, false if using MegaTag1. */
    boolean isMegaTag2,
    /** The botpose data type used to calculate the pose estimate. */
    Botpose botpose) {

  /** Initializes an "empty" PoseEstimate record with default values */
  public PoseEstimate() {
    this(new Pose2d(), 0, 0, 0, 0, 0, 0, new RawFiducial[0], false, Botpose.BLUE_MEGATAG1);
  }

  /**
   * This enum represents the different types of botpose data that can be retrieved from the
   * Limelight. Each constant corresponds to a specific NetworkTables entry name and indicates
   * whether it uses the MegaTag2 algorithm or the original MegaTag1 algorithm for pose estimation.
   */
  public static enum Botpose {
    /**
     * Botpose data for the blue alliance using the MegaTag1 algorithm. Retrieved from the
     * "botpose_wpiblue" NetworkTables entry.
     */
    BLUE_MEGATAG1("botpose_wpiblue", false),
    /**
     * Botpose data for the blue alliance using the MegaTag2 algorithm. Retrieved from the
     * "botpose_orb_wpiblue" NetworkTables entry.
     */
    BLUE_MEGATAG2("botpose_orb_wpiblue", true),
    /**
     * Botpose data for the red alliance using the MegaTag1 algorithm. Retrieved from the
     * "botpose_wpired" NetworkTables entry.
     */
    RED_MEGATAG1("botpose_wpired", false),
    /**
     * Botpose data for the red alliance using the MegaTag2 algorithm. Retrieved from the
     * "botpose_orb_wpired" NetworkTables entry.
     */
    RED_MEGATAG2("botpose_orb_wpired", true);

    private final String entryName;
    private final boolean isMegaTag2;

    Botpose(String entryName, boolean isMegaTag2) {
      this.entryName = entryName;
      this.isMegaTag2 = isMegaTag2;
    }

    /**
     * Gets the entry name for the botpose type. This is the name of the network table entry that
     * contains the botpose data.
     *
     * @return The network table entry name
     */
    public String getEntryName() {
      return entryName;
    }

    /**
     * Gets the botpose type from the entry name.
     *
     * @param entryName The entry name to get the botpose type for
     * @return The botpose type, or null if the entry name is not recognized
     */
    public static Botpose fromEntryName(String entryName) {
      for (Botpose botpose : Botpose.values()) {
        if (botpose.getEntryName().equals(entryName)) {
          return botpose;
        }
      }
      return null;
    }

    /**
     * Gets if the botpose is calculated using MegaTag2.
     *
     * @return True if the botpose is calculated using MegaTag2, false if using MegaTag1
     */
    public boolean isMegaTag2() {
      return isMegaTag2;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    PoseEstimate that = (PoseEstimate) obj;
    // We don't compare the timestampSeconds as it isn't relevant for equality and makes
    // unit testing harder
    return Double.compare(that.latency(), latency) == 0
        && tagCount == that.tagCount()
        && Double.compare(that.tagSpan(), tagSpan) == 0
        && Double.compare(that.avgTagDist(), avgTagDist) == 0
        && Double.compare(that.avgTagArea(), avgTagArea) == 0
        && pose.equals(that.pose())
        && Arrays.equals(rawFiducials, that.rawFiducials());
  }

  /**
   * Checks if the PoseEstimate is valid. If this is true, it means the PoseEstimate has valid data
   * from at least one april tag.
   *
   * @return True if the PoseEstimate is valid, false otherwise
   */
  public boolean isValidPoseEstimate() {
    return rawFiducials != null && rawFiducials.length != 0;
  }
}
