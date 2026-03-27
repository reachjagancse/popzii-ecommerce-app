Popzii Mobile — Customer App

Overview
--------
React Native app for customers to browse, order, and track deliveries.

Features
--------
- Authentication (register/login)
- Product browsing by vendor/category
- Cart and checkout
- Real-time order tracking
- Profile management

Prerequisites
-------------
- Node.js 18+
- Expo CLI
- Android Studio (for emulator) or physical Android device
- Backend running on localhost:4000

Running Locally
---------------
1. Install dependencies:
   ```
   npm install
   ```
2. Start Expo:
   ```
   npm start
   ```
3. Choose platform:
   - Press 'a' for Android
   - Press 'i' for iOS
   - Press 'w' for Web

For Android:
- Install Android Studio
- Create an AVD (Android Virtual Device)
- Start the emulator from Android Studio
- Or use Expo Go on a physical device

Validation
----------
- Register a new user
- Login
- Browse products
- Add to cart, checkout
- View order tracking

API
---
- Base URL: http://10.0.2.2:4000 (emulator)
- Endpoints: /api/auth, /api/products, /api/orders

Troubleshooting Android
-----------------------
If "No Android connected device found":
1. Install Android Studio: https://developer.android.com/studio
2. Open Android Studio > Configure > AVD Manager
3. Create a new Virtual Device (e.g., Pixel 4, API 33)
4. Start the emulator
5. Run `npm start` again and press 'a'

Alternative: Use Web mode (press 'w') for testing UI without native features.

Build for Production
--------------------
- expo build:android
- expo build:ios
