package com.titaniumtigers4829.data.pose;

import com.titaniumtigers4829.data.fiducial.RawFiducial;
import edu.wpi.first.math.geometry.Pose2d;
import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a Limelight pose estimate from Limelight's NetworkTables output. This is for any
 * botpose estimate, so MegaTag1 or MegaTag2
 */
public record PoseEstimate(
    /** The estimated 2D pose of the robot, including position and rotation. */
    Optional<Pose2d> pose,
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
    /** An Optional array of RawFiducial used to calculate the pose estimate. */
    Optional<RawFiducial[]> rawFiducials,
    /** true if the pose estimate is calculated using MegaTag2, false if using MegaTag1. */
    boolean isMegaTag2,
    /** The botpose data type used to calculate the pose estimate. */
    Botpose botpose) {

  /** Initializes an "empty" PoseEstimate record with default values */
  public PoseEstimate() {
    this(Optional.empty(), 0, 0, 0, 0, 0, 0, Optional.empty(), false, Botpose.BLUE_MEGATAG1);
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
        && rawFiducials.map(Arrays::toString).orElse("").equals(that.rawFiducials().map(Arrays::toString).orElse(""));
  }

  /**
   * Checks if the PoseEstimate is valid. If this is true, it means the PoseEstimate has valid data
   * from at least one april tag.
   *
   * @return True if the PoseEstimate is valid, false otherwise
   */
  public boolean isValidPoseEstimate() {
    return rawFiducials.isPresent() && rawFiducials.get().length != 0;
  }
}
