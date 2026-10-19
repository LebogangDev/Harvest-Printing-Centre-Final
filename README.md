# Harvest Printing Centre

Digital platform for Harvest Printing Centre: a customer website, an Android mobile app and a REST API, backed by Firebase.
**Module:** XISD6329 | **Task 2:** Working Prototype

| Link | URL |
|---|---|
| Website | https://harvest-printing-centre.vercel.app |
| API base URL | https://harvest-printing-api.onrender.com |
| Services (public) | https://harvest-printing-api.onrender.com/services |
| GitHub repository | https://github.com/LebogangDev/Harvest-Printing-Centre-Final |
| Merged pull request | https://github.com/LebogangDev/Harvest-Printing-Centre-Final/pull/1 |
| GitHub Actions | https://github.com/LebogangDev/Harvest-Printing-Centre-Final/actions |

> **Note:** the API runs on a free Render server that sleeps when idle. The first request can take 30 to 60 seconds. Open the API link a minute before using the app or recording the demo.

---

## Purpose of the App and Website

Harvest Printing Centre relied on walk-in traffic, phone calls and social media posts. Customers could not see completed work, book outside store hours, or check current services and pricing without contacting the shop.

This platform solves that with connected parts:

| Part | What it does | Who uses it |
|---|---|---|
| **Website** | Home, Services, Portfolio, Book Online and Contact pages. Takes booking requests 24/7. | New and walk-in customers |
| **Android app** | Browse services, place orders, make bookings, settings and help screens. | Repeat customers |
| **REST API** | Serves services and stores orders and bookings for logged-in users. | Website and app |

**Main features**

- Public services listing
- Online booking and order placement for logged-in users
- Sign in with Firebase Authentication (email and Google)
- Edit and cancel your own bookings
- Orders with automatic total and a quote flag for items that need a quote
- Settings and Help screens with input validation in the app

**Screenshots**

| Website home | Book Online |

| ![Website home](docs/screenshots/<img width="720" height="1600" alt="web-home png" src="https://github.com/user-attachments/assets/ba2d5e3b-dd92-4dd9-aefa-51f438cb0cd3" />) 

| ![Book Online](docs/screenshots/<img width="720" height="1600" alt="web-book png" src="https://github.com/user-attachments/assets/b0274ea5-0164-47e8-83c3-ff2dc8a4b476" />
)|

| Mobile app home | Services screen |

| ![App home](docs/screenshots/<img width="720" height="1600" alt="app-home png" src="https://github.com/user-attachments/assets/62b66a3a-7781-4793-abd6-0700e019b541" />) 

| ![Services](docs/screenshots/<img width="720" height="1600" alt="app-services png" src="https://github.com/user-attachments/assets/c5a3fc7e-cb44-4831-bdc8-dc56bf126f30" />



## Design Considerations

- **Layered design.** Website and app (client) talk only to the REST API. The API is the only thing that touches the database, so clients never hold database access.
- **Secure by default.** Reading services is public. Orders and bookings need a logged-in user: the client sends a Firebase ID token and the API verifies it. Requests without a valid token get **401**.
- **Users only see their own data.** Orders and bookings are stored with the user's ID.
- **Validation.** The API rejects bad data with a clear 400 message instead of saving it: empty name, phone with letters, wrong date format (use YYYY-MM-DD), invalid email, quantity of 0, negative or text, an empty items list, and services that do not exist. The app also validates input before sending.
- **Free hosting.** Render (API) and Vercel (website) free tiers keep running cost at zero.
- **One identity.** The Android app is registered in the same Firebase project (`harvest-printing-centre`) as the API so tokens are accepted.

**Tech stack**

| Area | Technology |
|---|---|
| Website | HTML, CSS, JavaScript, hosted on Vercel |
| Android app | Kotlin, Retrofit `[CONFIRM: Jetpack Compose / XML]` |
| API | Node.js and Express, hosted on Render |
| Token checking | firebase-admin (verifies the Firebase ID token on every protected request) |
| Authentication | Firebase Authentication (ID tokens) |
| Database | Cloud Firestore |
| CI | GitHub Actions |

**REST API**

| Endpoint | Method | Access | Purpose |
|---|---|---|---|
| `/services` | GET | Public | List services |
| `/services/{id}` | GET | Public | One service |
| `/orders` | POST, GET | Logged in | Create and list your orders |
| `/bookings` | POST, GET | Logged in | Create and list your bookings |
| `/bookings/{id}` | PUT, DELETE | Logged in | Update or cancel a booking |

Send this header on protected calls:

```
Authorization: Bearer <Firebase ID token>
```

Example order body:

```json
{ "items": [ { "serviceId": "business-cards", "quantity": 2 } ] }
```

Example booking body (phone = digits only, date = YYYY-MM-DD):

```json
{
  "name": "Thabo",
  "phone": "0821234567",
  "email": "t@test.com",
  "service": "Business Cards",
  "quantity": 100,
  "date": "2026-12-01",
  "message": ""
}
```

**Database (Cloud Firestore)**

| Collection | Key fields |
|---|---|
| services | `[CONFIRM fields]` |
| bookings | name, phone, email, service, quantity, date, message, userId |
| orders | userId, items, total, requiresQuote, status, createdAt |
| users / customers |  linked by Firebase UID |

Firestore was approved by the lecturer. The ERD and data dictionary are in the documentation report `[ADD link / path]`.

## How GitHub and GitHub Actions Were Used

**GitHub**

- One shared repository holds all code and documents, so everyone works from a single source of truth. No zip files are submitted; all work goes through commits.
- The repository was created with a README and an Android-based `.gitignore`, which gave it its first commit. Extra lines (`.env`, `node_modules/`, `google-services.json`) keep API keys and Firebase configuration out of the repository.
- Each member works on their own branch (for example `feature/lebogang-setup`) and merges into `main` through a pull request, so teammates can review each other's code and nobody overwrites another person's work.
- Pull request #1 is merged into `main`: https://github.com/LebogangDev/Harvest-Printing-Centre-Final/pull/1 
- Board screenshots are kept in `docs/` as evidence of the Kanban board.

Folder structure (lowercase, no spaces):

| Folder | Contents | Owner |
|---|---|---|
| `app/` | Android Kotlin project | Mpho Nethomboni |
| `api/` | Node.js and Express backend API | Mulamuleli Given Mutshotsho |
| `database/` | Database files and seed data (at least 10 rows per collection) | Mulamuleli Given Mutshotsho |
| `website/` | Website files | Mulamuleli Given Mutshotsho |
| `docs/` | Screenshots, test report, user guide and ERD | Whole team |

**Azure DevOps (Kanban board)**

Azure DevOps is used to manage the application life cycle. The project `HarvestPrintingCentre` (private, Git, Agile process) has one user story for every deliverable in the work division, 30 in total, each with an owner. Cards move from **New** to **Active** to **Closed** as work is started and checked. Screenshots of the board are in `docs/`.

**GitHub Actions**

The workflow in `.github/workflows/` runs on every push and pull request. 

1. Check out the code.
2. Install dependencies.
3. Run the automated tests. The build fails if any test fails.

The tests are currently **passing** (green run): https://github.com/LebogangDev/Harvest-Printing-Centre-Final/actions

## Setup Instructions

**Use the live API (app developers)**

1. Set the Retrofit base URL to `https://harvest-printing-api.onrender.com/`.
2. Add the Android app to the existing Firebase project `harvest-printing-centre` so tokens are accepted by the API.
3. After login, get the token with `currentUser.getIdToken(false)` and send it as `Authorization: Bearer <token>` on protected calls.
4. If the first request is slow, wait up to 60 seconds. The free server is waking up.

**Run the API locally**

1. Clone the repository:
   ```bash
   git clone https://github.com/LebogangDev/Harvest-Printing-Centre-Final.git
   cd Harvest-Printing-Centre-Final
   ```
2. Install dependencies: `[COMMAND, e.g. npm install]`
3. Add the Firebase service account credentials as environment variables or a local file. **Never commit this file. 
4. Start the server: `[COMMAND, e.g. npm start]`
5. Open `http://localhost:[PORT]/services`.

**Run the tests:** `[COMMAND, e.g. npm test]`

**Test with Postman**

1. Create a collection and set the base URL to `https://harvest-printing-api.onrender.com`.
2. Wake the server first by opening `/services` in a browser (it can take 30 to 60 seconds).
3. For protected calls, get a Firebase ID token and set it once as the **Bearer Token** of the collection so every request inherits it. Tokens expire after about an hour, so refresh when you get 401.
4. Send the requests below and compare with the expected status.

### API Testing Results

Munei tested the live API in Postman on 18 October 2026. **All 42 test figures passed**, and every status code required by the project plan is shown. The full evidence is in `Harvest_Printing_API_Testing_Report.pdf`.

| What was tested | Endpoint | Expected | Result |
|---|---|---|---|
| Services list (14 services) | GET /services | 200 | PASS |
| One service | GET /services/business-cards | 200 | PASS |
| Unknown service | GET /services/does-not-exist | 404 | PASS |
| Unknown route | GET /random-route | 404 | PASS |
| No token on orders and bookings (GET and POST) | /orders, /bookings | 401 | PASS |
| List own orders with token | GET /orders | 200 | PASS |
| Create order | POST /orders | 201 | PASS |
| Create, list, update and delete a booking | /bookings, /bookings/{id} | 201, 200, 200, 200 | PASS |
| Update a deleted or fake booking | PUT /bookings/{id} | 404 | PASS |
| Order validation (quantity -1 or 0, empty items, empty body, unknown service) | POST /orders | 400 | PASS |
| Booking validation (empty body, empty name, phone with letters, bad date, quantity 0, -5 or text, bad email) | POST /bookings | 400 | PASS |

| Code | Meaning | Shown in |
|---|---|---|
| 200 OK | Request succeeded | Figures 2 to 9, 16, 24 to 26 |
| 201 Created | New record saved | Figures 17, 23, 41 |
| 400 Bad Request | Invalid data rejected | Figures 18 to 22, 28 to 40 |
| 401 Unauthorized | No login token | Figures 11 to 14 |
| 404 Not Found | Unknown service, route or booking | Figures 10, 15, 27, 42 |

## Release Notes

### v1.0 `[DATE]`

- REST API live on Render with public and protected endpoints
- Firebase Authentication token checks on orders and bookings
- Orders collection with total and quote flag
- Website live on Vercel
- Android app screens: Services, Orders, Bookings, Settings, Help `[CONFIRM]`
- API tested in Postman: 42 tests, all passing (200, 201, 400, 401, 404 covered)
- GitHub Actions tests passing; code merged via pull request #1

#Demo Video

Watch the demo on YouTube (unlisted)
https://youtu.be/U77z-zHZVnk 

The video shows register, login with email and Google, every screen, settings, and ordering through the API. It also shows the data behind the app: the Firebase Authentication user list, the API responding in Postman or the browser, and the Firestore collections with the new rows.

Team Members and Roles
Name	Student number	Role	Contribution
Lebogang Ntlatleng	ST10456295	Project Manager	GitHub repository and README shell, Azure DevOps Kanban board, meeting log, release notes, final rubric check
Mpho Nethomboni	ST10448422	Lead Software Developer	Android app: navigation, register and login, Google sign-in, API connection, Services and Orders screens, logging, real-phone testing
Mulamuleli Given Mutshotsho	ST10311127	Software Designer	Database build, API, website fixes and Google sign-in, Settings, validation and Help screens, unit tests and GitHub Actions
Munei Nevhutanda	ST10333563	Tester / QA Analyst	Test plan and cases, Postman API testing (42 tests, all pass), phone testing, bug log and test report, user guide, Help text
Portia Mashaba	ST10357369	Secretary / Documentation	Database table design, ERD and data dictionary, README, demo video, final report compilation
