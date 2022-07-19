BackEnd Project.

 
Git:@JPiotr


-----------------------
Aplikacje są skompilowane do Javy w wersji 18.0.1.1 2022-04-22
Aby uruchomić aplikacje potrzebna jest baza MongoDB z udostępnionym portem 5003. (najlepiej uruchomiona za pomocą dockera)
Potrzebna również będzie uruchomiona aplikacja odpowiedzialna za RabbitMQ na porcie 5672

---------------------------------------------------
Kolejność uruchamiania aplikacji ma znaczenie!
Pierwsze uruchaiamy appRunner a później serviceApi.
---------------------------------------------------

Dane do logowania:
Mongo: domyślne
RabbitMQ: domyślne

Uruchomienie aplikacji:

w cmd wpisujemy komendę: 
java -jar appRunner.jar

Aplikacja działa na porcie 5004

w drugiej konsoli cmd:
java -jar serviceApi.jar

Aplikacja działa na porcie 5005

-------------------------
Krótki opis:

Aplikacja ma na celu poprawienie jakości pracy - obsługi serwisowej klientów.
Aplikacja pozwala na zarejestrowanie klientów do systemu, wraz z wszystkimi potrzebnymi danymi połączeniowymi
Aplikacja pozwala na uruchamianie aplikacji odpowiedzialnych za połączenia.
Aplikacja pozwala na logowanie się do systemu, oraz ogranicza prawa dostępu do danych na podstawie ról. (in progress)

AppRunner - mikroserwis odpowiedzialny za uruchamianie aplikacji, oraz ich rejestracji
github: https://github.com/JPiotr/AppRunner
SerwiceApi - serwis odpowiedzialny za zbieranie informacji dostępowych do klientów. 
github: https://github.com/JPiotr/Service-Api/tree/master/Accounts
------------------------

Dodatkowo dołączona zostaje: 
Export catalogów zapytań z Postman-a. 
Java w wymaganej do uruchomienia wersji.
Skrypty uruchomieniowe aplikacji.






