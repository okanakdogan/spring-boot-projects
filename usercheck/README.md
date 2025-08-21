# ⚡ Username Availability Check System

This project is inspired by [ByteMonk’s video](https://www.youtube.com/watch?v=_l5Q5kKHtR8), **“How Big Tech Checks Your Username in Milliseconds ⚡.”** It replicates how large-scale platforms like Instagram or Twitter check if a username is available—**instantly and at scale**, even with billions of users.

---

## 💡 Why This Matters

When users sign up, a message like *“Username is already taken”* might seem trivial. But for companies handling **billions of users**, this check needs to be blazing fast and efficient.

A simple database query for every request would:

* Slow down response times
* Overload the system
* Create performance bottlenecks

So how do tech giants handle this?

---

## 🛠 What This Project Does

This project implements a **multi-layered username availability checker**, designed to be scalable and ultra-fast. It follows a **three-tier architecture**:

1. ✅ **Bloom Filter** — Fast, memory-efficient first check
2. ⚡ **Redis Cache** — Quick, in-memory lookup
3. 🗄️ **Database** — Final, authoritative source

---

## 🧠 How It Works

### 1. 🌸 Bloom Filter – The First Line of Defense

* **What it is:** A smart, memory-efficient data structure that quickly says if something **might exist** or **definitely doesn’t**.
* **How it helps:**

  * If the Bloom Filter says a username **definitely doesn’t exist** → ✅ Return "Available" immediately.
  * If it **might exist** → 🤔 Move to the next layer (possible false positive).
* **Why use it:** It’s lightning fast and saves tons of unnecessary work.

> 🔍 Fun Fact: You can store 1 billion usernames with a 1% false-positive rate using just \~1.2 GB of memory!

---

### 2. 🧠 Redis Cache – The Fast Lookup

* **What it is:** A super-fast, in-memory key-value store (hashmap).
* **How it helps:**

  * If the username is found in Redis → ❌ "Username is taken"
  * If not found → 🔍 Check the database
* **Why use it:** Redis responds in microseconds, perfect for frequently checked usernames.

---

### 3. 🗃️ Database – The Source of Truth

* **What it is:** A persistent storage system (you can use PostgreSQL, MySQL, Cassandra etc.).
* **How it helps:**

  * If both the Bloom Filter and Redis are unsure → the DB confirms if the username really exists.
* **Why use it:** It’s slowest but most accurate. Used only when needed.

---

## 🔁 System Flow – Step by Step

Here’s what happens when a user checks a username:

1. 👤 User enters a username.
2. ⚙️ Request goes to the server.
3. 🧪 **Bloom Filter** checks if the username might exist.

   * ❌ Definitely not? → Return **Available**
   * 🤔 Might exist? → Proceed
4. 🧠 **Redis Cache** is checked.

   * ✅ Found? → Return **Taken**
   * ❌ Not found? → Proceed
5. 🗃️ **Database** is queried.

   * ✅ Found? → Return **Taken**
   * ❌ Not found? → Return **Available**
6. 🔁 (Optional) Cache the result in Redis and update Bloom Filter.

This strategy ensures:

* 🚀 Speed
* ⚖️ Scalability
* 💰 Cost-efficiency

---

## 🧰 Technologies Used

| Tech            | Purpose                           |
| --------------- | --------------------------------- |
| **Java**        | Core language                     |
| **Spring Boot** | Backend framework                 |
| **Redis**       | Fast in-memory cache layer        |
| **Postgres**    | SQL Database                      |

---

## 🚀 Getting Started

1. Start with Docker Compose:

2. Hit the `localhost:8080/api/auth/check-username?username=<your_username>` endpoint

---

## 📝 Notes

* This project simulates the logic; in real-world setups, the database would be distributed (like Cassandra or DynamoDB), and Bloom filters would likely be distributed or managed via services like RedisBloom.
* Redis acts as a simple hashmap in this implementation.
* Bloom Filter logic is in-memory (you can persist or sync this in production scenarios).

---

## 📺 Reference

Original inspiration:
[ByteMonk – How Big Tech Checks Your Username in Milliseconds ⚡](https://www.youtube.com/watch?v=_l5Q5kKHtR8)
