# ⚖️ JanjiTemu - Sistem Reservasi Konsultasi Hukum (Full Java)

Aplikasi web sistem informasi dan manajemen reservasi janji temu konsultasi hukum yang dibangun **100% menggunakan Java murni** berbasis **Spring Boot 3.4** dan **Thymeleaf**. Aplikasi ini tidak memerlukan Node.js ataupun runtime eksternal lainnya.

---

## 🛠️ Teknologi & Komponen yang Digunakan

| Komponen | Teknologi | Versi | Penjelasan & Alasan Penggunaan |
| :--- | :--- | :--- | :--- |
| **Bahasa Pemrograman** | Java (OpenJDK) | **Java 21 LTS** | Bahasa utama bertipe statis (*strongly typed*), performa tinggi, dan standar industri enterprise. |
| **Framework Web** | Spring Boot | **3.4.2** | Framework Java modern untuk *rapid application development*, dependency injection, dan web routing. |
| **Template Engine** | Thymeleaf | **3.1** | Mesin *Server-Side Rendering* (SSR) yang terintegrasi erat dengan Spring MVC untuk menampilkan HTML secara dinamis. |
| **Database** | MongoDB | **Local / Atlas** | Database NoSQL dokumen berbasis JSON/BSON, fleksibel untuk data akun, reservasi, dan jenis layanan. |
| **Data Access** | Spring Data MongoDB | **4.4.2** | Abstraksi repository (*MongoRepository*) untuk query, indexing, dan manipulasi data tanpa boilerplate code. |
| **Keamanan & Autentikasi** | Spring Security | **6.4** | Mengamankan rute halaman (RBAC), hashing password dengan **BCrypt**, serta manajemen sesi login. |
| **API Token (Opsional)** | JJWT (io.jsonwebtoken) | **0.12.6** | Pustaka pembuatan dan validasi JSON Web Token (JWT) untuk endpoint REST API. |
| **Antarmuka (UI/CSS)** | Tailwind CSS | **v3 (CDN)** | Framework CSS utility-first untuk desain modern, responsif, kartu statistik, dan badge status. |
| **Tipografi** | Google Fonts | **Plus Jakarta Sans** | Font modern dan profesional untuk keterbacaan yang optimal. |
| **Build Tool** | Maven Wrapper | **Maven 3.9+** | Script pembungkus Maven (`mvnw` / `mvnw.cmd`) sehingga pengguna tidak perlu menginstal Maven secara manual. |

---

## 📂 Struktur Direktori Proyek

```text
janjiTemu-Java/
├── pom.xml                                   # File konfigurasi dependensi & build Maven
├── mvnw & mvnw.cmd                           # Maven Wrapper (Windows & Linux/macOS)
├── run.bat                                   # Script 1-klik untuk menjalankan di Windows
├── README.md                                 # Dokumentasi & panduan instalasi
└── src/
    ├── main/
    │   ├── resources/
    │   │   ├── application.properties        # Konfigurasi Port 5000 & koneksi database MongoDB
    │   │   └── templates/                    # File tampilan HTML (Thymeleaf)
    │   │       ├── login.html                # Tampilan Masuk (Email/Username)
    │   │       ├── register.html             # Tampilan Pendaftaran Klien Baru
    │   │       ├── admin/
    │   │       │   └── dashboard.html        # Panel Admin (Sesi Hari Ini, Semua Janji, Walk-in, CRUD Layanan)
    │   │       └── customer/
    │   │           └── dashboard.html        # Portal Klien (Reservasi Jadwal & Riwayat)
    │   └── java/com/janjitemu/app/
    │       ├── JanjitemuApplication.java     # Entrypoint utama Spring Boot
    │       ├── controller/                   # Controller Web & REST API
    │       ├── dto/                          # Data Transfer Object (Request & Response)
    │       ├── model/                        # Entity Dokumen MongoDB (User, ServiceItem, Appointment)
    │       ├── repository/                   # Interface query Spring Data MongoDB
    │       ├── security/                     # Konfigurasi keamanan, Session & JWT
    │       └── service/                      # DataSeeder (Pembuat akun admin & layanan otomatis)
    └── test/                                 # Unit & Integration Tests
```

---

## 💻 Panduan Menjalankan dari Laptop Lain (Setelah Git Clone)

Ikuti langkah-langkah berikut ketika Anda baru pertama kali mengunduh atau melakukan *clone* proyek ini ke laptop lain.

### 1. Persiapan Prasyarat di Laptop Baru

Pastikan perangkat baru telah terpasang:
1. **JDK 21 (Java Development Kit 21)**
   - Unduh dari: [Eclipse Temurin (Adoptium)](https://adoptium.net/) atau Amazon Corretto 21.
   - Saat instalasi di Windows, pastikan mencentang opsi: *"Set JAVA_HOME variable"* dan *"Add to PATH"*.
   - Cek di terminal: `java -version` (harus menampilkan versi 21).
2. **Git**
   - Unduh dari: [git-scm.com](https://git-scm.com/).
3. **Database MongoDB** (Pilih salah satu opsi):
   - **Opsi A (Rekomendasi - MongoDB Lokal)**:
     - Unduh dan pasang [MongoDB Community Server](https://www.mongodb.com/try/download/community).
     - Jalankan MongoDB Service (berjalan di port default `localhost:27017`).
   - **Opsi B (MongoDB Atlas di Cloud)**:
     - Jika menggunakan database cloud, siapkan connection string URI MongoDB Atlas Anda.

---

### 2. Langkah Clone Repositori

Buka Terminal / Git Bash / PowerShell di laptop baru:

```bash
git clone <URL_REPOSITORI_ANDA>
cd janjiTemu-Java
```

---

### 3. Konfigurasi Database (Jika Diperlukan)

Secara default, aplikasi langsung terhubung ke MongoDB lokal:
```properties
# file: src/main/resources/application.properties
spring.data.mongodb.uri=${MONGO_URI:mongodb://localhost:27017/janjitemu}
```

- **Jika menggunakan MongoDB Lokal**: Anda **tidak perlu mengubah apa pun**. Aplikasi akan otomatis membuat database `janjitemu` saat pertama kali dijalankan.
- **Jika menggunakan MongoDB Atlas**: Anda dapat mengatur *Environment Variable* `MONGO_URI` sebelum menjalankan server, atau langsung mengganti baris di `src/main/resources/application.properties`:
  ```properties
  spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/janjitemu?retryWrites=true&w=majority
  ```

---

### 4. Menjalankan Aplikasi

#### 🟢 Cara 1: Menggunakan Script Batch (Pengguna Windows)
Cukup klik dua kali file **`run.bat`** di dalam folder proyek.

#### 🔵 Cara 2: Melalui Terminal / PowerShell (Windows)
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"  # Sesuaikan dengan lokasi folder JDK 21 Anda
.\mvnw.cmd spring-boot:run
```
*(Catatan: Jika `JAVA_HOME` sudah terpasang permanen di Environment Variables sistem Anda, cukup jalankan `.\mvnw.cmd spring-boot:run`)*.

#### 🍏 Cara 3: Melalui Terminal (macOS / Linux)
Beri izin eksekusi wrapper lalu jalankan:
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

---

### 5. Mengakses Aplikasi

Setelah terminal menampilkan log:
```text
Tomcat started on port 5000 (http) with context path '/'
Started JanjitemuApplication in ... seconds
✅ Akun Admin Berhasil Dibuat: admin@janjitemu.com / admin123
✅ Layanan Default Berhasil Dibuat (4 Layanan)
```

Buka browser dan kunjungi:
👉 **[http://localhost:5000](http://localhost:5000)**

---

## 🔑 Kredensial Login Bawaan

Saat pertama kali terhubung ke database, sistem secara otomatis menginisialisasi akun awal:

### Akun Administrator:
- **Email / Username**: `admin@janjitemu.com`
- **Password**: `admin123`
- **Akses**: Kelola seluruh janji temu, konfirmasi/tolak jadwal, buat janji tamu walk-in, dan tambah/edit layanan hukum.

### Akun Customer:
- Klien baru dapat langsung mendaftar sendiri melalui tautan **[Daftar Sekarang](http://localhost:5000/register)** di halaman login.
- Atau login menggunakan akun customer yang didaftarkan.

---

## ❓ FAQ & Troubleshooting di Laptop Baru

1. **Error: `JAVA_HOME is not set and no 'java' command could be found in your PATH`**
   - **Solusi**: Atur path JDK 21 di terminal sebelum menjalankan Maven wrapper:
     ```powershell
     $env:JAVA_HOME = "C:\Program Files\Java\<nama-folder-jdk21>"
     ```
2. **Error: `Timed out while waiting for a server ... connection refused`**
   - **Solusi**: Pastikan MongoDB service lokal sudah berjalan (`services.msc` -> cari **MongoDB Server** -> klik Start), atau periksa apakah koneksi internet/URI MongoDB Atlas Anda valid.
3. **Apakah perlu install Node.js / npm?**
   - **Tidak perlu sama sekali**. Seluruh tampilan HTML, styling CSS, dan fungsi aplikasi dirender oleh Spring Boot di server Java.
