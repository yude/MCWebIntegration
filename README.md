# MCWebIntegration
🌏🎮 Integrate data provided from Minecraft server with Web API.

## Web API: `http://<host>:<web_port>`
### Common
* If the specified UUID is invalid, endpoints return `invalid_uuid`.
* If the specified player is not found, endpoints return `not_found`.
### Player online status
* `/online/<uuid with hyphen>`\
Return whether the specified player is online. (`true` or `false`)
### Timestamp
* `/last/<uuid with hyphen>`\
Return the specified player's last online timestamp in unixtime.
* `/first/<uuid with hyphen>`\
Return the specified player's the first joined timestamp in unixtime.
### Group
* `/group/<uuid with hyphen>`\
Return the most important user group to which the specified player belongs. \
(e.g. If user belongs 2 group: `["default", "staff"]`, the result will be `staff`.)
* `/groups/<uuid with hyphen>`\
Return all groups to which the specified player belongs. \
(e.g. If user belongs 2 group: `["default", "staff"]`, the result will be `["default", "staff"]`.)
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
