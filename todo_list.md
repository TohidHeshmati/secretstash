# ✅ Note Stash - Secure Note-Taking API - TODO List

## ✅ Initial Setup
- [x] Project skeleton with Gradle + Kotlin + Spring Boot
- [x] Basic app startup verified

---

## 🔐 Authentication & Authorization
- [ ] JWT-based login & registration  
  - [ ] `/auth/register` endpoint  
  - [ ] `/auth/login` endpoint  
- [ ] Password hashing (e.g., BCrypt)
- [ ] Restrict notes access to authenticated users

---

## 📝 CRUD for Notes
- [x] Create note  
- [x] Read (get) note  
- [x] Update note  
- [x] Delete note  
- [x] Expiry timestamp logic  
  - [x] Expired notes are **excluded** from query results  
  - [x] Expiry check logic (e.g., `createdAt + expiresIn < now()`)

---

## 🛡️ Security & Performance
- [ ] Add rate limiter (e.g., to prevent brute-force login attempts)
- [ ] `GET /notes/recent`:  
  - [ ] Returns latest 1000 notes  
  - [ ] Sorted by creation date (descending)  
  - [ ] Only user’s notes  
  - [ ] Excludes expired notes  

---

## 🧪 Testing
- [x] Add tests for:
  - [x] Auth flow (register + login)  
  - [x] CRUD functionality  
  - [x] Expired note filtering  
- [x] Test edge cases:  
  - [x] Wrong password  
  - [ ] Expired token  
  - [ ] Missing fields  

---

## 🛠️ Infrastructure & Local Dev
- [ ] Dockerize app  
- [ ] Add `application-local.yml`  
- [ ] Create `curl-commands.md` with test requests  
- [ ] Add README with:  
  - [ ] How to run the app  
  - [ ] Example requests  
  - [ ] Architectural notes

---

## 🧹 Cleanup and Enhancements
- [x] Enable Ktlint  
- [ ] Centralized error handling (e.g., `@ControllerAdvice`)  
- [ ] DTOs renamed to end with `Dto`  
- [ ] Validate note length and required fields  
- [ ] Move command line test runner to test config if needed
