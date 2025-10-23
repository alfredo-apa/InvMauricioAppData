# Changes Applied
- Connect.java now copies `BD/invmauricio.db` from inside the JAR to the AppData folder on first run and opens a writable connection there (WAL + busy_timeout + foreign_keys ON).
- SqliteQuery.java and MainInterface.java no longer use `jdbc:sqlite::resource:` nor `src/BD/...`. All connections go through `BD.Connect.open()`.
- If no template DB is found inside the JAR, an empty DB file is created in AppData: 
  - Windows: %LOCALAPPDATA%\InvMauricio\invmauricio.db
  - macOS: ~/Library/Application Support/InvMauricio/invmauricio.db
  - Linux: ~/.local/share/InvMauricio/invmauricio.db

## After building
- Ensure your JAR still includes `BD/invmauricio.db` as a resource if you want to seed from a template.
- On first run, data is copied out and all writes persist to the AppData copy.
