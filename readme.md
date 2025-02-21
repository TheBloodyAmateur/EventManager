# EventManager

**EventManager** is a logging module designed to be used in a multi-threaded environment. It is designed to be used in a 
way that is thread-safe and can be customised to meet every user's and projects needs.

Find the project documentation [here](https://github.com/TheBloodyAmateur/EventManager/wiki), the Javadoc can be found [here](https://thebloodyamateur.github.io/EventManager/).

You may install the module using the following maven dependency:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.github.TheBloodyAmateur</groupId>
        <artifactId>EventManager</artifactId>
        <version>version</version>
    </dependency>
</dependencies>
```
And for Gradle (Groovy):
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.TheBloodyAmateur:EventManager:version'
}
```
Or for Gradle (Kotlin):
```kotlin
repositories {
    maven("https://jitpack.io")
}
dependencies {
    implementation("com.github.TheBloodyAmateur:EventManager:version")
}
```