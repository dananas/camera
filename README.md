# Camera library
Android library for easy camera preview rendering.
You don't need to worry about camera errors because library does it for you.

## How to use it
### Import with gradle:
```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/dananas/android"
    }
}

dependencies {
    implementation "com.github.dananas:camera:0.2.0"
}
```
### Create instance of CameraStarter through CameraStarterFactory
```kotlin
val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
val cameraStarter = CameraStarterFactory.create(manager)
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