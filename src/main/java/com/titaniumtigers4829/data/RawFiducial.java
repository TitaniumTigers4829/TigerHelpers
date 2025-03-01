package com.titaniumtigers4829.data;

/** Represents a Limelight Raw Fiducial result from Limelight's NetworkTables output. */
public record RawFiducial(
    /** The id of the april tag */
    int id,
    /**
     * The horizontal offset of the target from the Limelight's principle pixel (the image's central
     * pixel) in degrees. Positive values mean the target is to the right of the crosshair.
     */
    double txnc,
    /**
     * The vertical offset of the target from the Limelight's principle pixel (the image's central
     * pixel) in degrees. Positive values mean the target is below the crosshair.
     */
    double tync,
    /**
     * The area of the target in the Limelight's view as a percentage of the total image area. So,
     * if the target takes up 10% of the image, this value will be 0.1.
     */
    double ta,
    /** The distance from the Limelight to the april tag in meters. */
    double distToCamera,
    /** The distance from the robot to the april tag in meters. */
    double distToRobot,
    /**
     * The ambiguity of the april tag. This ranges from 0 to 1, a lower value meaning less
     * ambiguous. The higher this is, the more likely it is that you will see ambiguity flipping.
     * Ambiguity flipping is when there are multiple possible "solutions" for the pose estimate, so
     * if this is higher your estimate will be less reliable. The best way to reduce this is to make
     * the april tag look as trapezoidal as possible from the Limelight's perspective.
     */
    double ambiguity) {}
