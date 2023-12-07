# Project-ipminor-groep1-2-4
Project-ipminor-groep1-2-4 created by GitHub Classroom

Welkom op onze spring boot applicatie! 
Op deze webapplicatie kun je regattas, boten, stallingen en teams registreren!
Een overzicht van boten, regattas, teams of stallingen kan getoond worden.
Op deze paginas/links vind je ook sorteer opties en/of zoekfuncties.
Boten, teams, stallingen en regattas kunnen toegevoegd, aangepast en verwijderd worden.
Boten en teams kunnen respectievelijk aan stallingen en regattas toegevoegd en verwijderd worden.

-------------------------------------------------------------------------------------------------------
Hieronder vindt u de takenverdeling van dit project. Deze is mogelijks niet meer up-to-date.

# TO DO opdracht 3 26/05/2023, tag v3.0 voor finale commit
Indienen Toledo: 
* link github repo team
* link commit met tag v3.0

## EINDOPDRACHT/DEEL 3 *DONE*

###laatste stapjes
* exception catching mag niet in controllers : *Rainier* *DONE*
* exception catching check in overige klasses: service, domain *Rainier* *DONE*
* exceptions service -> domain: *Amine* *DONE*
* check postman tests *Rainier DONE*
* check AllTests *DONE*
* check classes *DONE*
* random "walkthrough app" testing : *Rainier *DONE* en Amine DONE*
* BUG: storage.not.empty error bij update storage van een storage die 0 boten heeft *Amine* *FIXED*
* BUG: sort by organiser regatta werkt niet *Rainier* *FIXED*
* BUG: http://localhost:8080/regatta/update/1 en analoge links zichtbaar voor iedereen *Rainier* *FIXED*
* BUG: Exceptions errors o.w.v. geen "Action" parameter (REST) *Rainier* *FIXED*
* BUG: DomainExceptions in terminal ipv applicatie *Rainier* *FIXED*
* BUG: Error in terminal delete-confirmation page *Rainier* *FIXED*
* BUG: Paginatie error na search met null values *Rainier* *FIXED*
* BUG: Errorpagina throwing bij empty searchresult *Rainier* *FIXED*
* BUG: foutieve links storage *Amine* *FIXED*
* BUG: foutieve links regatta *Amine* *FIXED*

### Consistente code QA
Basis: Amine: *DONE*
* update/delete van Boot/Team zijn enkel mogelijk als ze niet gelinkt zijn aan Stalling/Regatta 
* update/delete van Stalling/Regatta zijn enkel mogelijk als ze geen Boot/Team bevatten 

~~Gevorderd: --> na Boat integratie~~ 
* ~~delete van Boot => lijst in Stalling wordt aangepast~~
* ~~delete van Stalling => alle Boten worden vrijgegeven~~ 
* ~~delete van Team of Regatta => lijsten van betrokken Regatta’s of Teams worden aangepast~~ 
* ~~update Boot: controleren of Boot nog in de Stalling past (voldoende plaats, hoogte, …)~~ 
* ~~update Stalling: controleren of alle Boten nog passen (voldoende plaats, hoogte, …)~~ 
* ~~update Team: controleren of Team nog kan deelnemen aan al zijn Regatta’s~~ 
* ~~update Regatta: controleren of alle ingeschreven Teams nog kunnen deelnemen~~
  ~~Bepaal zelf hoe je het aanpakt, maar zorg ervoor dat je applicatie consistent blijft en informeer de gebruiker wat (niet) kan.~~
  ~~Zorg voor aangepaste foutboodschappen als de gebruiker iets doet wat niet kan.~~

# ***URLs : Rainier en Amine DONE --> CHECK BIJ INDIENEN DONE***

-------------STORAGE-BOAT----------------------
* POST http://localhost:8080/api/storage-boat/add/boat/{boatId}/to/storage/{storageId} 
  * Response: JSON Boat 
* GET http://localhost:8080/api/storage-boat/boats?storageId=8 
  * Response: JSON lijst van boten die staan in de storage met gegeven storageId 
* POST http://localhost:8080/api/storage-boat/remove/boat/{boatId}/from/storage/{storageId} 
  * Response: JSON Boat (die net van de stalling verwijderd is) 

--------------REGATTA-TEAM---------------------
* POST http://localhost:8080/api/regatta-team/add/team/{teamId}/to/regatta/{regattaId} 
  * Response: JSON Team 
* POST http://localhost:8080/api/regatta-team/remove/team/{teamId}/from/regatta/{regattaId} 
  * Response: JSON Team (dat net uitgeschreven is uit de regatta) 
* GET http://localhost:8080/api/regatta-team/teams?regattaId=7 
  * Response: JSON lijst van teams die ingeschreven zijn in de regatta met gegeven regattaId

Boat - velden: id, name, email, length, width, height, insurance & naam van de stalling (default: “unknown”) 

Team - velden: id, name, category, passengers, club & lijst van namen van regattas’s waar ze ingeschreven zijn (default: [ ]) 

//in orde normaal
Als het request geen succes heeft (bijv niet-valide invoer, onbestaand id, …), geeft de response body een aangepaste en leesbare foutboodschap, indien mogelijk met het type van fout en response status 400 (bad request)

### Regatta - Team : ***DONE***
Leg de relatie tussen regatta en team

* Een regatta moet een bestaand team kunnen toevoegen : *DONE*
  * Een team kan zich inschrijven voor verschillende wedstrijden, maar slechts één per dag *Rainier* *DONE*
  * Inschrijven is maar mogelijk als het max aantal teams niet bereikt is *Amine* *DONE*
  * Inschrijven kan alleen als het team tot de juiste categorie behoort *Amine* *DONE*
* Maak een REST api om (url’s zie slide 34)
  * een team toe te voegen aan een regatta : Amine *DONE*
  * alle teams van een regatta op te vragen : *Rainier* *DONE*
  * een team te verwijderen van een regatta : Amine *DONE*
    * implementatie: maak het mogelijk dat een regatta een team verwijdert
    * Het team zelf mag niet verwijderd worden
* De REST api van team toont ook de namen van de regatta’s waartoe hij/zij behoort : Rainier en Amine *DONE*

### Storage - Boat : ***DONE***
Leg de relatie tussen stalling en boot : *Rainier* *DONE*

* Een stalling moet een bestaande boot (die nog niet ergens anders gestald is) kunnen toevoegen *Rainier* *DONE*
  * De boot mag niet te hoog zijn *Rainier* *DONE*
  * Er moet plaats zijn in de stalling. Bereken de plaats die de boot nodig heeft als lengte*breedte. Een stalling is vol als 80% van de beschikbare ruimte ingenomen is (laatste 20% is verlies omdat er tussen boten wat ruimte gelaten wordt en omdat we geen rekening houden met lengte en breedte van de stalling en/of boot). *Rainier* *DONE*
    * Een boot kan maar in één stalling gestald zijn *Rainier en Amine* *DONE*
  * Een eigenaar mag maar één boot in dezelfde stalling plaatsen *Rainier* *DONE*
* Maak REST api zodat (url’s cfr slide 34)
  * je een bestaande boot kunt toevoegen aan een bestaande stalling *Rainier* *DONE*
  * je alle boten van een stalling kan opvragen *Rainier* *DONE*
  * je een boot kan verwijderen van een stalling *Rainier* *DONE*
    * Implementatie: maak het mogelijk dat een stalling een boot verwijdert
    * De boot zelf mag niet verwijderd worden
    * Als een stalling een boot verwijdert, kan een (andere) stalling die boot gaan kiezen
* De REST api van boten toont ook de naam van de stalling waartoe hij behoort *Rainier* *DONE*

### Requests in Postman voor testing purposes:
##### Regatta-Team: Rainier en Amine *DONE*
##### Storage-Boat: Rainier *DONE*

### DEEL 2 *DONE*

##### Boat tests: DONE

* Testklasses : DONE
    * Boat: Amine DONE
    * BoatRepository: Amine DONE
    * BoatService:  DONE
    * BoatRestController: Rainier DONE
* niveaus:
    * happy scenario 1 a 3
    * mogelijke unhappy scenarios --> 2 a 3

##### Foutafhandeling JSON Rainier en Amine DONE
* bij ingeven van data voor toevoegen en updaten 
* bij niet-bestaand id bij update en delete

##### Team tests: DONE

  * Testklasses DONE
    * Team: *Amine* DONE
    * TeamRepository: Amine DONE
    * TeamService: Amine DONE
    * TeamRestController: Amine en Rainier DONE
  * niveaus:
    * happy scenario 1 a 3
    * mogelijke unhappy scenarios --> 2 a 3


##### ERROR HANDLING: DONE

  * Zorg ervoor dat je niet meer de error white label ziet, maar deftige eigen gemaakte error pages 
    * Maak onderscheid tussen verschillende errors door pagina met duidelijke foutboodschap ipv standaard pagina zoals in demo code 
    * 404 not found ==> error page in stijl app met boodschap deze pagina bestaat niet 
    * 403 forbidden ==> error page in stijl app met boodschap u heeft niet voldoende rechten

##### Foutafhandeling JSON Rainier en Amine DONE
* bij ingeven van data voor toevoegen en updaten
* bij niet-bestaand id bij update en delete

##### URLs Boat: Rainier DONE

* http://localhost:8080/api/boat/overview
    * GET
    * response body: json lijst van Boat met velden: id, name, email, length, width, height, insurance
* http://localhost:8080/api/boat/add
    * POST
    * request body : json Boat met velden name, email, length, width, height, insurance
    * response body: json object met het toegevoegde boat (velden: id, name, email, length, width, height, insurance) (indien succes)
* http://localhost:8080/api/boat/update?id=7
    * PUT
    * request en response body als bij add
* http://localhost:8080/api/boat/delete?id=1
    * DELETE
    * response body: json object met de verwijderde boat (velden: id, name, email, length, width, height, insurance) (indien succes)
* http://localhost:8080/api/boat/search?insurance=abc
    * GET
    * response body: json object met velden: id, name, email, length, width, height, insurance
* http://localhost:8080/api/boat/search/{height}/{width} zoek boten met gegeven width en height (exact match)
    * GET
    * response body:  json lijst van boten met velden: id, name, email, length, width, height, insurance

##### URLs Team: Rainier DONE

* http://localhost:8080/api/team/overview DONE
    * GET
    * response body: json lijst van Teams met velden: id, name, category, passengers, club
* http://localhost:8080/api/team/add DONE
    * POST
    * request body : json Team met velden name, category, passengers, club
    * response body: json object met het toegevoegde team (velden: id, name, category, passengers, club) (indien succes)
* http://localhost:8080/api/team/update/{id} DONE
    * PUT
    * request en response body als bij add
* http://localhost:8080/api/team/delete/{id} DONE
    * DELETE-response body: json object met het verwijderde team (velden: id, name, category, passengers, club) (indien succes)
* http://localhost:8080/api/team/search?category=ab452po DONE
    * GET
    * response body: json lijst van Teams met velden: id, name, category, passengers, club
* http://localhost:8080/api/team/search/{number} DONE
    * GET
    * response body: json lijst van Teams met velden: id, name, category, passengers, club

##### Pagination overzichtspagina (ook na sorteren/zoeken): Rainier *DONE*
* https://www.baeldung.com/spring-data-jpa-pagination-sorting
* Zorg ervoor dat de gegenereerde HTML correct en valide is (nav>ul>li)
* overview boot, team, regatta, storage

##### Authentication & Authorization: (REST controllers zeker niet afschermen)  *DONE*
* Regels: *Amine*
    * Niet-ingelogde gebruiker kan enkel homepagina zien:
    * Gewone user kan overzichten zien, kan alle zoekfuncties gebruiken:
    * Admin user kan wat gewone user kan, maar ook create, update en delete --> rollen:
* Gebruik:
    * een echte login/logout ipv pop-up zoals in demo code *Amine*
    * zorg ook voor afscherming van de navigatie en knoppen die niet beschikbaar mogen zijn (bv. update) *Rainier*
    * Maak standaard 2 gebruikers aan: USER (name = user, password = t) en ADMIN (name = admin, password = t) *Amine*

##### Boat domain klasse: *Amine* **DONE**

* fields: naam, email (van de eigenaar), lengte (m), breedte (m), hoogte (m), en verzekeringsnummer
* alle velden verplicht 
* validatie:
    * naam heeft minimale lengte 5
    * verzekeringsnummer is sequentie van 10 karakters a-Z-0-9 en is uniek
    * de combinatie naam-email is uniek

##### Team domain klasse: *Amine* **DONE**

  * fields: naam, categorie, aantal inzittenden, club
  * alle velden verplicht behalve club
  * validatie:
    * naam heeft minimale lengte 5
    * categorie is sequentie van 7 karakters a-Z-0-9
    * het aantal inzittenden is een getal tussen 1 en 12
    * de combinatie naam team en categorie is uniek

##### Service en Repository klasses Boat: *Amine* **DONE**

##### Service en Repository klasses Team: *Rainier* **DONE**

##### Database tabel Boat maken: *Amine* **DONE**

##### Database tabel Teams maken: *Amine* **DONE**

##### HTML pagina boats maken: **DONE**
* boat add: *Rainier* **DONE**
* boat overview: *Amine* **DONE**
* boat delete: *Amine* **DONE**
* boat update: *Rainier* **DONE**

##### HTML pagina teams maken: **DONE**
  * team add: *Amine* **DONE**
  * team overview: *Amine* **DONE**
  * team delete: *Rainier* **DONE**
  * team update: *Rainier* **DONE**

##### RESTController api **Boat**: moet mogelijk zijn om **--DONE--**
* een boat toe te voegen: *Amine* DONE
* alle boats op te vragen: *Amine* DONE
* een boat te updaten: *Amine* DONE
* een boat te deleten: *Amine* DONE
* alle boats met een insurance number te zoeken: *Rainier* DONE
* alle boats kunnen zoeken op hoogte-breedte: *Rainier* DONE

##### RESTController api **Teams**: moet mogelijk zijn om **--DONE--**
* een team toe te voegen: *Amine*
* alle teams op te vragen: *Amine*
* een team te updaten: *Rainier*
* een team te deleten: *Rainier*
* alle teams van een categorie te zoeken (niet case sensitive): *Rainier*
* alle teams kunnen tonen met minder deelnemers dan aangegeven, gesorteerd op aantal deelnemers: *Rainier*

##### Foutafhandeling JSON **DONE**
* bij ingeven van data voor toevoegen en updaten: *Rainier*
* bij niet-bestaand id bij update en delete: *Rainier*

##### RESTController api **Boat**: TESTEN POSTMAN : *Rainier* *DONE*

##### RESTController api **Teams**: TESTEN POSTMAN : Rainier DONE
