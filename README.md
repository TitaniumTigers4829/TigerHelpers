# TigerHelpers

TigerHelpers is a refined version of [LimelightHelpers](https://github.com/LimelightVision/limelightlib-wpijava), aimed at being cleaner, better documented, and more focused on AprilTag detection. It will be kept up to date with any new Limelight features and includes additional helper methods aimed at making various things, such as unit testing, easier.

## Download For Your Robot

Add the vendor dep to your project: 
```
http://titaniumtigers4829.com/tigerhelpers.json
```
Migrating from LimelightHelpers to this is pretty easy, just delete `LimelightHelpers.java`, install the vendor dep, and change `LimelightHelpers` to `TigerHelpers` in your code. A few methods might have slightly different names, (e.g. `SetRobotOrientation` is now `setRobotOrientation`), but most are the same. Also, if you were using any properties from `PoseEstimate` or `RawFiducial`, you will need to put parenthesis after them, so change `myPoseEstimate.pose` to `myPoseEstimate.pose()`.  

## Documentation

Eventually, there will be better docs and examples, but for now there's [Javadoc](http://titaniumtigers4829.com/TigerHelpers/index.html)

## Setup Instructions (Only do this if you want to help contribute)

### Prerequisites
- Install [Java 17+](https://adoptium.net/)
- Install [Gradle](https://gradle.org/) (optional, as the project includes the Gradle wrapper)

### Cloning and Building
1. Clone the repository:
   ```sh
   git clone https://github.com/TitaniumTigers4829/TigerHelpers.git
   cd TigerHelpers
   ```
2. Build the project:
   ```sh
   ./gradlew build
   ```
### Other Commands
- Format Code:
   ```sh
   ./gradlew spotlessApply
   ```
- Generate JavaDoc:
   ```sh
   ./gradlew generateDocs
   ```

### Dependencies
Gradle will automatically download all required dependencies, including WPILib libraries. If you run into missing dependencies, try:
```sh
./gradlew clean build
```

### Notes
- The Gradle wrapper (`gradlew`/`gradlew.bat`) is included, so you don't need to install Gradle manually.
