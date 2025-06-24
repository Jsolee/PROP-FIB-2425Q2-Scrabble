
<div  align="center">

  

# 🎯 Scrabble Game - Edició Moderna

  

<img  src="https://img.shields.io/badge/Java-11+-blue?style=for-the-badge&logo=java"  alt="Java">

<img  src="https://img.shields.io/badge/Gradle-8.0+-green?style=for-the-badge&logo=gradle"  alt="Gradle">

<img  src="https://img.shields.io/badge/Swing-GUI-orange?style=for-the-badge&logo=java"  alt="Swing">

<img  src="https://img.shields.io/badge/Status-Finalitzat-success?style=for-the-badge"  alt="Status">

  

*Una implementació moderna i elegant del clàssic joc de taula Scrabble*

  

---

  

🎲 **Juga** • 🏆 **Competeix** • 📊 **Millora**

  

---

  

</div>

  

## ✨ Característiques Principals

<div align="center">

| 🎮 **Modes de Joc** | 🎨 **Interfície Moderna** |
|:---------------------|:---------------------------|
| 👥 **1 vs 1**: Juga contra un amic | 🎯 **Disseny Card-Based**: Interface neta i intuïtiva |
| 🤖 **1 vs Bot**: Desafia la CPU | 🌈 **Paleta Scrabble**: Colors temàtics autèntics |
| 💾 **Carregar Partida**: Continua on ho vas deixar | 📱 **Disseny Responsiu**: S'adapta a diferents mides |
| 🌍 **Multiidioma**: Català, Castellà i Anglès | ⚡ **Animacions Fluides**: Experiència visual excel·lent |

</div>

  

---

  

## 👥 Equip de Desenvolupament

<div align="center">

| 🧑‍💻 **Desenvolupador** | 🎯 **Rol Principal** |
|:----------------------:|:-------------------:|
| **Víctor Abelló** | Algoritmes & Bot |
| **Daniel Cebrián** | Backend & Domini & Persistència |
| **Pau Dehesa** | Documentació & Dades |
| **Joan Solé** | Frontend & UI/UX & Domini |

*🏫 Cluster 41 - FIB (UPC) • 📚 Projecte de Programació 2024/2025*

</div>

  

---

  

## 🏗️ Arquitectura del Projecte

  

```

📦 Scrabble Game

├── 🎯 Domini/ # Lògica de negoci

│ ├── ControladorDomini.java # Controlador principal

│ ├── Partida.java # Gestió de partides

│ ├── Taulell.java # Taulell de joc

│ ├── Bot.java # Intel·ligència artificial

│ └── Diccionari.java # Validació de paraules

├── 💾 Persistència/ # Gestió de dades

│ ├── ControladorPersistencia.java

│ ├── Usuaris.json # Base de dades d'usuaris

│ └── Partides.json # Partides guardades

├── 🎨 Presentació/ # Interfície d'usuari

│ ├── ControladorPresentacio.java

│ ├── ModernUI.java # Components UI moderns

│ ├── GamePanel.java # Panell de joc

│ ├── LoginPanel.java # Autenticació

│ └── ProfilePanel.java # Perfil d'usuari

└── 📚 Resources/ # Diccionaris multiidioma

├── catalan/

├── castellano/

└── english/

```

  

---

  

## 🚀 Instal·lació i Execució

  

### 📋 Prerequisits

  

- ☕ **Java 11** o superior

- 🔧 **Gradle** (inclòs amb el projecte)

  

### ⚡ Execució Ràpida

  

```bash

# Clona el repositori

git  clone  https://repo.fib.upc.es/grau-prop/subgrup-prop41.4.git

cd  subgrup-prop41.4

  

# Executa l'aplicació

./gradlew  run

```

  

### 🛠️ Comandes de Desenvolupament

<div align="center">

| 🔧 **Comanda** | 📝 **Descripció** |
|:-------------:|:------------------:|
| `./gradlew build` | 🔨 Compila el projecte complet |
| `./gradlew test` | 🧪 Executa tots els tests unitaris |
| `./gradlew run` | ▶️ Executa l'aplicació directament |
| `./gradlew jar` | 📦 Genera l'arxiu JAR executable |
| `./gradlew assembleDist` | 🚀 Crea distribució completa |
| `./gradlew clean` | 🧹 Neteja tots els artefactes |

</div>

  

---

  

## 🎮 Com Jugar

  

<div  align="center">

  

### 1️⃣ **Registra't o Inicia Sessió**

Crea un compte nou o accedeix amb les teves credencials

  

### 2️⃣ **Escull el Mode de Joc**

Selecciona entre 1vs1, contra Bot o carrega una partida

  

### 3️⃣ **Forma Paraules**

Arrossega les fitxes al taulell per crear paraules vàlides

  

### 4️⃣ **Guanya Punts**

Acumula punts segons les lletres i caselles especials

  

### 5️⃣ **Millora el teu Rànking**

Competeix per arribar al top dels millors jugadors

  

</div>

  

---

  

## 🏆 Funcionalitats Avançades

  

### 🤖 **Algoritme DAWG**

- Algoritme DAWG per a validació ràpida de paraules

- Bot intel·ligent

- Estratègies optimitzades per maximitzar puntuació

  

### 📊 **Sistema de Rànking**

- Estadístiques detallades per jugador

- Classificació global i per nivells

- Historial de partides i rècords personals

  

### 💾 **Persistència Avançada**

- Guardat automàtic de partides

- Perfils d'usuari persistents

- Sistema de còpies de seguretat

  

### 🌐 **Suport Multiidioma**

- Diccionaris complets en 3 idiomes

- Validació de paraules específica per idioma

- Interfície traduïda completament

  

---

  

## 🔧 Tecnologies Utilitzades

<div align="center">

| 🛠️ **Categoria** | 💻 **Tecnologies** |
|:----------------:|:------------------:|
| **Llenguatge** | ![Java](https://img.shields.io/badge/Java-11+-ED8B00?style=flat-square&logo=java&logoColor=white) |
| **Build Tool** | ![Gradle](https://img.shields.io/badge/Gradle-8.0+-02303A?style=flat-square&logo=gradle&logoColor=white) |
| **GUI Framework** | ![Swing](https://img.shields.io/badge/Java_Swing-GUI-orange?style=flat-square&logo=java&logoColor=white) |
| **Persistència** | ![JSON](https://img.shields.io/badge/JSON-Data-000000?style=flat-square&logo=json&logoColor=white) |
| **Testing** | ![JUnit](https://img.shields.io/badge/JUnit-Testing-25A162?style=flat-square&logo=junit5&logoColor=white) |

</div>

  

---

  

## 📈 Estadístiques del Projecte

<div align="center">

| 📊 **Mètrica** | 📈 **Valor** |
|:--------------:|:------------:|
| **Línies de Codi** | ~8,000+ |
| **Classes** | 25+ |
| **Tests Unitaris** | 50+ |
| **Idiomes Suportats** | 3 |
| **Paraules al Diccionari** | 100,000+ |

</div>

  

---

  

## 🤝 Contribució

  

Aquest projecte ha estat desenvolupat com a part de l'assignatura **PROP** a la **FIB (UPC)**.

  

### 📋 Estàndards de Codi

- ✅ Documentació completa en JavaDoc

- ✅ Tests unitaris per totes les funcionalitats crítiques

- ✅ Arquitectura MVC ben definida

- ✅ Patrons de disseny aplicats

  

---

  

## 📄 Llicència

  

Aquest projecte està desenvolupat amb fins acadèmics per a l'assignatura PROP de la FIB (UPC).

  

---

  

<div  align="center">

  

### 🎯 **Fet amb ❤️ pel Cluster 41**

  

*Projecte de Programació - FIB (UPC) - 2024/2025*

  

---

  

🔗 **Links Útils:**

[Documentació Gradle](https://docs.gradle.org/current/userguide/application_plugin.html) •

[Java 11 Docs](https://docs.oracle.com/en/java/javase/11/) •

[Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)

  

---

  

⭐ *Si t'agrada el projecte, dona-li una estrella!* ⭐

  

</div>
