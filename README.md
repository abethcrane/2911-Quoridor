2911-Quoridor
=============

Craig and Beth's 2012s2 2911 Quoridor Project

interfaces:
board
- islegalmove
 - checkwall
 - withinbounds
- update walls
- update player position

game running
- create board
- call next players turn until finished
- is finished
- winner
- numplayers

player
- whereami/position
- howmanywallsleft
- whereamigoing/goal = 0,0 - 0,2 e.g.
- move direction
- place wall

classes:
-

tests:
each function of each interface
each function of each class


general
2d array of nodes that are corners
store position as top left corner of the current square
don't store who the wall belongs to (irrelevant) - or say 0 = no wall, 1 = player 1 wall, etc
we could generalise to say 6 sides by just marking off as walled the blank areas
