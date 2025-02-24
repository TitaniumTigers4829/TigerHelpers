# TigerHelpers

TigerHelpers is a refined version of [LimelightHelpers](https://github.com/LimelightVision/limelightlib-wpijava), aimed at being cleaner, better documented, and more focused on AprilTag detection. It will be kept up to date with any new Limelight features and includes additional helper methods aimed at making various things, such as unit testing, easier.

## Download For Your Robot

As of right now, the installation process is the same as LimelightHelpers. Go to [Releases](https://github.com/TitaniumTigers4829/TigerHelpers/releases), download `TigerHelpers.java` and paste it into your project.

## Documentation

Eventually, there will be better docs and examples, but for now theres [Javadoc](http://titaniumtigers4829.com/TigerHelpers/frc/robot/extras/vision/package-summary.html)

## Setup Instructions (Only do this if you want to help contribute)

### Prerequisites
- Install [Java 17+](https://adoptium.net/)
- Install [Gradle](https://gradle.org/) (optional, as the project includes the Gradle wrapper)

### Cloning and Building
1. Clone the repository:
   ```sh
   git clone <repository-url>
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
