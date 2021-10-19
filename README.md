# QuadPay Android SDK

This is standard Quadpay toolkit for Android, used for full integrations and consumed by the React Native Bridge.

# Requirements to use and publish
- [Gradle](https://gradle.org/releases/): Note - Mac / Unix / Linux users can download this with apt-get
- [Android Studio](https://developer.android.com/studio/?gclid=Cj0KCQjwsLWDBhCmARIsAPSL3_0h0gvZcAQ8FS3T1-btXWWbwre8NlIogVC2Xi3_Sww1bPKmtIHlFI8aAubIEALw_wcB&gclsrc=aw.ds): For development and publishing
- [Access to Sonatype Nexus](https://s01.oss.sonatype.org/index.html): For publishing and releasing artifacts. You need to have valid user credentials with the com.quadpay organization to do this.

# How To Use
Simply navigate to the 'Run' tab of Android Studio and run the example - This will begin an Android simulation of the Quadpay checkout.

# How To Publish
1. Replace **ossrhUsername** and **ossrhPassword** in the gradle.properties file with valid nexus credentials. These are the same credentials used to log into Sonatype.
2. Run `gradle wrapper` in the Android Studio terminal in the top-level repository. This will generate the gradle wrapper.
3. Run `gradlew build` in the Android Studio terminal in the top-level repository. This will build necessary artifacts and the aar (Android Studio SDK).
4. Run `gradlew createJar` in the Android Studio terminal in the top-level repository. This will generate the quadpay versioned jar that contains all of the class files.
5. In publish.gradle, replace signingKey in the 'signing' section with the entire file contents of keyfile.txt in Quadpay Android SDK Secret Key in 1Password. This is a long block of text and should have a newline at the end.
6. In publish.gradle, replace signingPassword in the 'signing' section with Quadpay Android SDK Secret Key passphrase's value from 1Password.
7. Run `gradlew publish`. This will push all of the required artifacts for publish to a staging repository in Nexus.
8. Visit Nexus at https://s01.oss.sonatype.org/index.html#stagingRepositories and login. Then click 'Staging Repositories' on the left sidebar.
Note: You can only have two staging repositories per project at a time - If you need to push another and already have two, select one
of the unused ones and click 'Drop' in the toolbar immediately above it, and then 'Refresh' to the left of that and see the dropped repository should no longer be present.
9. Select the desired repository for release and then select 'Close' in the top toolbar. After about a minute, click 'Refresh' and you should see that validation has passed on the project.
10. Select 'Release' on the top toolbar with the closed repository selected. Do not toggle defaults and confirm. This will clear your staging repository.
11. After about 10 minutes, you should see your newly versioned project in its own folder [here](https://repo1.maven.org/maven2/com/quadpay/quadpay).