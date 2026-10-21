# JanjiTemu (Full Java Web Application)

Aplikasi web sistem reservasi dan booking konsultasi/janji temu (layanan hukum) yang **100% Full Java (Spring Boot 3.4.2 + Thymeleaf)** tanpa memerlukan Node.js, npm, ataupun runtime JavaScript eksternal.

---

## 🛠 Tech Stack
- **Bahasa**: Java 21 LTS (OpenJDK 21)
- **Framework Web**: Spring Boot 3.4.2 (Spring MVC + Thymeleaf)
- **Database**: MongoDB (Spring Data MongoDB)
- **Security**: Spring Security 6 (Session & FormLogin + BCrypt + JWT API)
- **Styling**: Tailwind CSS (via CDN) + Google Fonts (Plus Jakarta Sans)
- **Build Tool**: Maven Wrapper (`mvnw`) bawaan tanpa perlu install Maven global

---

## 📁 Struktur Direktori Proyek

```text
janjiTemu-Java/
├── pom.xml                                   # Konfigurasi Maven (Spring Boot, Thymeleaf, Security, MongoDB)
├── mvnw & mvnw.cmd                           # Maven Wrapper
├── run.bat                                   # 🚀 1-Klik Menjalankan Aplikasi
├── README.md                                 # Panduan Proyek
└── src/
    ├── main/
    │   ├── resources/
    │   │   ├── application.properties        # Port 5000 & Konfigurasi MongoDB
    │   │   └── templates/                    # Tampilan Web Thymeleaf
    │   │       ├── login.html                # Halaman Masuk
    │   │       ├── register.html             # Halaman Registrasi Customer
    │   │       ├── admin/
    │   │       │   └── dashboard.html        # Admin Dashboard (Sesi Hari Ini, Semua Janji, Walk-in, Layanan)
    │   │       └── customer/
    │   │           └── dashboard.html        # Customer Portal (Form Booking & Riwayat Pengajuan)
    │   └── java/com/janjitemu/app/
    │       ├── JanjitemuApplication.java     # Main Class Spring Boot
    │       ├── controller/                   # Controller Web & REST API
    │       │   ├── HomeController.java       # Routing root "/"
    │       │   ├── WebAuthController.java    # /login, /register-web
    │       │   ├── WebAdminController.java   # /admin (Sesi, filter, approval, CRUD)
    │       │   ├── WebCustomerController.java# /customer (Booking & riwayat)
    │       │   ├── AuthController.java       # REST API /register, /login
    │       │   ├── ServiceController.java    # REST API /services
    │       │   └── AppointmentController.java# REST API /appointments
    │       ├── model/                        # Entity Dokumen MongoDB (User, ServiceItem, Appointment)
    │       ├── repository/                   # Spring Data MongoDB Repository
    │       ├── security/                     # Spring Security, CustomUserDetailsService, & JWT
    │       └── service/                      # DataSeeder (Seed admin & 4 layanan default)
    └── test/
        └── java/com/janjitemu/app/           # Unit & Context Tests
```

---

## ⚡ Fitur Utama

1. **100% Full Java**:
   - Seluruh backend dan frontend (server-side rendering) dihandle langsung oleh Spring Boot.
   - Tidak ada ketergantungan pada Node.js, `npm`, atau `node_modules`.
2. **Role-Based Access Control (RBAC)**:
   - **Admin**:
     - 📊 *Sesi Hari Ini*: Memfilter langsung janji temu yang dijadwalkan pada hari ini dan update status cepat.
     - 📅 *Semua Janji*: Tabel seluruh janji temu dengan badge status (Pending, Approved, Completed, Rejected).
     - 📝 *Booking Walk-in / Telepon*: Form khusus admin untuk mencatat booking klien yang datang langsung.
     - 🛠 *Kelola Layanan*: CRUD layanan hukum (tambah, perbarui, hapus).
   - **Customer**:
     - 📋 *Reservasi Janji*: Form pemilihan bidang hukum, tanggal, dan preferensi jam konsultasi.
     - 📜 *Riwayat Pengajuan*: Daftar janji temu milik akun bersangkutan dan status persetujuannya.
3. **Akun & Layanan Default (Auto-Seeding)**:
   - Saat aplikasi pertama kali terkoneksi ke MongoDB, sistem otomatis membuat akun admin dan 4 layanan hukum default:
     - **Email Admin**: `admin@janjitemu.com`
     - **Password**: `admin123`
4. **Login Fleksibel**:
   - Pengguna dapat masuk menggunakan **Email** maupun **Username**.

---

## 🚀 Cara Menjalankan

### Cara 1: Menggunakan File Batch (1-Klik di Windows)
Cukup klik dua kali file [**`run.bat`**](file:///c:/Users/oscar/janjiTemu-Java/run.bat).

### Cara 2: Menggunakan PowerShell / Terminal
```powershell
$env:JAVA_HOME = "C:\Program Files\Java"
.\mvnw.cmd spring-boot:run
```

Buka browser di:
👉 **`http://localhost:5000`**
