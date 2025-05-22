# ToDo App - Kotlin Edition

## Description

This is a simple yet functional ToDo application for Android. It helps users manage their daily tasks, set reminders, and keep track of their to-do items. The application supports different themes (Light and Dark) for a personalized user experience.

This project was originally written in Java and has been **fully converted to Kotlin**, showcasing modern Android development practices.

## Features

*   **Add and Manage Tasks**: Easily add new tasks with titles, dates, and times.
*   **Delete Tasks**: Remove tasks once completed or no longer needed.
*   **Task Reminders**: Set notifications to get reminded about specific tasks.
*   **Theme Switching**: Choose between a Light and Dark theme for visual comfort.
*   **Persistent Storage**: Tasks are saved locally using Room database.
*   **MVVM Architecture**: Utilizes ViewModel, LiveData, and Repository patterns.

## Screenshots

Here's a glimpse of the application:

### Main Screen

| Light Theme                             | Dark Theme                              |
| :--------------------------------------: | :-------------------------------------: |
| ![Main Light](screenshots/main_light.jpg) | ![Main Dark](screenshots/main_dark.jpg) |

### Add Task Dialog

| Light Theme                               | Dark Theme                                |
| :---------------------------------------: | :--------------------------------------: |
| ![Dialog Light](screenshots/dialog_light.jpg) | ![Dialog Dark](screenshots/dialog_dark.jpg) |

## Build Instructions

### Prerequisites

*   **Android Studio**: Android Studio Hedgehog (2023.1.1) or newer is recommended.
*   **Android SDK**: Ensure you have the Android SDK installed and configured (typically handled by Android Studio). The project uses SDK version 34.
*   **Java Development Kit (JDK)**: JDK 17 or compatible is recommended, usually bundled with recent Android Studio versions.

### Steps to Build

1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd <repository-directory>
    ```
    *(Replace `<repository-url>` and `<repository-directory>` with the actual URL and desired local directory name.)*

2.  **Import into Android Studio**:
    *   Open Android Studio.
    *   Select "Open" or "Import Project".
    *   Navigate to the cloned repository directory and select it.
    *   Android Studio will automatically sync the Gradle project.

3.  **Build using Gradle**:
    *   **Using Android Studio**: Click on `Build > Make Project` from the menu, or click the "Build" button (hammer icon).
    *   **Using Gradle Wrapper (command line)**:
        Open a terminal in the project root directory and run:
        ```bash
        ./gradlew build
        ```
        (On Windows, you might need to use `gradlew.bat build`)

        *Note: The first build might take some time as Gradle downloads necessary dependencies. If you encounter issues related to the Android SDK location, ensure you have an `ANDROID_HOME` environment variable set, or create a `local.properties` file in the project root with the `sdk.dir` property pointing to your Android SDK installation path (e.g., `sdk.dir=/Users/YourUser/Library/Android/sdk` on macOS or `sdk.dir=C:\\Users\\YourUser\\AppData\\Local\\Android\\Sdk` on Windows).*

## Running the App

1.  **Set up an Emulator or Device**:
    *   **Emulator**: In Android Studio, go to `Tools > Device Manager` and create a new Android Virtual Device (AVD) if you don't have one.
    *   **Physical Device**: Enable Developer Options and USB Debugging on your Android device, then connect it to your computer via USB.

2.  **Run from Android Studio**:
    *   Select your target emulator or device from the dropdown menu in the toolbar.
    *   Click the "Run" button (green play icon) or select `Run > Run 'app'` from the menu.

Android Studio will build the app, install it on the selected emulator/device, and automatically launch it.

## Tech Stack & Libraries

*   **Kotlin**: The entire application is written in Kotlin.
*   **AndroidX Libraries**:
    *   **AppCompat**: For backward compatibility of UI features.
    *   **ConstraintLayout**: For flexible and efficient UI design.
    *   **Material Components**: For modern Material Design UI elements.
    *   **Lifecycle (ViewModel, LiveData)**: For building lifecycle-aware components and managing UI-related data.
    *   **Room Persistence Library**: For local database storage.
*   **Lottie**: For rendering After Effects animations.
*   **Gradle**: For build automation.
*   **JUnit & Espresso**: For unit and instrumented testing.
