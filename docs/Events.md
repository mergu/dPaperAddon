## Player

```
@Events
paper player armor change (in <area>)

@Cancellable false

@Triggers when a player changes their equipped armor.

@Context
<context.slot_type> returns the type of slot being altered.
<context.new_item> returns the new dItem.
<context.old_item> returns the old dItem.
```

```
@Events
paper player starts spectating (<entity>) (in <area>)
paper player stops spectating (<entity>) (in <area>)

@Cancellable true

@Triggers when a player starts or stops spectating an entity.

@Context
<context.new_entity> returns the newly spectated dEntity.
<context.old_entity> returns the previously spectated dEntity.
```

## Server

```
@Events
paper server exception

@Cancellable false

@Triggers when the server throws an exception.

@Context
<context.message> returns the exception's message.
<context.name> returns the exception's name.
```

```
@Events
paper server list ping

@Cancellable true

@Triggers when the server is pinged for a client's server list.

@Context
<context.motd> returns the MOTD that will show.
<context.max_players> returns the number of max players that will show.
<context.num_players> returns the number of online players that will show.
<context.address> returns the IP address requesting the list.
<context.version> returns the server version sent to the client.

@Determine
"MOTD:" + Element to change the message of the day.
"NUM_PLAYERS:" + Element(Number) to change the number of displayed players.
"MAX_PLAYERS:" + Element(Number) to change the number of displayed maximum players.
"VERSION:" + Element to change the server version sent to the client.
"ICON:" + Element to change the server icon sent to the client by filename. Icon must be 64x64 pixels.
"HOVER_TEXT:" + dList to change the text displayed when hovering over the player count.
"HIDE_PLAYERS" to hide all player related information. This will make the player count "???"
```
