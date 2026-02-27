<div align="center">

**🚀 Minecraft Land Flight Plugin**

[![Latest Build](https://img.shields.io/github/v/release/wunanc/DomFly?label=%E6%9C%80%E6%96%B0%E6%9E%84%E5%BB%BA%E4%B8%8B%E8%BD%BD&logo=github&color=0aa344)](https://github.com/wunanc/DomFly/releases/latest)
![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Folia-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

[English]  | [简体中文](README.md)

</div>

---

### 📖 Introduction

DomFly is a lightweight Minecraft server plugin designed for Paper/Spigot/Folia servers. It allows players to fly freely within their claimed lands, providing convenience for building and land management while maintaining game balance.

### ✨ Features

- **🏠 Land Flight** - Players can enable flight mode within lands they own
- **🔐 Permission Management** - Flexible permission system for fine-grained control over player flight
- **⚡ Auto Detection** - Automatically detects if a player is inside their land and disables flight upon leaving
- **🎯 Lightweight & Efficient** - Optimized for minimal server resource usage
- **🌐 Multi-language Support** - Customizable messages and multi-language support

### 📋 Requirements

- **Minecraft Version**: 1.20.1 or higher
- **Server Software**: Paper / Spigot / Purpur / Folia
- **Java Version**: Java 21 or higher
- **Dependency**: Requires the Dominion land plugin

### 📥 Installation

1. **Download the plugin**
   - Download the latest `DomFly.jar` from the [Releases](https://github.com/wunanc/DomFly/releases) page

2. **Install to server**
   ```bash
   # Place the jar file into the server's plugins folder
   cp DomFly.jar ./plugins/
   ```

3. **Restart the server**
   ```bash
   # Restart the server to load the plugin
   stop  # or use other restart commands
   ```

4. **Configure the plugin**
   - This plugin currently has no configuration file

5. **Set up permissions**
   - Use a permissions plugin like LuckPerms to assign permissions

### 🎮 Usage

#### Basic Commands

- `/domfly` - Toggle flight mode within your own land
- `/domfly help` - Display help information
- `/domfly reload` - Reload the configuration file (requires admin permission)
- `/domfly undomfly <player>` - Forcefully disable flight for a player

#### How to Use

1. Player enters a land they own or have permission in
2. Grant the player the `domfly.use` permission
3. Use the `/domfly` command to enable flight
4. Fly freely within the land
5. Flight mode is automatically disabled when leaving the land

### 🔐 Permission Nodes

| Permission Node | Description | Default |
|---------|------|-----|
| `domfly.use` | Allows basic flight functionality | op |
| `domfly.admin` | Allows admin commands (e.g., reload) | op |

### ⚙️ Configuration

Configuration file location: `plugins/DomFly/config.yml`

#### Currently none

### 🛠️ Development Build

#### Prerequisites

- JDK 21 or higher
- Maven 3.6 or higher
- Git

#### Build Steps

```bash
# Clone the repository
git clone https://github.com/wunanc/DomFly.git
cd DomFly

# Build with Maven
mvn clean package

# The built artifact is located at target/DomFly-*.jar
```

### 📝 Changelog

See [CHANGELOG.md](CHANGELOG.md) for detailed version history.

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 👥 Author

- **wunanc | Hotguo** - *Main Developer*

### 🙏 Acknowledgements

Thanks to all the developers and users who have contributed to this project!

### 📞 Support & Feedback

- 🐛 [Report a Bug](https://github.com/wunanc/DomFly/issues)
- 💡 [Feature Requests](https://github.com/wunanc/DomFly/issues)
- 📧 Contact the author: via GitHub Issues

---

<div align="center">

**Made with ❤️ by Hotguo**

⭐ If you like this project, consider giving it a Star!

[![bStats](https://bstats.org/signatures/bukkit/DomFly.svg)](https://bstats.org/plugin/bukkit/DomFly)

</div>