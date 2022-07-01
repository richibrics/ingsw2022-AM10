# Eriantys

Group ID: AM10

### Group components

Riccardo Briccola - email: riccardo.briccola@mail.polimi.it

Leonardo Giovanni Cavallini - email: leonardogiovanni.cavallini@mail.polimi.it

Marco Calzavara - email: marco2.calzavara@mail.polimi.it

Build status:  [![Build](https://github.com/riccardo-briccola/ingsw2022-AM10/actions/workflows/report.yml/badge.svg?branch=main)](https://github.com/riccardo-briccola/ingsw2022-AM10/actions/workflows/report.yml)

### Project status

<!DOCTYPE html>

Functionality | State
-- | --
Basic rules | 🟢
Complete rules | 🟢
CLI | 🟢
GUI | 🟢
Socket | 🟢
12 character cards | 🟢
4 players match | 🟢
Multiple matches | 🟢


## Peer Review 1

Content: UML Class Diagram

Group reviewed: AM40

Reviewed by: AM37

## Peer Review 2

Content: Communication Protocol

Group reviewed: AM40

Reviewed by: AM37

# Test coverage

All tests in model and controller has a classes' coverage at 100%.

**Coverage criteria: code lines.**

| Package | Coverage |
|:-----------------------|:------------------------------------:|
| Model | 96% (1477/1528)
| Model.game_components | 100% (218/218)
| Model.actions | 97% (769/789)
| Model.managers | 94% (323/341)
| Controller | 91% (398/437)
| Controller.gson_serializers | 92% (120/130)
| Controller.observers | 100% (26/26)

# JAR execution

We deployed Server and Client in a single JAR file.

## Server

Start the server using the default port (12987):

```
java -jar AM10-1.0-SNAPSHOT.jar
```

Start the server using a custom port:

```
java -jar AM10-1.0-SNAPSHOT.jar PORT
```

## Client

### GUI


```
java -jar AM10-1.0-SNAPSHOT.jar SERVER_ADDRESS SERVER_PORT
```

### CLI


```
java -jar AM10-1.0-SNAPSHOT.jar SERVER_ADDRESS SERVER_PORT -c
```

or

```
java -jar AM10-1.0-SNAPSHOT.jar SERVER_ADDRESS SERVER_PORT --cli
```


