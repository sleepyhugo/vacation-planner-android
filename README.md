# 🏝️ Vacation Planner (Android)

An Android app that helps users keep track of vacations and their related excursions.
You can create trips, add excursions (like tours or activities), set date alerts, and share trip details.

---

## ✨ Features

- Create, edit, and delete **vacations**
  - Title, hotel, start date, end date, price
- Add **excursions** linked to a vacation
  - Title, date, price
- **Date alerts** for vacation start and end dates
- **Share vacation details** via other apps (email, messages, etc.)
- Local storage using **Room** (SQLite)

---

## 🛠 Tech Stack

- **Language:** Java
- **Platform:** Android (API 26+ / Android 8.0+)
- **Architecture:** Room + Repository pattern
- **Libraries & Tools:**
  - AndroidX
  - RecyclerView
  - Room (Entity, DAO, Database)
  - AlarmManager for alerts

---

## 📱 Screens

- Home screen – entry point to vacation list
- Vacation list – shows all saved vacations
- Vacation details – edit vacation info and view excursions
- Excursion list – excursions tied to a vacation
- Excursion details – add or edit an excursion

---

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/<your-username>/vacation-planner-android.git
