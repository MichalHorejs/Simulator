# Simulator
Tréninkový simulátor pro dispečery s prvky gamifikace a využitím velkých předtrénovaných jazykových modelů

---
### Návod na spuštění
Z důvodu využití datasetu, k němuž jsem podepsal NDA, jsem aplikaci nikde nenasazoval.
Je potřeba mít nainstalované `npm`, testováno bylo na verzi 10.8.3, Java 21 a PostgreSQL ve verzi 14. 
Předpokládám že projekt bude fungovat i na novějších verzích.

#### Databáze
Bohužel je potřeba v Postgres předem vytvořit databázi pojmenovaou `simulator` 
a spustit na defaultním portu. 
Tabulky se vytvoří při inicializaci projektu i s daty.
Alternativou je upravit názvy v konfiguraci projektu `src/main/resources/application.properties`.

#### Npm
Před spustěním je potřeba zavolat příkaz `npm --prefix src/main/client install` 
a následně  `npm --prefix src/main/client run  build`. React je potřeba mít ve verzi 18 nebo vyšší.

#### Java
Nakonec je potřeba zavolat `./gradlew bootRun` a ve webovém prohlížeči přistoupit 
na adresu <a href="http://localhost:8080/">http://localhost:8080/</a>. Databáze se automaticky naplní daty. 
Alternativou je `IntelliJ IDEA`, kde je potřeba projekt naimportovat a spustit.

#### Uživatelé
| **Jméno** | **Heslo** |
|-----------|-----------|
| user      | pass      |
| admin     | pass      |

#### Testy
Testy jse spouštějí příkazem `./gradlew test` a je potřeba běžící databáze a zkompilovaný React.

---
### Kontakt
V případě potřeby mě kontaktujte na školním mailu, 
`michalhorejs16@gmail.com` nebo telefonicky `+420 773 699 811`.