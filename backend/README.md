# Backend Java (Spring Boot 3.4 + Java 21) - JanjiTemu

Rewrite lengkap dari backend Node.js/Express ke Java Spring Boot dengan arsitektur enterprise-grade yang rapi dan modular.

## 🛠 Tech Stack
- **Java**: OpenJDK 21 LTS
- **Framework**: Spring Boot 3.4.2
- **Database**: MongoDB (Spring Data MongoDB)
- **Security**: Spring Security + JWT (`jjwt` 0.12.6) + BCrypt
- **Build Tool**: Maven Wrapper (`mvnw`)

## 📂 Struktur Paket (`com.janjitemu.app`)
- **`model/`**:
  - `User.java` (Entity akun & role: admin/customer)
  - `ServiceItem.java` (Entity layanan)
  - `Appointment.java` (Entity booking janji temu)
- **`repository/`**:
  - `UserRepository.java`
  - `ServiceRepository.java`
  - `AppointmentRepository.java`
- **`dto/`**:
  - `LoginRequest.java`, `RegisterRequest.java`, `AuthResponse.java`, `MessageResponse.java`, `AppointmentCreateRequest.java`, `AppointmentUpdateRequest.java`, `ServiceRequest.java`
- **`security/`**:
  - `JwtUtils.java` (Pembuatan & validasi JWT claim token)
  - `JwtAuthenticationFilter.java` (Filter stateless authentication)
  - `SecurityConfig.java` (CORS & konfigurasi role security)
  - `UserPrincipal.java` (User details terautentikasi)
- **`controller/`**:
  - `AuthController.java` (`POST /register`, `POST /login`)
  - `ServiceController.java` (`GET /services`, `POST /services`, `PUT /services/{id}`, `DELETE /services/{id}`)
  - `AppointmentController.java` (`GET /appointments`, `POST /appointments`, `PUT /appointments/{id}`)
- **`service/`**:
  - `DataSeeder.java` (Otomatis membuat user `admin@janjitemu.com` dan 4 layanan bawaan saat pertama kali jalan)

## 🚀 Cara Menjalankan

### Cara 1: Menggunakan file batch
Klik dua kali file `run.bat` di dalam folder `backend`.

### Cara 2: Menggunakan Terminal / PowerShell
```powershell
cd backend
$env:JAVA_HOME = "C:\Program Files\Java"
.\mvnw.cmd spring-boot:run
```

Aplikasi akan berjalan di `http://localhost:5000` (port yang sama persis dengan backend sebelumnya).
