# WishApp

Projekt w Javie oraz MySQL oraz SpringBoot

## Getting started

Utwórz darmowe konto na sendinblue.com, a następnie utwórz zmienne środowiskowe.
Tworząc szablon(template) maila, pamiętaj o dodaniu w polu Subject Line wartości:

- SPRING,
- SUMMER,
- AUTUMN,
- WINTER,
  jeżeli chcesz, aby maile były wysyłane według pory roku urodzenia pracownika.

Podczas tworzenia treści szablonu masz możliwość ustawienia imienia oraz nazwiska pracownika
wstawiając w treści (IMIE) jeżeli chcemy wstawić imię pracownika
oraz (NAZWISKO), jeżeli chcemy wstawić nazwisko pracownika.

Stwórz zmienne środkowiskowe:

- SENDINBLUE_API_KEY = twój sendinblue api-key
- SPRING_MAIL_HOST = twój serwer SMTP, np. smtp.poczta.onet.pl
- SPRING_MAIL_PORT = twój port
- SPRING_MAIL_USERNAME = twój username do poczty
- SPRING_MAIL_PASSWORD = twoje hasło do poczty
- TEMPLATE_ZYCZENIE = ID twojego template dla życzeń
- SUBJECT_ZYCZENIE = treść tematu maila dla życzeń
- HOUR_SEND = godzina wysyłki maili, np. 14
- SEND_MODE = rodzaj wysyłki maili, do wyboru:
  random - podczas wysyłki życzeń używa losowego template
  seasons - podczas wysyłki życzeń używa template dla danej pory roku według daty urodzin