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
- [ ] Create note  
- [ ] Read (get) note  
- [ ] Update note  
- [ ] Delete note  
- [ ] Expiry timestamp logic  
  - [ ] Expired notes are **excluded** from query results  
  - [ ] Expiry check logic (e.g., `createdAt + expiresIn < now()`)

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
- [ ] Add tests for:
  - [ ] Auth flow (register + login)  
  - [ ] CRUD functionality  
  - [ ] Expired note filtering  
- [ ] Test edge cases:  
  - [ ] Wrong password  
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
- [ ] Enable Ktlint  
- [ ] Centralized error handling (e.g., `@ControllerAdvice`)  
- [ ] DTOs renamed to end with `Dto`  
- [ ] Validate note length and required fields  
- [ ] Move command line test runner to test config if needed