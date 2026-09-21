# ClimbingCanvas

ClimbingCanvas is a specialized Android application designed for climbers to track and share bouldering routes (boulders) on "spray walls." It allows users to take or upload photos of their climbing walls and digitally mark routes with precise hold indicators.

## Features

### 🏢 Gym & Wall Management
- **Gym Profiles**: Organize your climbing spots by creating custom gym entries.
- **Spray Wall Library**: Upload high-quality photos of different spray walls within each gym.
- **Persistent Storage**: All images and route data are stored locally using Room Database, with persistable URI permissions to ensure your wall photos are always available.

### 🎨 Interactive Route Setting (Boulder Creation)
- **Precise Hold Placement**: Simply tap on the wall image to add a hold.
- **Drag & Drop**: Fine-tune the position of any hold by dragging it across the screen.
- **Dynamic Resizing**: Use dedicated controls to increase or decrease the size of each hold indicator to match the actual hold on the wall.
- **Hold Categorization**: Color-coded markers for different hold types:
    - 🟢 **Start**: Green markers for starting positions.
    - 🔵 **Hand**: Blue markers for intermediate hand holds.
    - 🟡 **Foot**: Yellow markers for designated foot holds.
    - 🔴 **Top**: Red markers for the finishing hold.
- **Full-Width Canvas**: The interface automatically scales your wall photos to fit the screen width for maximum visibility.

### 🔍 Route Viewing
- **Focus Mode**: When viewing a saved boulder, the background (non-hold areas) is slightly dimmed to make the route pop.
- **Zoom & Pan**: Fully interactive view with pinch-to-zoom and panning support to inspect every detail of the route.
- **Visual Legend**: Integrated color legend to quickly identify hold types.

## Tech Stack
- **Language**: Java
- **Database**: Room Persistence Library (SQLite)
- **UI Components**: Material Design 3 (M3)
- **Architecture**: MVVM with LiveData for reactive UI updates
- **Image Loading**: Glide
- **Gesture Handling**: Custom `ScaleGestureDetector` and `GestureDetector` for the interactive canvas.

## Installation
1. Clone the repository.
2. Open in Android Studio (Koala or later recommended).
3. Build and run on an Android device or emulator (Min SDK 24).
