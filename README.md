# Harvest Printing Centre

Digital platform for Harvest Printing Centre: a customer website, an Android mobile app and a REST API, backed by Firebase.
**Module:** XISD6329 | **Task 2:** Working Prototype

A website, an Android app and a hosted REST API that let Harvest Printing Centre show its services, take bookings and accept orders 24/7. Module: XISD6329/w | Task 3: Final Project

Link	URL
Website	https://harvest-printing-centre.vercel.app
API base URL	https://harvest-printing-api.onrender.com
Services (public)	https://harvest-printing-api.onrender.com/services
Main repository	https://github.com/LebogangDev/Harvest-Printing-Centre-Final
GitHub Actions	https://github.com/LebogangDev/Harvest-Printing-Centre-Final/actions
API repository (Given)	https://github.com/GivenMu/Harvest-Printing-Centre
API token test page	https://harvest-printing-centre.vercel.app/token.html
Demo video (unlisted)	https://youtu.be/qKt-ICMOcjE

Note: the API runs on a free Render server that sleeps when idle. The first request can take 30 to 60 seconds. Open the API link, or the token test page, a minute before using the app or recording a demo.

Purpose of the App and Website

Harvest Printing Centre relied on walk-in traffic, phone calls and informal social media posts. Customers could not see completed work, book outside store hours, or check current services and pricing without contacting the shop.

This platform solves that with three connected parts:

Part	What it does	Who uses it
Website (Vercel)	Home, Services, Portfolio, Book Online and Contact pages. 24/7 booking, Google sign-in, and an admin dashboard behind Firebase Authentication.	New and walk-in customers, shop staff
Android app (Kotlin)	Register and log in with email or Google, browse services, place orders and bookings, Settings and Help screens.	Repeat customers
REST API (Render)	Serves services publicly and stores orders and bookings for logged-in users in Firestore.	Website and app

Client needs this solves

Be findable online and look professional on any device.
Show current services and pricing from one source of truth.
Accept booking and order requests outside store hours.
Give returning customers an account so they do not re-enter their details.
Keep each customer's orders and bookings private to them.

Main features

Public services listing with photos and prices
Register and log in with email or Google (SSO)
Place and track orders, with the price calculated server-side so it cannot be faked
Create, update and delete your own bookings
Settings and Help screens, with friendly error messages instead of crashes
Server-validated South African phone numbers and email addresses, with typo suggestions (for example gmail.con)
Colour-coded order status (Pending, Quoted, In production, Ready, Completed, Cancelled)

**Screenshots**

| Website home | Book Online |

| ![Website home](docs/screenshots/<img width="720" height="1600" alt="web-home png" src="https://github.com/user-attachments/assets/ba2d5e3b-dd92-4dd9-aefa-51f438cb0cd3" />) 

| ![Book Online](docs/screenshots/<img width="720" height="1600" alt="web-book png" src="https://github.com/user-attachments/assets/b0274ea5-0164-47e8-83c3-ff2dc8a4b476" />
)|

| Mobile app home | Services screen |
| ![Google signin](docs/screenshots/<img width="720" height="1600" alt="app-google png" src="" />) <img width="576" height="1280" alt="google-signin png" src="https://github.com/user-attachments/assets/341deaec-8c9b-46bb-99b3-847e8927dbcf" />

| ![App home](docs/screenshots/<img width="720" height="1600" alt="app-home png" src="https://github.com/user-attachments/assets/62b66a3a-7781-4793-abd6-0700e019b541" />)

| ![Services](docs/screenshots/<img width="720" height="1600" alt="app-services png" src="https://github.com/user-attachments/assets/c5a3fc7e-cb44-4831-bdc8-dc56bf126f30" />
settings
<img width="720" height="1600" alt="setting png" src="https://github.com/user-attachments/assets/4520614e-50dd-4b51-8cd5-987ef019cff5" />

Design Considerations
Three layers. The website and app are clients. They never touch the database directly; one hosted REST API is the only door to the data, which sits in Firebase.
Backend. Node.js and Express on Render. Simple to build and test, and the free tier suits a prototype.
Database. Cloud Firestore, approved by the lecturer in place of SQL tables. Documents suit orders with embedded line items.
Authentication. Firebase Authentication (email and Google). Passwords are salted and hashed by Firebase, never handled by our code.
Authorisation. Every protected request carries a Firebase ID token as a Bearer token, checked by the API with firebase-admin. No valid token means 401, and users only ever see their own data.
Server-side pricing. The app sends only a service ID and quantity. The API looks up the real price, so a customer cannot send a fake total. Items without a fixed price are automatically flagged for a quote.
Validation. Checked in both the app and the API. South African phone numbers and email addresses are validated, with typo suggestions, and bad data returns 400 before it reaches the database.
App architecture. MVVM: Fragment, ViewModel (StateFlow), Repository, then the API or Firebase. Screens never call the network directly, which keeps them testable.
Hosting. Render (API) and Vercel (website). Free tiers keep running cost at zero. Vercel redeploys the website and Render redeploys the API automatically on every merge to main.

Tech stack

Area	Technology
Website	HTML, CSS, JavaScript, hosted on Vercel
Android app	Kotlin (minSdk 24, targetSdk 35), XML layouts, Material Components, Jetpack Navigation, Retrofit
API	Node.js and Express, hosted on Render
Token checking	firebase-admin
Authentication	Firebase Authentication (email and Google)
Database	Cloud Firestore
Source control	GitHub (branches and pull requests)
CI	GitHub Actions
Project management	Azure DevOps Kanban board

Database (Firestore collections, 10+ documents each)

Collection	Purpose
services	Printing services and prices (14 documents)
portfolio	Portfolio items shown on the website (14 documents)
bookings	Customer bookings, linked to the user by userId
contacts	Messages from the website contact form
orders	Orders: userId, items, total, requiresQuote, status, createdAt
users	Managed by Firebase Authentication: uid, email, displayName, provider

The full field list is in database/COLLECTIONS.md.

REST API

Base URL: https://harvest-printing-api.onrender.com

Endpoint	Method	Purpose	Access
/	GET	Health check (also wakes the server)	Public
/services	GET	List all services	Public
/services/{id}	GET	One service, or 404	Public
/orders	POST, GET	Create an order, or list your own	Logged in
/bookings	POST, GET	Create a booking, or list your own	Logged in
/bookings/{id}	PUT, DELETE	Update or delete your own booking	Logged in

Send this header on protected calls:

Authorization: Bearer <Firebase ID token>

Use the token test page (token.html) to sign in and copy a fresh token for Postman. Tokens expire after about an hour.

How GitHub and GitHub Actions Were Used

GitHub

All code and documents live in one repository: https://github.com/LebogangDev/Harvest-Printing-Centre-Final. No zip files are submitted; everything goes through commits.
Work is split into 30 user stories on an Azure DevOps Kanban board, built on separate branches, and merged through reviewed pull requests.
.gitignore keeps secrets out of the repository: .env, node_modules/, the service account key, and google-services.json.
Workflow: clone, create a feature branch (for example feature/given-api), commit small described changes, push, open a pull request into main, let GitHub Actions and the Vercel preview run as checks, then merge only when everything is green.
Folder	Contents	Owner
app/	Android Kotlin project	Mpho Nethomboni
api/	Node.js and Express API	Mulamuleli Given Mutshotsho
database/	Seed scripts and COLLECTIONS.md	Mulamuleli Given Mutshotsho
website/	Website files	Mulamuleli Given Mutshotsho
docs/	Screenshots, test report, user guide, ERD	Whole team

Key pull requests (API repository)

PR	Title	Result
#1	Add Express API with Firestore, auth middleware, tests and CI	Merged; 4 checks passed
#2	Add orders seed, data dictionary update, API README and gitignore	4 checks passed
#3	Add API test tool page (token.html)	4 checks passed; Vercel preview deployed
#7	Fix phone and email form validation (lecturer feedback)	Merged; strict South African phone and email checks, with typo suggestions

GitHub Actions (CI)

The workflow .github/workflows/ci.yml runs on every push and pull request to main:

Check out the code.
Set up Node.js 20 and cache dependencies.
Install API dependencies with npm ci.
Run the unit tests with npm test. The run fails if any test fails.

This proves the API installs and passes its tests on a clean machine, not just one laptop. Pull request #7 passed this workflow on its branch and again on main after merging.

Azure DevOps Kanban board

The project HarvestPrintingCentre is private, uses Git and the Agile process. Each card moves New → Active → Closed as work starts and is checked. Board screenshots are kept in docs/ as evidence.

Setup Instructions

Clone the repository

bash
git clone https://github.com/LebogangDev/Harvest-Printing-Centre-Final.git
cd Harvest-Printing-Centre-Final

Run the API

Open the api folder and run npm install.
Add the Firebase service account values to a local .env file. Never commit this file.
Run npm start, then open /services in a browser to confirm it works.

Run the tests

bash
cd api
npm test

16 Jest and Supertest tests run against a fake Firestore, so they need no keys or internet and give the same result on any machine.

Build the Android app

Open the app folder in Android Studio.
Set the project's Gradle JDK to 21.
Add google-services.json from the Firebase console.
Run the app on a phone with USB debugging enabled.

Test the live API in Postman

Open the token test page and sign in to get a Firebase ID token.
Set it once as the collection's Bearer Token so every request inherits it.
Wake the server first with a GET request to /, since the first call can take 30 to 60 seconds.
Refresh the token if a protected call returns 401 after about an hour.
Release Notes
v1.1 (final) — November 2026
Website booking and contact forms now check South African phone numbers and email addresses, suggest fixes for typos such as gmail.con, and show an error under each field (pull request #7)
Stricter email check added in the API and Firestore rules
Android build and JUnit test job added to GitHub Actions
Signed release build prepared for Google Play
Updates made in response to lecturer feedback on Tasks 1 and 2
v1.0 (prototype) — October 2026
REST API live on Render with public and protected endpoints
Firebase ID token checks on every protected request
Orders collection with server-calculated total and automatic quote flag
Website live on Vercel with Google sign-in
Android app with Services, Orders, Bookings, Settings and Help screens
42 Postman tests and 16 Jest tests passing
GitHub Actions pipeline passing
Innovative features
Feature	Why it is innovative
Server-side pricing with automatic quote flag	Prices cannot be faked; items without a fixed price are flagged for a quote
Token test page (token.html)	Lets testers sign in and copy a Firebase token for Postman in one click
Colour-coded order status chips	Customers see their order's status at a glance
Offline-friendly service photos	Photos are bundled in the app, so they load instantly without mobile data
Friendly error handling with Try again	No crashes when the server is asleep or the phone is offline
Live form checks with typo suggestions	Catches mistakes like gmail.con as the customer types
Demo Video
https://youtu.be/qKt-ICMOcjE?si=vEx-g_idREhvzBam
Watch the demo on YouTube (unlisted)

The video shows registering and logging in with email and Google, every app screen, the website, ordering through the API, and the data behind it: the Firebase Authentication user list, the API responding in Postman, and the Firestore collections with new documents.

Team Members and Roles
Name	Student number	Role	Contribution
Lebogang Ntlatleng	ST10456295	Project Manager	Repo and README shell, Azure DevOps Kanban board, meeting log, release notes, final rubric check, GitHub Actions screenshots
Mpho Nethomboni	ST10448422	Lead Software Developer	Android project and navigation, register and login, Google sign-in, Retrofit API connection, Services and Orders screens, logging and comments, real-phone testing
Mulamuleli Given Mutshotsho	ST10311127	Software Designer	Database tables and seed data, API endpoints, token middleware and hosting, website fixes and Google sign-in, Settings and Help screens, unit tests and GitHub Actions
Munei Nevhutanda	ST10333563	Tester / QA Analyst	Test plan and cases, Postman API testing (42 tests, all pass), phone testing, bug log and test report, user guide, in-app Help text
Portia Mashaba	ST10357369	Secretary / Documentation	Database table design, ERD and data dictionary, README, demo video script and edit, final report compilation


ictionary, README, demo video, final report compilation
