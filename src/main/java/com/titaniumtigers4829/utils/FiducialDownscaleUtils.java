package com.titaniumtigers4829.utils;

import java.util.HashMap;
import java.util.Map;

public class FiducialDownscaleUtils {

  private static final Map<Float, Integer> DOWNSCALE_MAP = new HashMap<>();

  private FiducialDownscaleUtils() {}

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
   * @param downscale raw float
   * @return int downscale, default 0
   */
  public static int convertDownscale(float downscale) {
    return DOWNSCALE_MAP.keySet().stream()
        .filter(k -> k.floatValue() == downscale)
        .findFirst()
        .map(DOWNSCALE_MAP::get)
        .orElse(0);
  }
}
