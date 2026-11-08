# Minecraft Report Plugin

ReportWatch is a Minecraft report plugin for Spigot/Paper servers that provides player reports, staff notifications, clickable teleport and spectator actions, and support for target names containing dots, including Bedrock Edition names such as `.PlayerName`.

## Install

1. Build the project with Maven:

   ```bash
   mvn package
   ```

2. Copy the generated `target/plugin.jar` file to your server's `plugins` directory.
3. Run a Paper 1.21 server using Java 21 or newer.
4. Start or restart the server.
5. Grant trusted staff the `report.receive` permission through your permissions plugin or server configuration.

## Use

Players submit a report with:

```text
/report [player] [reason]
```

The first word is treated as the target player name. Everything after the first word becomes the reason, so names containing dots are accepted.

Example:

```text
/report .PlayerName Using suspicious movement
```

Operators cannot submit reports. Players cannot report themselves.

Staff members with `report.receive` receive the report details and two clickable buttons:

- `[TELEPORT]` teleports the staff member to the target.
- `[SPECTATE]` changes the staff member to spectator mode and teleports them to the target.

The associated commands are:

```text
/report-teleport-normal [player]
/report-teleport-spec [player]
```

Both teleport commands require `report.receive` and report an error when the target is offline.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/68c2f99e-8284-4c35-bfbd-9eae483cd100?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=68c2f99e-8284-4c35-bfbd-9eae483cd100
