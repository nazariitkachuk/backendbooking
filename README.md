Temat: Opracowanie systemu wspomagającego obsługę sieci hoteli
Technologie: Java, Spring, Angular, MySQL


ROLE:

1)Administrator
2)Recepcjonista
3)Użytkownik zalogowany
4)Użytkownik niezalogowany

Opis roli (wstępnie):

1)Administrator – możliwość dodawania nowych danych do bazy danych. Przegląd wszystkich rezerwacji. Może anulować rezerwację która jeszcze się nie skończyła(dostaje niezbędne info od Recepcjonisty)

2) Recepcjonista – jeżeli ktoś dokonał rezerwacji to sprawdza czy przeszła transakcja i jeżeli tak to zaklepuje wybrany przez użytkownika pokój. W każdym przypadku Użytkownik dostaje maila ze szczegółami rezerwacji oraz wynikiem(rezerwacja powiodła się/nie powiodła się(z tłumaczeniem dla czego)). Przegląda wszystkie rezerwacje, w razie potrzeby odpowiada na komentarz, anuluję rezerwacje które wygasły, może odmówić klientowi jeżeli jego ranking jest niski,  zgłasza Administratorowi anulowanie rezerwacji która jest w toku.

3) Użytkownik zalogowany – Wszystko to co może Użytkownik niezalogowany + dostaje zniżkę za poprzednie rezerwacje, „zarabia” sobie ranking po zakończeniu rezerwacji, ma swój profile, przeglądanie wszystkich dokonanych rezerwacji i t.d.

4)Użytkownik niezalogowany – przeglądanie strony, wybór hotelu, może sprawdzić czy są dostępne pokoje na wybraną przez niego datę. Jeżeli tak, to klika rezerwuj i go przekierowuje do strony rezerwowania. Po wprowadzeniu danych dostaje info o rezerwacji oraz ma możliwość akceptacji oraz odrzucenia rezerwacji. Jeżeli akceptuje – przekierowuje go na stronę gdzie będzie mógł dokonać wpłatę zadatku. Jeżeli wpłaca to dostaje potwierdzenie dokonania wpłaty oraz informację żeby sprawdzał pocztę i przekierowuje go na stronę domową. (czyli nie musi się logować).




