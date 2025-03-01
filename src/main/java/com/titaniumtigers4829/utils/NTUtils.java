package com.titaniumtigers4829.utils;

import com.titaniumtigers4829.data.PoseEstimate;
import com.titaniumtigers4829.data.RawFiducial;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NTUtils {

  private static NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();

  private static final Map<String, DoubleArrayEntry> doubleArrayEntries = new ConcurrentHashMap<>();

  /**
   * Gets the NetworkTableInstance used for Limelight communication.
   *
   * @return The NetworkTableInstance used for Limelight communication
   */
  public static NetworkTableInstance getNetworkTableInstance() {
    return networkTableInstance;
  }

  /**
   * Sets the NetworkTableInstance to use for Limelight communication. Normally it it uses the
   * default instance. This is useful for unit testing when you might need multiple instances.
   */
  public static void setNetworkTableInstance(NetworkTableInstance newNetworkTableInstance) {
    networkTableInstance = newNetworkTableInstance;
    clearCaches();
    flushNetworkTable();
  }

  /**
   * Clears any static caches held in TigerHelpers. This should be called in your test setup (or
   * teardown) to ensure a fresh state between tests.
   */
  public static void clearCaches() {
    doubleArrayEntries.clear();
  }

  /**
   * Flushes the NetworkTables to ensure that any newly updated entry is immediately sent to the
   * network.
   */
  public static void flushNetworkTable() {
    networkTableInstance.flush();
  }

  /**
   * Gets the NetworkTable for a Limelight camera.
   *
   * @param tableName The name of the Limelight set in the UI ("" for default)
   * @return NetworkTable for the Limelight
   */
  public static NetworkTable getLimelightNetworkTable(String tableName) {
    return networkTableInstance.getTable(sanitizeName(tableName));
  }

  /**
   * Gets a NetworkTableEntry from a Limelight camera.
   *
   * @param tableName The name of the Limelight set in the UI ("" for default)
   * @param entryName Name of the entry to get
   * @return NetworkTableEntry for the entry name
   */
  public static NetworkTableEntry getLimelightNetworkTableEntry(
      String tableName, String entryName) {
    return getLimelightNetworkTable(tableName).getEntry(entryName);
  }

  /**
   * Gets a NetworkTableEntry from a Limelight camera as a double.
   *
   * @param tableName The name of the Limelight set in the UI ("" for default)
   * @param entryName Name of the entry to get
   * @return double value of the entry
   */
  public static double getLimelightNetworkTableDouble(String tableName, String entryName) {
    return getLimelightNetworkTableEntry(tableName, entryName).getDouble(0.0);
  }

  /**
   * Sets a NetworkTableEntry from a Limelight camera as a double.
   *
   * @param tableName The name of the Limelight set in the UI ("" for default)
   * @param entryName Name of the entry to set
   * @param val Value to set the entry to
   */
  public static void setLimelightNetworkTableDouble(
      String tableName, String entryName, double val) {
    getLimelightNetworkTableEntry(tableName, entryName).setDouble(val);
  }

  /**
   * Gets a NetworkTableEntry from a Limelight camera as a double array.
   *
   * @param tableName The name of the Limelight set in the UI ("" for default)
   * @param entryName Name of the entry to get
   * @return double array value of the entry
   */
  public static double[] getLimelightNetworkTableDoubleArray(String tableName, String entryName) {
    return getLimelightNetworkTableEntry(tableName, entryName).getDoubleArray(new double[0]);
  }

  /**
   * Sets a NetworkTableEntry from a Limelight camera as a double array. If you want to the network
   * table array for a {@link PoseEstimate}, use {@link #setBotPoseEstimate(PoseEstimate, String)},
   * if for the {@link RawFiducial}s use {@link #setRawFiducials(RawFiducial[], String)}.
   *
   * @param tableName The name of the Limelight set in the UI ("" for default)
   * @param entryName Name of the entry to set
   * @param val Value to set the entry to
   */
  public static void setLimelightNetworkTableDoubleArray(
      String tableName, String entryName, double[] val) {
    getLimelightNetworkTableEntry(tableName, entryName).setDoubleArray(val);
  }

  /**
   * Gets a DoubleArrayEntry from a Limelight camera. This is useful for getting raw pose data.
   *
   * @param tableName The name of the Limelight set in the UI ("" for default)
   * @param entryName Name of the entry to get
   * @return DoubleArrayEntry for the entry name
   */
  public static DoubleArrayEntry getLimelightDoubleArrayEntry(String tableName, String entryName) {
    String key = tableName + "/" + entryName;
    return doubleArrayEntries.computeIfAbsent(
        key,
        k -> {
          NetworkTable table = getLimelightNetworkTable(tableName);
          return table.getDoubleArrayTopic(entryName).getEntry(new double[0]);
        });
  }

  private static final String sanitizeName(String name) {
    if (name.equals("") || name.equals(null)) {
      return "limelight";
    }
    return name;
  }
}
