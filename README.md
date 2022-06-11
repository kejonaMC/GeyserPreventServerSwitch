[![Discord](https://img.shields.io/discord/853331530004299807?color=7289da&label=discord&logo=discord&logoColor=white)](https://discord.gg/M2SvqCu4e9)

# GeyserPreventServerSwitch
Prevent Bedrock players from joining specific subservers on a proxy

Default config:
```yaml
# A list of servers to prevent Geyser players connecting to
prohibited-servers:
  - skyblock
  - survival

# Message to send to players when a server connection is cancelled. Set blank or comment out to disable
message: "You are not allowed to join that server! :("

# Use Geyser for checking players or Floodgate? (Keep as default unless Geyser is not on your server)
use-floodgate: false
```

Any player with the permission `geyserpreventserverswitch.server.bypass` will not be blocked from joining any server.  
Additionally, any player with the permision `geyserpreventserverswitch.server.bypass.<servername>` will not be blocked from joining the specified server. 
