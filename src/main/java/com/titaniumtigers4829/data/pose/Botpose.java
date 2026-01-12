package com.titaniumtigers4829.data.pose;

import com.titaniumtigers4829.data.networktables.NetworkTablesEntries;

/**
 * This enum represents the different types of botpose data that can be retrieved from the
 * Limelight. Each constant corresponds to a specific NetworkTables entry name and indicates whether
 * it uses the MegaTag2 algorithm or the original MegaTag1 algorithm for pose estimation.
 */
public enum Botpose {
  /**
   * Botpose data for the blue alliance using the MegaTag1 algorithm. Retrieved from the
   * "botpose_wpiblue" NetworkTables entry.
   */
  BLUE_MEGATAG1(NetworkTablesEntries.BOTPOSE_WPIBLUE, false),
  /**
   * Botpose data for the blue alliance using the MegaTag2 algorithm. Retrieved from the
   * "botpose_orb_wpiblue" NetworkTables entry.
   */
  BLUE_MEGATAG2(NetworkTablesEntries.BOTPOSE_ORB_WPIBLUE, true),
  /**
   * Botpose data for the red alliance using the MegaTag1 algorithm. Retrieved from the
   * "botpose_wpired" NetworkTables entry.
   */
  RED_MEGATAG1(NetworkTablesEntries.BOTPOSE_WPIRED, false),
  /**
   * Botpose data for the red alliance using the MegaTag2 algorithm. Retrieved from the
   * "botpose_orb_wpired" NetworkTables entry.
   */
  RED_MEGATAG2(NetworkTablesEntries.BOTPOSE_ORB_WPIRED, true);

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
