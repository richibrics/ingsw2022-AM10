# Communication Protocol

## Overview

The format used for all the messages is JSON, which is language indipendent. The messages have the following fields:
- type, which is a string that describes the type of the message;
- content, which contains the content of the message.

## Connection establishment

The server listens for client connection requests. Once a connection is established, the server notifies the client that their request was successful. Then the client sends a USER message to the server. A USER message has the following structure:

```
{
    "type": "USER",
    "content": {
        "id": "username",
        "preference": numOfPlayers
        "register": true/false
    }
}
```

The username is a string that identifies the client, while preference is an integer that represents the type of game the client wants to play (two-player game, three-player game or four-player game). The server than adds the user to the waiting list of the lobby. The client can decide to change their preference by sending a USER message with register set to false. In this case the username must be the same used by the client in the registration process. If in the meanwhile a game has been created, the server refuses the request and notifies the client.

## STILL_ALIVE_MSG message

Both the server and the clients periodically send STILL_ALIVE_MSG messages to check if the connection is still alive. If a STILL_ALIVE_MSG message is not received before a specified timeout is reached, the connection is closed. Once the message is received, the timeout is reset.

## LOBBY message

The server sends to all the clients waiting in the lobby the state of the lobby using a LOBBY message. The client can use this information to change their preference and access a game faster. Below is an example of LOBBY message:

```
{
    "type": "LOBBY",
    "content": {
        "listOfUsersWaiting": [
            "2-player game: 1/2",
            "3-player game: 2/3",
            "4-player game: 3/4"
        ]
    }
}
```

## ROUND message

When an action finishes executing, it can change the state of the round, which contains the order of play and the list of the actions the current player can perform. If a change of state occurs, the server sends the state of the round object to the clients participating in the game. Below is an example of ROUND message:

```
{
    "type": "ROUND",
    "content": {
        "listOfActions": [
            3,
            6
        ],
        "currentPlayer": 2
    }
}
```

## ACTION message

When the current player receives the ROUND message, they decide which action to perform and send an ACTION message back to the server. Below is an example of ACTION message:

```
{
    "type": "ACTION",
    "content": {
        "action": 5,
        "options": {
            "color": "red"
        }
    }
}
```

Options is a map which contains data required by the action.

## GAME message

The actions modify the state of the game, i.e. the state of the board and of the players. When a change occurs, the server sends the new state to the clients using a GAME message. Below is an example of GAME message. The fields in content are self-explanatory.

```
{
    "type": "GAME",
    "content": {
        "table": {
            "schoolBoards": [
                {
                    "entrance": [
                        114,
                        92,
                        97,
                        57,
                        82,
                        90,
                        32
                    ],
                    "diningRoom": [
                        [],
                        [],
                        [],
                        [],
                        []
                    ]
                },
                {
                    "entrance": [
                        93,
                        78,
                        113,
                        91,
                        128,
                        99,
                        21
                    ],
                    "diningRoom": [
                        [],
                        [],
                        [],
                        [],
                        []
                    ]
                }
            ],
            "bag": {
                "students": 98
            },
            "cloudTiles": [
                {
                    "id": 1,
                    "students": []
                },
                {
                    "id": 2,
                    "students": []
                }
            ],
            "motherNature": {
                "island": 2
            },
            "islandTiles": [
                [
                    {
                        "id": 1,
                        "students": [
                            53
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 2,
                        "students": [],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 3,
                        "students": [
                            81
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 4,
                        "students": [
                            107
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 5,
                        "students": [
                            29
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 6,
                        "students": [
                            55
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 7,
                        "students": [
                            79
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 8,
                        "students": [],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 9,
                        "students": [
                            27
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 10,
                        "students": [
                            3
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 11,
                        "students": [
                            1
                        ],
                        "noEntry": false
                    }
                ],
                [
                    {
                        "id": 12,
                        "students": [
                            105
                        ],
                        "noEntry": false
                    }
                ]
            ],
            "availableProfessorPawns": [
                {
                    "color": "YELLOW"
                },
                {
                    "color": "BLUE"
                },
                {
                    "color": "GREEN"
                },
                {
                    "color": "RED"
                },
                {
                    "color": "PINK"
                }
            ],
            "activeCharacterCards": {
                "1": {
                    "cost": 1,
                    "storage": [
                        58,
                        63,
                        106,
                        20
                    ]
                },
                "11": {
                    "cost": 2,
                    "storage": [
                        101,
                        51,
                        42,
                        121
                    ]
                },
                "6": {
                    "cost": 3,
                    "storage": []
                }
            },
            "availableNoEntryTiles": 4
        },
        "players": [
            {
                "username": "kevin",
                "coins": 3
            },
            {
                "username": "steph",
                "coins": 3
            }
        ]
    }
}
```

## END_GAME message 

Let's assume that the CheckEndMatchConditionAction detects that one of the game ending conditions is met. The server notifies the clients that the game has ended with an END_GAME message. Below is an example of END_GAME message:

```
{
    "type": "END_GAME",
    "content": {
        "winner": "username"
    }
}
```
