# MCWebIntegration
🌏🎮 Integrate data provided from Minecraft server with Web API.

## Web API: `http://<host>:<web_port>`
* `/online/<uuid with hyphen>`: Returns whether the specified player is online.
* `/last/<uuid with hyphen>` returns the specified player's last online timestamp with unixtime.
* `/first/<uuid with hyphen>` returns timestamp when the specified player first joined the server.

## Setup
### Prerequisites
* Working MySQL server
### Step-by-step
1. Copy and paste the plugin jar file into plugins/
2. Create database for this plugin.
3. Run the server and ignore the error if exists, then stop the server.
4. Edit `config.yml` as you like.
5. Run the server again.

## License
This repository is licensed under the MIT License.
