# 🏝️ Vacation Planner (Android)

An Android application that helps users organize vacations and manage related excursions.
Users can create trips, add activities, receive date alerts, and share vacation details—all stored locally on the device.

This project was built as part of my Android coursework and demonstrates CRUD operations, local persistence, and clean architecture using Room.

## ✨ Features

- Create, edit, and delete **vacations**
  - Title, hotel, start date, end date, price
- Add **excursions** linked to a vacation
  - Title, date, price
- **Date alerts** for vacation start and end dates
- **Share vacation details** via other apps (email, messages, etc.)
- Local storage using **Room** (SQLite)

## 🛠 Tech Stack

- **Language:** Java
- **Platform:** Android (API 26+ / Android 8.0+)
- **Architecture:** Room + Repository pattern
- **Libraries & Tools:**
  - AndroidX
  - RecyclerView
  - Room (Entity, DAO, Database)
  - AlarmManager for alerts

## 📱 Screens

- Home screen – entry point to vacation list
- Vacation list – shows all saved vacations
- Vacation details – edit vacation info and view excursions
- Excursion list – excursions tied to a vacation
- Excursion details – add or edit an excursion

## 🚀 Getting Started

1. Clone the repo:
   ```
   git clone https://github.com/<your-username>/vacation-planner-android.git
   ```
2. Open the project in Android Studio

3. Build and run the app on an emulator or physical device (Android 8.0+)
