# SimpleCrawler

Der Crawler erhällt 3 Configs. 

-> eine Zur Configurationd er Datenbank,

-> eine für den Crawler

-> eine für die Datenstruktur

Es wird vorausgesetzt, dass die Crawler den gleichen Namen haben, dafür unterschiedliche Fileendings. Zusätzlich sollen die sich alle im gleichem Pfad befindent, sodass der Aufruf nicht alzu kompliziert wird.

Der Crawler wird gestartet durch folgenden Befehl:
```
java -jar prog\CrawlerAnleihen.jar crftz .\Config\
```
configname (ohne ". "etc)


Die wurde so gewählt, um es Übersichtlicher zu gestalten. Bisher waren gab es eine Config. Es ist quasi ein Test ob es Sinn ergibt.

Es liegen 2 Configurationen bereit: eines für Finanzen.net Anleihen und eine für Zertifikate. Weiterhin sind 2 txt Dateien mit Job-Inserts für die Crawler-Job Tabelle bereitgestellt worden. Diese sind rein zufällig gewählt, sodass nicht jeder Eintrag tatsächlich Werte generiert durch den Crawler.


#Abblauf:

Der Ablauf und die der Lebenszyklos sind in der Classe Sched, Funktion run() enthalten. Alle anderen Classen bilden Funktionalitäten für das gesammtkonzept.

Anfangs wird die Robots.txt Datei gelesen. Falls dieser Bot nicht berechtigt ist bricht er sich selbst ab. 
Anschließend wird der nächste Job aus der Datenbank gefteched und anschließend auf der Seite gecrawled. 
Die gecrawlten Daten werden in die DatenBank geschrieben.

Falls es kein Job gibt, oder der Job bereits abgeschlossen ist, schläft dieser Crawler. Die Schlafzeit ist über die Crawler Config setzbar.

Es gibt 2 verschiedene Schlafzeiten, eine falls es kein Job gegeben hat und eine falls der Job abgeschlossen wurde, sodass es möglich ist zB, bei keinem vorhandenem Job länger pause zu machen.
