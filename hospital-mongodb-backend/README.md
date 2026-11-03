# Hospital Management System – MongoDB Backend

## Overview
Backend API for managing patient records, doctors, and hospital appointments. Built with Node.js, Express, MongoDB and Mongoose.

## Requirements
Node.js 18+, MongoDB 6+ or Docker Desktop.

## Setup
1. Copy `.env.example` to `.env`.
2. Run `npm install`.
3. Start MongoDB locally.
4. Run `npm run dev`.
5. Open `http://localhost:5000/health`.

Docker: `docker compose up --build`. The compose configuration starts MongoDB in replica-set mode; initialize the local demo replica set from mongosh with `rs.initiate({_id:"rs0",members:[{_id:0,host:"mongo:27017"}]})`, then restart the API if required. A single-node replica set is for demonstration only; production high availability requires multiple members on separate hosts.

## API
Base path: `/api`
- Patients: POST/GET `/patients`, GET/PATCH/DELETE `/patients/:id`
- Doctors: POST/GET `/doctors`, GET/PATCH/DELETE `/doctors/:id`
- Appointments: POST/GET `/appointments`, GET/PATCH/DELETE `/appointments/:id`
- Report: GET `/reports/appointments-by-status`
- Doctor search: GET `/doctors?search=cardiology&page=1&limit=10`
- Appointment filter: GET `/appointments?status=booked&page=1&limit=10`

Example patient JSON:
```json
{"name":"Riya Mehta","email":"riya@example.com","phone":"9876543210","dateOfBirth":"2002-04-12","gender":"female","bloodGroup":"B+"}
```

## Indexes and performance
Unique indexes: patient email and doctor email. Compound indexes support department/specialization and appointment lookups by doctor/date, patient/date, and status/date. Pagination limits returned records. Inspect plans in mongosh:
```javascript
db.appointments.find({status:"booked"}).explain("executionStats")
```
Indexes improve reads but add storage and write overhead; measure before adding more.

## Backup and restore
Install MongoDB Database Tools. Linux/macOS:
```sh
export MONGODB_URI=mongodb://localhost:27017/hospital_management
sh scripts/backup.sh
sh scripts/restore.sh backups/<timestamp>
```
PowerShell: `$env:MONGODB_URI="mongodb://localhost:27017/hospital_management"`. Store backups securely off-machine and test restores regularly.

## Tests
Run `npm test`. Tests use mongodb-memory-server and Supertest.

## Important
Educational demo only. Do not use real patient information. A production healthcare application additionally needs authentication, role-based access, audit trails, encryption, secure backups, validation, rate limiting and applicable privacy compliance.
