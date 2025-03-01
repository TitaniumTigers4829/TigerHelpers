package com.titaniumtigers4829.data;

/** Represents a Limelight Raw Fiducial result from Limelight's NetworkTables output. */
public class RawFiducial {
  /** The id of the april tag */
  public int id = 0;

  /**
   * The horizontal offset of the target from the Limelight's principle pixel (the images central
   * pixel) in degrees. Positive values mean the target is to the right of the crosshair.
   */
  public double txnc = 0;

  /**
   * The vertical offset of the target from the Limelight's principle pixel (the images central
   * pixel) in degrees. Positive values mean the target is below the crosshair.
   */
  public double tync = 0;

  /**
   * The area of the target in the Limelight's view as a percentage of the total image area. So, if
   * the target takes up 10% of the image, this value will be 0.1.
   */
  public double ta = 0;

  /** The distance from the Limelight to the april tag in meters. */
  public double distToCamera = 0;

  /** The distance from the robot to the april tag in meters. */
  public double distToRobot = 0;

  /**
   * The ambiguity of the april tag. This ranges from 0 to 1, a lower values meaning less ambiguous.
   * The higher this is, the more likely it is that you will see ambiguity flipping. Ambiguity
   * flipping is when there are multiple possible "solutions" for the pose estimate, so if this is
   * higher your estimate will be less reliable. The best way to reduce this it to make the april
   * tag look as trapezoidal as possible from the Limelight's perspective.
   */
  public double ambiguity = 0;

  /**
   * Initializes a RawFidicual with the provided values. This represents data from reading a single
   * april tag.
   *
   * @param id The id of the april tag
   * @param txnc The horizontal offset of the target from the Limelight's principle pixel (the
   *     images central pixel) in degrees.
   * @param tync The vertical offset of the target from the Limelight's principle pixel (the images
   *     central pixel) in degrees.
   * @param ta The area of the target in the Limelight's view as a percentage of the total image
   *     area.
   * @param distToCamera The distance from the Limelight to the april tag in meters.
   * @param distToRobot The distance from the robot to the april tag in meters.
   * @param ambiguity The ambiguity of the april tag. This ranges from 0 to 1, a lower values
   *     meaning less ambiguous.
   */
  public RawFiducial(
      int id,
      double txnc,
      double tync,
      double ta,
      double distToCamera,
      double distToRobot,
      double ambiguity) {
    this.id = id;
    this.txnc = txnc;
    this.tync = tync;
    this.ta = ta;
    this.distToCamera = distToCamera;
    this.distToRobot = distToRobot;
    this.ambiguity = ambiguity;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    RawFiducial other = (RawFiducial) obj;
    return id == other.id
        && Double.compare(txnc, other.txnc) == 0
        && Double.compare(tync, other.tync) == 0
        && Double.compare(ta, other.ta) == 0
        && Double.compare(distToCamera, other.distToCamera) == 0
        && Double.compare(distToRobot, other.distToRobot) == 0
        && Double.compare(ambiguity, other.ambiguity) == 0;
  }
}
