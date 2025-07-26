# EC7201 - Information Security

# Project Analysis Report

# SECURE VOTING PROTOCOL FOR CLASS ELECTIONS USING HYBRID CRYPTOGRAPHY IN JAVA

---

## 1. PROJECT OVERVIEW

### 1.1 Background & Motivation

Electronic voting is increasingly explored as an efficient, secure, and transparent alternative to traditional paper-based elections. However, these systems must overcome significant security concerns including vote confidentiality, voter anonymity, authentication, and integrity.

This project presents a Secure Voting Protocol for class level elections. It is developed using Java and leverages strong cryptographic mechanisms. Our motivation was to implement a secure, anonymous, and tamper resistant voting process suitable for academic institutions.

#### 1.1.1 Project Goals

* Ensure vote **confidentiality, authenticity, integrity**, and **non-repudiation** using modern cryptographic methods.
* Implement a secure **client-server** architecture where votes are cast from clients and recorded by a trusted server.
* Prevent **duplicate voting** and maintain **voter anonymity**.
* Demonstrate the practicality of hybrid cryptography (AES + RSA) in secure systems.

#### 1.1.2 Tools and Technologies

* **Programming Language**: Java
* **Libraries**: `javax.crypto`, `java.security`, `java.net`, `java.io`
* **GitHub Repository**: [Secure Voting Protocol GitHub](https://github.com/InformationSecurity11/Secure-Voting-Protocol-for-Class-Elections)

---

## 2. SYSTEM DESIGN

### 2.1 System Architecture

* **Client-Server Model**: Clients (voters) generate, encrypt, sign, and send votes to a centralized server.
* **Secure Communication**: Votes are encrypted using AES, and the AES key is encrypted using the server's RSA public key.
* **Digital Signature**: Each vote is signed by the voter to ensure its authenticity.

### 2.2 Major Components

#### 2.2.1 Client (voter.java)

* Inputs Voter ID and candidate choice.
* Generates RSA key pair.
* Encrypts the vote using AES.
* Encrypts AES key using server's RSA public key.
* Signs the encrypted vote with voter's private key.
* Sends all data to server (voter ID, public key, encrypted vote, encrypted AES key, signature).

#### 2.2.2 Server (Server.java)

* Validates voter ID and checks for duplicates.
* Verifies vote signature.
* Decrypts AES key and vote.
* Records anonymous vote and voter ID.

#### 2.2.3 Crypto Utilities (cryptoUtils.java)

* RSA Key Generation
* AES Key Generation
* AES and RSA encryption/decryption
* Digital Signature generation and verification

#### 2.2.4 Vote Object (vote.java)

* Serializable class containing candidate selection.

### 2.3 Message Flow

```
Client:
- vote object -> AES encrypt -> sign
- AES key -> RSA encrypt
- Send: Voter ID, Voter Public Key, Encrypted Vote, Encrypted AES Key, Signature

Server:
- Verify Signature
- Decrypt AES key using private RSA key
- Decrypt vote using AES key
- Record vote anonymously
```

---

## 3. SECURITY ANALYSIS

### 3.1 Implemented Security Features

* **Confidentiality**: Votes are AES encrypted; AES key is protected with RSA.
* **Integrity**: Digital signatures ensure vote data is not tampered.
* **Authentication**: Signature verification confirms the voter's identity.
* **Non-repudiation**: Voters cannot deny their submitted vote.
* **Anonymity**: Voter IDs are not linked to vote contents.
* **Duplicate Prevention**: Voter ID is logged to block repeated voting.

### 3.2 Trust Assumptions

* The server behaves honestly.
* Voter devices are uncompromised.
* RSA and AES implementations are secure.

---

## 4. TEAM MEMBERS

* DE Silva P.K.H.D (EG/2020/3884)
* Ranaweera K.D (EG/2020/4144)
* Balasooriya B.A.L.M (EG/2020/3838)
* Perera H.L.D.U.G (EG/2020/4112)

---
