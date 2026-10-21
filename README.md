# JanjiTemu (Java Spring Boot + React)

Aplikasi sistem reservasi dan booking konsultasi/janji temu (layanan hukum) yang telah di-rewrite dari Node.js ke **Java 21 LTS** menggunakan framework **Spring Boot 3.4.2** dengan frontend **React 19 + Vite + Tailwind CSS**.

---

## 📁 Struktur Monorepo

```text
janjiTemu-Java/
├── backend/                       # Backend Java Spring Boot
│   ├── pom.xml                    # Maven build & dependencies
│   ├── mvnw & mvnw.cmd            # Maven Wrapper bawaan
│   ├── run.bat                    # Script run khusus backend
│   └── src/
│       ├── main/resources/
│       │   └── application.properties # Konfigurasi port, MongoDB Atlas, & JWT
│       └── main/java/com/janjitemu/app/
│           ├── JanjitemuApplication.java
│           ├── controller/        # REST API Controllers (Auth, Services, Appointments)
│           ├── dto/               # Request & Response Data Transfer Objects
│           ├── model/             # MongoDB Document Entities (User, ServiceItem, Appointment)
│           ├── repository/        # Spring Data MongoDB Repositories
│           ├── security/          # Spring Security, JWT Auth Filter, & BCrypt
│           └── service/           # Seeder admin & layanan otomatis
│
├── frontend/                      # Frontend React 19 + Vite + Tailwind CSS
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── App.jsx                # UI Reservasi, Dashboard Admin & Customer
│       ├── main.jsx
│       └── index.css
│
├── run-all.bat                    # Script 1-klik untuk menjalankan Backend & Frontend sekaligus
├── run-backend.bat                # Script 1-klik menjalankan Backend saja
└── run-frontend.bat               # Script 1-klik menjalankan Frontend saja
```

---

## ⚡ Fitur Utama

1. **Role-Based Access Control (RBAC)**:
   - **Admin**: Mengelola seluruh janji temu, mengubah status (Approved/Rejected/Completed), melihat sesi hari ini, menambah booking walk-in, dan mengelola CRUD layanan.
   - **Customer**: Mendaftar akun, mengajukan janji temu sesuai layanan yang tersedia, dan melihat riwayat pengajuan milik sendiri.
2. **Autentikasi Fleksibel**:
   - Pengguna bisa login menggunakan **Email** maupun **Username**.
   - Password terenkripsi menggunakan **BCrypt**.
   - Token otentikasi berbasis **JWT (JSON Web Token)** HMAC SHA-256.
3. **Data Seeding Otomatis**:
   - Begitu server backend pertama kali dijalankan dan terhubung ke MongoDB Atlas, sistem otomatis menginisialisasi akun admin bawaan (`admin@janjitemu.com` / `admin123`) serta 4 data layanan awal (Badan Usaha, Perdata, Tata Negara, OPHI).
4. **Kompatibilitas Penuh**:
   - Format `_id` MongoDB tetap dipertahankan dengan anotasi `@JsonProperty("_id")` sehingga frontend React tidak mengalami kendala.

---

## 🚀 Cara Menjalankan

### Cara Termudah (Windows 1-Click)
Cukup klik dua kali file [**`run-all.bat`**](file:///c:/Users/oscar/janjiTemu-Java/run-all.bat) di folder root.

### Menjalankan Manual via Terminal

#### 1. Backend (Java 21)
```powershell
cd backend
$env:JAVA_HOME = "C:\Program Files\Java"
.\mvnw.cmd spring-boot:run
```
Backend berjalan di: **`http://localhost:5000`**

#### 2. Frontend (React)
```powershell
cd frontend
npm install
npm run dev
```
Frontend berjalan di: **`http://localhost:5173`**
