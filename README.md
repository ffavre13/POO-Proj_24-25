# Bullet Dungeon

## Screenshots
![Gameplay screenshot 1](https://github.com/user-attachments/assets/42339b16-1ed6-465b-9a4f-fc68fd0173bc)
![Gameplay screenshot 2](https://github.com/user-attachments/assets/4319b449-8d05-406a-bae2-d65b8eb10704)

## Video
https://github.com/user-attachments/assets/9d8701d7-59fe-484f-997b-8338b37c3875

## Structure
The project is structured as it follows :
```
📁 <root>
├── 📁 data              # Project ressources (audio, sprites, ...)
├── 📁 libs              # Librairies used in the project
├── 📁 src               # Main scala code 
│   ├── 📄 Game.scala    # Main entry point of the game. Launch this file to boot up the game.
│   ├── 📁 entity        # Contains the entities
│   │   └── 📁 enemies   # Contains all the enemies and bosses
│   ├── 📁 game          # Game related classes
│   └── 📁 utility       # Utility classes, used in various other files.
├── 📄 POO.tiled-project # Tiled project. Contains the tilesets and tilemaps.
└── 📄 README.md         # This file, it's in front of you right now.
```

## Controls
**Keyboard :**
- `WASD` to move
- `↑ ← → ↓` to change the orientation of the hero
- `space` to shoot
- `R` to restart the game

**Controller :**
- `Left Joystick` to move
- `A B X Y` to shoot

## Launch the project
**Using IntelliJ Community :**
1. Clone the repository
2. Set up your JDK and Scala SDK
3. Launch the `src/Game.scala` file. 
4. Enjoy ! :smile:

### Use Tiled
1. Open Tiled and on top of the window, press `File` → `Open file or project`
2. Select the `./POO.tiled-project` file

You'll be able to find the tilesets and maps, and modify them in the software.  
Each map should contain **3 layers** :  
- `map` : Map graphics, created using the tileset
- `collision` : Collisions, determinates where the player cannot go
- `door_collision` : This layer is used by the game to manage the doors between rooms
