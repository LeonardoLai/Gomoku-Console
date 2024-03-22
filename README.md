# Gomoku-Console
A console Gomoku game, with a built-in robot that fight against player.  The robot did way better than just random place pieces on the gameboard. 

***

**Here is how the robot fights:**

Begin at i = 5, the winning state. First check if robot can make i-in-a-role, and place piece to win. If robot can't make such role, check if player can make i-in-a-role, and block player from winning.

If both robot and player doesn't made such roles long enough, check for "(i-1) in-a-role" for both robot and player. Until there are someone build long enough connections. Then the robot build the role if the role was made by robot or block the role if the role was made by player.

If the robot starts the game, its first move is always the central of the board, by-passing the above checking process because there's nothing on the board. If the player starts the game, the first move from the player builds a "one-in-a-role", so that the robot just place the piece beside the player, and continue the game.
