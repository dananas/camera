# Camera library
Android library for easy camera preview rendering.
You don't need to worry about camera errors because library does it for you.

[ ![Download](https://api.bintray.com/packages/dananas/android/camera/images/download.svg) ](https://bintray.com/dananas/android/camera/_latestVersion)

## How to use it
### Import with gradle:
```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/dananas/android"
    }
}

dependencies {
    implementation "com.github.dananas:camera:0.3.0"
}
```
### Create instance of CameraStarter through CameraStarterFactory
```kotlin
val cameraStarter = CameraStarterFactory.create(context = this)
```

### Start and stop camera when needed
```kotlin
cameraStarter.start(cameraId = "0", listOf(surfaceHolder.surface))
// later on
cameraStarter.stop()
// note that you should always call stop when you done working with camera.
```
#### For more info see camera-test application module.

## License
MIT