package com.titaniumtigers4829.utils;

import java.util.HashMap;
import java.util.Map;

public class FiducialDownscaleUtils {

  /** Predefined map of downscale values */
  private static final Map<Float, Integer> DOWNSCALE_MAP = new HashMap<>();

  private FiducialDownscaleUtils() {}

  /** Fills the map with predefined downscale values */
  static {
    DOWNSCALE_MAP.put(1.0F, 1);
    DOWNSCALE_MAP.put(1.5F, 2);
    DOWNSCALE_MAP.put(2F, 3);
    DOWNSCALE_MAP.put(3F, 4);
    DOWNSCALE_MAP.put(4F, 5);
  }

  /**
   * Downscale ceiling convertor
   *
   * <p>Compares incoming float downscale value to float values in the map (keys) and returns the
   * map integer value of matched one.
   *
   * <p>If incoming downscale value does not match the defines in the map (which should never happen
   * but just in case) default value 1 will be returned
   *
   * @param downscale raw float
   * @return int downscale, default 1
   */
  public static int convertDownscale(float downscale) {
    return DOWNSCALE_MAP.keySet().stream()
        .filter(k -> k.floatValue() == downscale)
        .findFirst()
        .map(DOWNSCALE_MAP::get)
        .orElse(1);
  }
}
