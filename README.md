Lilypad Minigame Lobby
=====================

Lilypad Minigame Lobby is a simple Minecraft plugin built for Lilypad networks, allowing you to quickly and easily setup game lobbies with support for multiple gameboards per-server, each gameboard can listen for different channels, allowing you to have one central hub for all your gamemodes.

> **Note:** This plugin doesn't do anything without the [Lilypad Minigame Hook](https://github.com/Senither/Lilypad-Minigame-Hook) plugin.

## Commands

There is only one central command for _Lilypad Minigame Lobby_ which is `/LilypadMinigame`, aliases for the plugin includes `/LilypadGame`, `/MinigameLobby`, `/GameLobby`, `/LilypadLobby`, `/LilyLobby`.

All usages of the commands below requires the `lilypadminigame.use` permission node to use.

 - /LilypadMinigame \<setup\>
   - **Description**: Enters setup mode, allowing players to create gameboards.
   - **Aliases**: create
 - /LilypadMinigame \<servers\>
   - **Description**: Displays a list of servers connected to the network and their status.
   - **Aliases**: server, list
 - /LilypadMinigame \<boards\>
   - **Description**: Lists all of the existing boards, what channel they're listening on how big they are.
   - **Aliases**: board, walls, wall
 - /LilypadMinigame \<board name\> \<setting\> \<value\> 
   - **Description**: Modifies the settings of an existing game board, use the command in-game to see a full list of settings available.
   - **Aliases**: _can be any valid game board name_


## Setup and Installation

1. Download the [jar file](https://github.com/Senither/Lilypad-Minigame-Lobby/releases)
2. Drop the jar file into your `plugins` folder and restart your server.
3. Jump into the game and setup one or more gameboards.
4. You're done!

## Build minigames for Lilypad Minigame Lobby

Lilypad Minigame Lobby is part of a two plugin system, to build minigames that are able you interact with the Minigame Lobby, checkout [Lilypad Minigame Hook](https://github.com/Senither/Lilypad-Minigame-Hook), the plugin comes with an easy to use API that allows developers to quickly and easily create custom placeholders, teleport players between servers on the network, and toggle visibility for game servers.

To learn more, you can also checkout the [Lilypad Minigame Hook wiki](https://github.com/Senither/Lilypad-Minigame-Hook/wiki).

## Contributing

Before creating an issue, please ensure that it hasn't already been reported/suggested.

If you wish to contribute to the Lilypad Minigame Lobby codebase or documentation, feel free to fork the repository and submit a
pull request. we follow the "fork-and-pull" Git workflow.

 1. **Fork** the repo on GitHub
 2. **Clone** the project to your own machine
 3. **Commit** changes to your own branch
 4. **Push** your work back up to your fork
 5. Submit a **Pull request** so that we can review your changes

> **NOTE:** Be sure to merge the latest from "upstream" before making a pull request!

## License

Lilypad Minigame Lobby is open-sourced software licensed under the [GNU General Public License v3.0](http://www.gnu.org/licenses/gpl.html).