# MCWebIntegration
🌏🎮 Integrate data provided from Minecraft server with Web API.

## Web API: `http://<host>:<web_port>`
* `/online/<uuid with hyphen>`: Returns whether the specified player is online.
* `/last/<uuid with hyphen>` Returns the specified player's last online timestamp in unixtime.
* `/first/<uuid with hyphen>` Returns the specified player's the first joined timestamp in unixtime.

## Setup
### Prerequisites
* Working MySQL server
* [LuckPerms](https://luckperms.net) 5.x
### Step-by-step
1. Copy and paste the plugin jar file into plugins/
2. Create database for this plugin.
3. Run the server and ignore the error if exists, then stop the server.
4. Edit `config.yml` as you like.
5. Run the server again.

## License
This repository is licensed under the MIT License.
