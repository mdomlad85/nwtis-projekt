**Projekt:**  **NWTiS\_2016\_2017 v1.1**

Postoji sustav za upravljanje IoT uređajima pod nazivom koji se nalazi u aplikaciji DZ3\_Master i temelji se na web servisu pod nazivom IoT\_Master koji je namijenjen za studente (zbog toga se sustav naziva IoT\_Master). Njegova je uloga da prima informacije od određenog skupa IoT uređaja, koji su pridruženi pojedinom korisniku (to se naziva grupa), te da ih prosljeđuje korisniku u obliku MQTT poruka na njegovu temu (topic). Korisnik se treba pretplatiti na svoju temu kako bi mogao preuzeti/primati poruke. Korisnik može upravljati svojom grupom kao i pojedinim IoT uređajima u grupi. Integracija sustava IoT\_Master s ostalim dijelovima sustava realizirana je pomoću operacija web servisa. Svaki poziv operacije web servisa mora sadržavati podatke o korisničkom imenu i korisničkoj lozinci aktivnog korisnika web servisa. Tu se radi o podacima koji se koriste prilikom autentikacije za NWTiS video materijale i NWTiS\_svn Subversion. Operacije IoT\_Master web servisa jedino može pozivati {korisnicko\_ime}\_aplikacija\_1a radi se o sljedećim aktivnostima:

- registrirati svoju grupu što dovodi do kreiranja i pokretanje dretve za slanje MQTT poruka o aktivnim IoT uređajima iz njegove grupe. Grupa je inicijalno blokirana tako da dretvačeka.
- djaviti (deregistrirati) svoju grupu što dovodi prekida i brisanja dretve za slanje MQTT
- učitati predefinirane IoT uređaje u svojugrupu
- dodati IoT uređaj u svojugrupu
- aktivirati, blokirati i brisati IoT uređaje iz svoje grupe (sve, više ili pojedinačnodabranih)
- aktivirati svoju grupu što dovodi do izvršavanjedretve
- blokirati svoju grupu što dovodi do postavlja dretve u stanječekanja.
- dobiti status svojegrupe
- dobiti IoT uređaje iz svojegrupe
- dobiti status odabranog IoT uređaja iz svojegrupe

Adresa WSDL: http://nwtis.foi.hr:8080/DZ3\_Master/IoT\_Master?wsdl Adresa MQTT poslužitelja: [http://nwtis.foi.hr/](http://nwtis.foi.hr/)

Port MQTT poslužitelja: 61613

Za autentikaciju se koriste podaci za NWTiS video materijale i NWTiS\_svn Subversion. Naziv teme: ″/NWTiS/{korisnickoIme}″

Sadržaj MQTT poruke je u formatuJSON:{″IoT″:        d{1-6}, ″vrijeme″:″dd.MM.yyyy

hh.mm.ss.zzz″, ″tekst″: ″...″, ″status″: dd}

Poslužitelje MQTT poruka je Apollo (https://activemq.apache.org/apollo/).

Adresa NetBeans primjera za preuzimanje MQTT poruka (iz Apollo): https://elf.foi.hr/mod/resource/view.php?id=51243

Više o MQTT porukama na adresama: https://activemq.apache.org/apollo/documentation/mqtt-manual.html [http://mqtt.org/](http://mqtt.org/)

## Sustav se treba sastojati od sljedećih aplikacija:

1. web aplikacija ({korisnicko\_ime}\_aplikacija\_1) bavi se komuniciranjem s IoT\_Masterom te ujedno pruža sučelje ostalim aplikacijama prema IoT\_Masteru. U pozadinskom modu (tj. servletu s automatskim startanjem ili putem slušača), pokreće dretvu (konfiguracijom se određuje pravilni vremenski interval (jedinica je sekunda) preuzimanja podataka, npr. 30 sec, 100 sec, 2 min, 10 min, 30 min, 60 min, ...) koja preuzima važeće meteorološke podatke od openweathermap.org web servisa (u prilogu se nalazi opis postupka) za izabrani skup IoT uređaja na bazi njihovih lokacijskih podataka. APPID (APIKEY) za openweathermap treba biti spremljen  u konfiguracijskoj datoteci. U tablici uredjaji nalaze se podaci o IoT uređajima (id, naziv, koordinate, status itd.) koje su dodali svi korisnici. Tablica je definirana u 2. zadaći. Preuzete meteorološke i pomoćne podatke potrebno je spremati u tablicu meteo. Tablica je definirana u 3. zadaći. Preuzimanje meteoroloških podataka treba optimirati tako da se za jednu lokaciju kojoj pripada više IoT uređaja preuzimaju podaci samo jednom a zapisuju se za svaki IoT uređaj s tom lokacijom.

Tablice u bazi podataka trebaju sadržavati podatke za minimalno 20 IoT uređaja i za svaki od njih minimalno 100 preuzetih meteoroloških podataka u vremenskom intervalu duljim od zadnjih 72 sata.

Upravljanje pozadinskom dretvom i izvršavanje predviđenih operacija provodi se putem primitivnog poslužitelja koji ima ulogu socket servera na određenom portu (konfiguracijom se određuje). Kada poslužitelj primi zahtjev od klijenta zapisuje podatke u tablicu dnevnika rada u bazi podataka. Za sve zahtjeve prvo treba obaviti autentikaciju korisnika prema bazi podataka te ako je u redu može se nastaviti s radom. Provođenje spomenutih operacija treba biti putem dretvi jer se ne smije utjecati na sposobnost poslužitelja da primi nove zahtjeve. Zahtjev se temelji na komandama (isključivo u jednom retku), koje mogu biti sljedećih vrsta:

### Server socket komande:

USER        korisnik;        PASSWD        lozinka;        {PAUSE;        |        START;        |        STOP;        | STATUS;}

### IoT\_Master komande:

USER        korisnik;        PASSWD        lozinka;        IoT\_Master        {START;        |        STOP;        | WORK; WAIT; LOAD; CLEAR; STATUS;LIST;}

### IoT komande:

USER        korisnik;        PASSWD        lozinka;        IoT        d{1-6}        {ADD        ″naziv″

″adresa″; WORK; WAIT; REMOVE; STATUS;}

Objašnjenje komandi:

1.
  - USER korisnik; PASSWD lozinka; – autentikacija korisnika. Vraća ERR 10; ako ne postoji korisnik ili ne odgovara lozinka. Odnosno, prelazi na obradu ostalog dijela
  - PAUSE; – privremeno prekida preuzimanje meteoroloških podataka od sljedećeg ciklusa (i dalje može preuzimati komande). Vraća OK 10; ako nije bio u pauzi, odnosno ERR 10; ako je bio u
  - START; – nastavlja s preuzimanjem meteoroloških podataka od sljedećeg ciklusa. Vraća OK 10; ako je bio u pauzi, odnosno ERR 11; ako nije bio u
  - STOP; – potpuno prekida preuzimanje meteoroloških podataka i preuzimanje komandi. I završava rad. Vraća OK 10; ako nije bio u postupku prekida, odnosno ERR 12; ako je bio u postupku
  - STATUS; – vraća trenutno stanje poslužitelja. Vraća OK dd; gdje dd znači: 13 – privremeno ne preuzima podatke, 14 – preuzima podatke, 15 – ne preuzima podatke i korisničke
  - IoT\_Master START; – registrira grupu. Vraća OK 10; ako nije bila registrirana, odnosno ERR 20; ako je bila
  - IoT\_Master STOP; – odjavljuje (deregistrira) grupu. Vraća OK 10; ako je bila registrirana, odnosno ERR 21; ako nije bila
  - IoT\_Master WORK; – aktivira grupu. Vraća OK 10; ako nije bila aktivna, odnosno ERR 22; ako je bila
  - IoT\_Master WAIT; – blokira grupu. Vraća OK 10; ako je bila aktivna, odnosno ERR 23; ako nije bila
  - IoT\_Master LOAD; – učitava predefinirane IoT uređaje u grupu. Vraća OK10;.
  - IoT\_Master CLEAR; – briše sve IoT uređaje iz grupu. Vraća OK10;.
  - IoT\_Master STATUS; – vraća status grupe. Vraća OK dd; gdje dd znači: 24 – blokirana, 25 –
  - IoT\_MasterLIST;–vraćaidsvihIoTuređajeizVraćaOK10;{IoTd{1-6}

″naziv″, {IoT d{1-6} ″naziv″,...,{IoT d{1-6} ″naziv″}}};

1.
  - IoT d{1-6} ADD ″naziv″ ″adresa″; – dodaje IoT uređaj u grupu. Vraća OK 10; ako ne postoji, odnosno ERR 30; ako
  - IoT d{1-6} WORK; – aktivira IoT uređaj. Vraća OK 10; ako nije bio aktivan, odnosno ERR 31; ako je bio
  - IoT d{1-6} WAIT; – blokira IoT uređaj. Vraća OK 10; ako je bio aktivan, odnosno ERR 32; ako nije bio
  - IoT d{1-6} REMOVE; – briše IoT uređaja iz grupe. Vraća OK 10; ako postoji, odnosno ERR 33; ako ne
  - IoT d{1-6} STATUS; – vraća status IoT uređaja. Vraća OK dd; gdje dd znači: 34 – blokiranan, 35 –

U slučaju uspješne komande vrste Server socket potrebno je poslati email poruku (adresa primatelja, adresa pošiljatelja i predmet poruke određuju se postavkama) u MIME tipu „text/plain&quot; s informacijama o zahtjevu (u prvi redak kopira se sadržaj komande, a zatim u sljedećem retku slijede podaci o vremenu primanja zahtjeva u obliku: dd.MM.yyyy hh.mm.ss.zzz).

U slučaju prijema komande vrste IoT\_Master ili IoT potrebno je provesti autentikaciju korisnika i ako je uspješna treba pozvati pripadajuću operaciju web servisa IoT\_Master.

Drugi zadatak web aplikacije je pružanje SOAP web servisa. Svaki zahtjev treba biti autenticiran prema korisničkim podacima u bazi podataka i upisan u tablicu dnevnika rada u bazi podataka. SOAP web servis svoj rad temelji na meteorološkim podacima koji su putem pozadinske dretve spremljeni u bazi podataka i na . Na raspolaganju stoje:

- zadnje preuzeti meteorološki podaci za izabrani IoTuređaj
- posljednjih n (n se unosi) meteoroloških podataka za izabrani IoTuređaj
- meteorološki podaci za izabrani IoT uređaj u nekom vremenskom intervalu (od datuma, do datuma)
- važeći meteorološki podaci za izabrani IoT uređaj (putem openweathermap.org web servisa)
- adresa izabranog IoT uređaja na bazi njegove geolokacije (reverse geocoding, address lookup).  Uzima  se  atribut  &quot;formatted\_address&quot;  iz  objekta  koji  je  dobiven  iz    atributa

„results&quot;.Npr.

[http://maps.google.com/maps/api/geocode/json?latlng=46.3076267,16.3382566](http://maps.google.com/maps/api/geocode/json?latlng=46.3076267%2C16.3382566)        gdjesu upisani lokacijski podaci zaFOI.

Potrebno je pripremiti u NetBeans-ima za testiranje vlastitog web servisa (u Web services kreirati novu grupu NWTiS i dodati vlastiti servis).

Treći zadatak web aplikacije je pružanje REST web servisa u JSON formatu. Svaki zahtjev za REST web servisom treba biti upisan u tablicu dnevnika rada u bazi podataka. Prvi REST web servis odnosi se na rad s korisnicima u tablici korisnika u bazi podataka. Na raspolaganju stoje:

- dodavanje jednog korisnika (vraća 0 ako već postoji, 1 ako ne postoji te jedodan)
- ažuriranje jednog korisnika (vraća 0 ako ne postoji, 1 ako postoji te jeažuriran)
- preuzimanje jednog korisnika (vraća JSON korisnika, uključujući korisničkulozinku)
- preuzimanje svih korisnika (vraća niz JSON korisnika, bez korisničkelozinke).

Svi korisnici preuzimaju se putem osnovne adrese. Za izabranog korisnika šalje se njegovo korisničko ime tako da je to parametar {korisnickoIme} u URL strukturi poziva REST web servisa.

Drugi REST web servis odnosi se na rad s IoT uređajima u tablici uređaja u bazi podataka. Na raspolaganju stoje:

- dodavanje jednog IoT uređaja (vraća 0 ako već postoji, 1 ako ne postoji te jedodan)
- ažuriranje jednog IoT uređaja (vraća 0 ako ne postoji, 1 ako postoji te jeažuriran)
- preuzimanje jednog IoT uređaja (vraća JSON IoTuređaja)
- preuzimanje svih IoT uređaja (vraća niz JSON IoTuređaja).

Svi IoT uređaji preuzimaju se putem osnovne adrese. Za izabranog IoT uređaja šalje se njegov id tako da je to parametar {id}u URL strukturi poziva REST web servisa.

Četvrti zadatak je vidljivi dio web aplikacije koji treba biti zaštićen putem kontejnera na bazi vlastitog obrasca/forme za prijavljivanje uz pomoć JDBC pristupa do baze podataka te  osiguranjem sigurnog kanala (SSL uz vlastiti certifikat s imenom i prezimenom studenta). Korisnik možeobaviti:

- pregled korisnika uz straničenje (konfiguracijom se određuje broj linija postranici)
- pregled dnevnika rada web servisa uz straničenje (konfiguracijom se određuje broj linija po stranici)
- pregled zahtjeva za socket server uz straničenje (konfiguracijom se određuje broj linija po stranici).

1. enterprise aplikacija ({korisnicko\_ime}\_aplikacija\_2) koja ima EJB i Web module. Prvi dio je EJB modul koji sadrži Singleton Session Bean (SB) i Stateful Sesssion Bean. Kreiranje Singleton SB pokreće dretvu (konfiguracijom se određuje pravilni vremenski interval rada  (jedinica je sekunda), npr. 5 sec, 20 sec, 100 sec, ...) koja provjerava u poštanskom sandučiću (adresa poslužitelja, korisničko ime i lozinka definiraju se u konfiguracijskoj datoteci) pristiglu poštu. Brisanje Singleton SB prekida dretvu i zaustavlja

Od pristiglih nepročitanih email poruka one koje imaju predmet poruke prema postavkama iz konfiguracijske datoteke i MIME tip „text/plain&quot; nazivamo NWTiS porukama. Obrađene NWTiS poruke treba označiti da su pročitane i prebaciti u mapu/direktorij prema postavkama za NWTiS poruke. Ostale ne-NWTiS poruke treba ostaviti da su nepročitane. Na kraju svake iteracije obrade email poruka treba poslati JMS poruku (naziv reda čekanja NWTiS\_{korisnicko\_ime}\_1) s podacima o rednom broju JMS poruke koja se šalje, vremenu početka i završetka rada iteracije dretve, broju pročitanih poruka, broju NWTiS poruka. Poruka treba biti u obliku ObjectMessage, pri čemu je naziv vlastite klase proizvoljan, a njena struktura treba sadržavati potrebne podatke  koji su prethodno spomenuti. Red čekanja treba ima vlastiti brojač JMSporuka.

Autenticiranje korisnika obavlja se u Stateful SB, a nakon uspješnog autenticiranja registrira se za prijem MQTT poruka od IoT uređaja korisnikove grupe putem IoT\_Master-a. Primljeni podaci iz poruka upisuju se u tablicu poruka u bazi podataka. Nakon prijema određenog broja MQTT  poruka, tzv. slot, (konfiguracijom se određuje broj MQTT poruka u slotu) treba poslati JMS  poruku (naziv reda čekanja NWTiS\_{korisnicko\_ime}\_2) s podacima o rednom broju JMS poruke koja se šalje, vremenu početka i završetka prikupljanja tog slota, broju obrađenih poruka, kolekciji u koju se sprema atribut ″tekst″ primljenih MQTT poruke u slotu. Poruka treba biti u obliku ObjectMessage, pri čemu je naziv vlastite klase proizvoljan, a njena struktura treba sadržavati potrebne podatke koji su prethodno spomenuti. Red čekanja treba ima vlastiti brojač JMSporuka.

Drugi dio je web modul koji treba realizirati putem JSF (facelets) ili PrimeFaces uz minimalno dvojezičnu varijantu (hrvatski i engleski jezik). To znači da svi statički tekstovi  u pogledima trebaju biti označeni kao „labele&quot; i dobiti jezične prijevode. Jezik se odabire na početnoj stranici aplikacije i svi pogledi moraju imati direktnu poveznicu na tu adresu.

Potrebno je voditi evidenciju zahtjeva pomoću aplikacijskog filtera s upisom u tablicu dnevnika rada u bazi podataka. Osim osnovnih podataka o zahtjevu (url, IP adresa, korisničko ime, vrijeme prijema) potrebno je spremiti trajanje obrade pojedinog zahtjeva.

Korisniku na početku stoje na raspolaganju registracija ili prijavljivanje. Ova aplikacija nema tablicu korisnika u bazi podataka. Korisnik kod registracije upisuje korisničko ime (mora biti jedninstveno), prezime, lozinku i ponovljenu lozinku za provjeru, email adresu. Nakon uspješne provjere   (validacije)   podataka   za   registraciju   korisnika,   podaci   se   šalju   REST     servisu

{korisnicko\_ime}\_aplikacija\_1. Ako je dodavanje u redu javlja se poruka o tome. Odnosno poruka ako nije u redu. Nakon uspješnog prijavljivanja (šalje se zahtjev REST servisu

{korisnicko\_ime}\_aplikacija\_1 za podatke o korisniku te se provjeravaju) korisnik može obavljati aktivnosti kroz određene poglede:

1.
  - pogled1:
    - ažurirati vlastite podatke ili vidjeti popis svih korisnika (konfiguracijom se određuje broj   linija   po   stranici).   Obje   aktivnosti   provode   su   pomoću   RESTservisa

{korisnicko\_ime}\_aplikacija\_1.

- pogled2:
  - pregled svih IoT uređaja, dodavanje i ažuriranje odabranog IoT uređaja pomoću REST servisa{korisnicko\_ime}\_aplikacija\_1.
  - na temelju podataka web servisa iz{korisnicko\_ime}\_aplikacija\_1:
    - pregled zadnjih meteoroloških podataka za izabrani IoTuređaj
    - pregled važećih meteoroloških podataka za izabrani IoTuređaj
    - pregled adrese za izabrani IoTuređaj.
- pogled3:

- pregled        trenutnog        statusa        primitivnog        poslužitelja        (socket        servera)        iz

{korisnicko\_ime}\_aplikacija\_1 i njime upravljati. Obavlja se slanjem pripadajuće komande USER korisnik; PASSWD lozinka; {PAUSE; | START; | STOP; | STATUS};

- pregled trenutnog statusa grupe u IoT\_Master-u pomoću primitivnog poslužitelja (socket servera) iz {korisnicko\_ime}\_aplikacija\_1 i njime upravljati. Obavlja se slanjem pripadajuće komande USER korisnik; PASSWD lozinka; IoT\_Master {START; | STOP; | WORK; WAIT; | STATUS;}

- pogled4:

- pregled svih IoT uređaja grupe pomoću primitivnog poslužitelja (socket servera)  iz

{korisnicko\_ime}\_aplikacija\_1 i upravljati grupom. Obavlja se slanjem pripadajuće komande USER korisnik; PASSWD lozinka;IoT\_Master

{LOAD; | CLEAR; | LIST;}

- pregled statusa odabranog IoT uređaja grupe pomoću primitivnog poslužitelja (socket servera) iz {korisnicko\_ime}\_aplikacija\_1 i njime upravljati. Obavlja se slanjem pripadajuće komande USER korisnik; PASSWD lozinka; IoT d{1-6} { WORK; | WAIT; | REMOVE; |STATUS;}
- dodavanje novog IoT uređaja grupe pomoću primitivnog poslužitelja (socket servera) iz {korisnicko\_ime}\_aplikacija\_1. Obavlja se slanjem pripadajuće komande USER korisnik; PASSWD lozinka; IoT d{1-6} ADD″naziv″;

- pogled5:

o pregled dnevnika rada uz straničenje (konfiguracijom se određuje broj linija po stranici).

- pogled6:

o pregled poruka u poštanskom sandučiću (adresa poslužitelja, korisničko ime i lozinka definiraju se u postavkama) uz straničenje (konfiguracijom se određuje broj linija po stranici). Pri tome može izabrati mapu/direktorij u kojem pregledava poruke. Nazivi mapa preuzimaju se od mape poštanskog sandučića korisnika email poslužitelja. Korisnik može izvršiti brisanje svih poruka aktivne (izabrane)mape.

- pogled7:

o pregled spremljenih MQTT poruka od IoT uređaja za korisnika uz straničenje (konfiguracijom se određuje broj linija po stranici). Korisnik može izvršiti brisanje svih svojih poruka

Funkcionalnost korisničkog dijela za rad s IoT uređajima (pogled 2, pogled 3, pogled 4) treba biti realizirana pomoću Ajax-a. Dodatni bodovi mogu se dobiti ako se u pogledu 2 za prikaz odabranih IoT uređaja i njihovih podataka (npr. koordinate, trenutna temp i sl.) koristi Google Maps JavaScript API.

Pristup do podataka u bazi podataka treba biti realiziran putem ORM-a tj. putem session, entity bean-ova i criteria API.

1. enterprise aplikacija ({korisnicko\_ime}\_aplikacija\_3) koja ima EJB i Web module. Prvi dio je EJB modul koji sadrži Singleton Session Bean (SB), Stateful Sesssion Bean (SB) i Message-Driven Bean. Message-Driven Bean preuzima dvije vrste JMS poruka: za stanje obrade email poruka i primljene MQTT poruke. Primljene JMS poruke spremaju se u Singleton Session Bean (SB). Ako aplikacija prestaje s radom (brisanje Singleton SB), potrebno je poruke serijalizirati na vanjski spremnik (naziv datoteke u postavkama, smještena u WEB-INF  direktoriju). Kada se aplikacija podiže (kreiranje Singleton Session Beana) potrebno je učitati serijalizirane poruke (ako postoji datoteka) u Singleton Session Bean. Autenticiranje korisnika obavlja se u Stateful

Drugi dio je web modul koji treba realizirati putem JSF (facelets) ili PrimeFaces. Korisniku na početku obavlja prijavljivanje. Ova aplikacija nema tablicu korisnika u bazi podataka. Nakon uspješnog prijavljivanja (šalje se zahtjev REST servisu {korisnicko\_ime}\_aplikacija\_1 za podatke o korisniku te se provjeravaju) korisnik može obavljati aktivnosti kroz određene poglede:

1.
  - pogled1:
    - pregled i brisanje spremljenih JMS poruka iz reda čekanja NWTiS\_{korisnicko\_ime}\_1). Pomoću websocketa treba obavijestiti pregled poruka da je stigla nova JMS poruka njegove vrste te treba osvježiti
  - pogled2:
    - pregled i brisanje spremljenih JMS poruka iz reda čekanja NWTiS\_{korisnicko\_ime}\_2). Pomoću websocketa treba obavijestiti pregled poruka da je stigla nova JMS poruka njegove vrste te treba osvježiti
    - pregled trenutnog statusa grupe u IoT\_Master-u pomoću primitivnog poslužitelja (socket servera) iz {korisnicko\_ime}\_aplikacija\_1 i njime upravljati. Obavlja se slanjem pripadajuće komande USER korisnik; PASSWD lozinka; IoT\_Master {START; | STOP; | WORK; WAIT; | STATUS;}

## Instalacijska, programska i komunikacijska arhitektura sustava:

### {korisnicko\_ime}\_aplikacija\_1:

- Razvojni alat (IDE) kod obrane projekta:NetBeans
- Web poslužitelj:Tomcat
- EE osobine: EE6/7Web
- korisničko sučelje: JSP, JSF (facelets) iliPrimeFaces
- baza podataka: MySQL - naziv nwtis\_{korisnickoime}\_bp\_1, treba sadržavatitablice:

uredjaji, adresei ostale koje su potrebne za rad

- rad s bazom podataka: JDBC,SQL
- šalje emailporuku
- daje socketserver
- daje SOAP web servis za meteorološke podatke izabranihadresa
- daje REST  web servis za prognostičke podatke izabranihadresa
- koristi IoT\_Master web servis za upravljanje IoTuređajima
- koristi openweathermap.org REST web servis za preuzimanje meteorološkihpodataka
- koristi Google Maps API REST web servis za preuzimanje geolokacijskih podataka zaadresu
- koristi Google Maps API REST web servis za preuzimanje adrese za geolokacijskepodatke

### {korisnicko\_ime}\_aplikacija\_2:

- Razvojni alat (IDE) kod obrane projekta:NetBeans
- Web poslužitelj:Glassfish
- EE osobine:EE7
- korisničko sučelje: JSF (facelets) iliPrimeFaces
- baza podataka: JavaDB – naziv nwtis\_{korisnickoime}\_bp\_2, treba sadržavati podatke i tablice koje su potrebne za

- rad s bazom podataka: ORM (EclipseLink), CriteriaAPI
- James emailposlužitelj
- pregledava i briše emailporuke
- prima  MQTTporuke
- šalje JMS poruku u red poruka: NWTiS\_{korisnicko\_ime}\_1za statistiku obrade email poruka
- brađuje MQTTporuke
- šalje JMS poruku u red poruka: NWTiS\_{korisnicko\_ime}\_2za slot MQTT poruka
- koristi socket server {korisnicko\_ime}\_aplikacija\_1za upravljanje socket serverom, upravljanje grupom i upravljanje IoT uređajima izgrupe
- koristi SOAP web servis {korisnicko\_ime}\_aplikacija\_1za meteorološke podatke za odabrani IoT uređaj, za adresu odabranog IoTuređaja
- koristi REST web servis {korisnicko\_ime}\_aplikacija\_1za pregled, upravljanje i autenticiranjekorisnika
- koristi REST web servis {korisnicko\_ime}\_aplikacija\_1za upavljanje IoT uređajima

### {korisnicko\_ime}\_aplikacija\_3:

- Razvojni alat (IDE) kod obrane projekta:NetBeans
- Web poslužitelj:Glassfish
- EE osobine:EE7
- korisničko sučelje: JSF (facelets) iliPrimeFaces
- baza podataka: ne koristi bazu
- koristi JMS redove poruka NWTiS\_{korisnicko\_ime}\_1i

NWTiS\_{korisnicko\_ime}\_2za preuzimanje, spremanje i pregled JMS poruka

- koristi websocket za osvježavanje pregleda poruka nakon prijema nove JMSporuke
- koristi socket server {korisnicko\_ime}\_aplikacija\_1za provjeru i dodavanje adrese, preuzimanje meteoroloških podataka zaadresu
- koristi REST web servis {korisnicko\_ime}\_aplikacija\_1za autenticiranje korisnika

- putem SOAP i REST web servisa, email poruka, MQTT poruka, JMS poruka i vlastitog jezika na bazi socket servera (NE putem bazapodataka).

## Postupak za aktiviranje i korištenje web servisa openweathermap.org:

1. **1.** Dokumentacija: [**http://openweathermap.org/api**](http://openweathermap.org/api)
2. **2.** Upoznati se s ponuđenim modelima: [**http://openweathermap.org/price\_detailes**](http://openweathermap.org/price_detailes)

(FREE)

1. **3.** Registrirati se ( [**http://openweathermap.org/register**](http://openweathermap.org/register)- Register on the Sign up page)
2.
**4.**  Popunitipodatke
3. **5.** Zapisati podatke za APPID (APIkey)
4. **6.** Servisi:
  1. **a.**** Važeći vremenski podaci**- [**http://openweathermap.org/current**](http://openweathermap.org/current)

Parametri:

APIKEY=nnnnnn lokacijski:

lat=nnn&amp;lon=mmm units=metric

mode=xml | (json je po osnovi) lang=jezik (kratica)

[http://api.openweathermap.org/data/2.5/weather?lat=46.307768,&amp;lon=16.3](http://api.openweathermap.org/data/2.5/weather?lat=46.307768%2C&amp;amp;lon=16.3) **38123&amp;units=metric&amp;lang=hr&amp;APIKEY=nnnnnn**

Parametri API odgovora važećeg vremena i prognoza:

[**http://openweathermap.org/weather-data#current**](http://openweathermap.org/weather-data#current)

Ikone za vremenske uvjete: [**http://openweathermap.org/weather-conditions**](http://openweathermap.org/weather-conditions)



## Google Maps API – za dobivanje geolokacijskih podataka za adresu

### JSON

[**http://maps.google.com/maps/api/geocode/json?address=varazdin&amp;sensor=false**](http://maps.google.com/maps/api/geocode/json?address=varazdin&amp;amp;sensor=false)

XML

[**http://maps.google.com/maps/api/geocode/xml?address=varazdin&amp;sensor=false**](http://maps.google.com/maps/api/geocode/xml?address=varazdin&amp;amp;sensor=false)

Prije slanja adrese važno je obaviti njeno pretvaranje u HTTP URL format pomoću funkcije URLEncoder.encode(adresa, &quot;UTF-8&quot;)

## Google Maps API – za dobivanje adrese za geolokacijske podatake (reverse geocoding, address lookup)

### JSON

[http://maps.google.com/maps/api/geocode/json?](http://maps.google.com/maps/api/geocode/json) **latlng=xx.xxxxxx,yy.yyyyy**

XML

[**http://maps.google.com/maps/api/geocode/xml?**](http://maps.google.com/maps/api/geocode/xml) **latlng=**** xx.xxxxxx,yy.yyyyy**
