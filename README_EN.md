<div align="center">

**🚀 Minecraft Territory Flight Plugin**

[![Version](https://img.shields.io/badge/version-2.0.3-blue.svg)](https://github.com/wunanc/DomFly)
[![Minecraft](https://img.shields.io/badge/minecraft-1.21.8-green.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/java-21-orange.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/license-MIT-red.svg)](LICENSE)

[简体中文](README.md)  | [English]

</div>

---

### 📖 Project Overview

DomFly is a lightweight Minecraft server plugin designed for Paper/Spigot/Folia servers. This plugin allows players to fly freely within their own territory, providing convenience for building and managing claimed lands.

### ✨ Main Features

- **🏠 Territory Flight** - Players can enable flight mode within their own claims.
- **🔐 Permission Management** - Flexible permission system for fine-grained control over player flight access.
- **⚡ Auto Detection** - Automatically detects whether a player is within their claim and disables flight upon leaving.
- **🎯 Lightweight & Efficient** - Optimized performance with minimal server resource usage.
- **🌐 Multi-language Support** - Supports custom messages and multiple languages.

### 📋 Requirements

- **Minecraft Version**: 1.21.8 or above
- **Server**: Paper / Spigot / Purpur / Folia
- **Java Version**: Java 21 or above
- **Dependency**: Requires Dominion territory plugin

### 📥 Installation

1. **Download the Plugin**
   - Download the latest `DomFly.jar` from the [Releases](https://github.com/wunanc/DomFly/releases) page.

2. **Install to Server**
   ```bash
   # Place the jar file in your server's plugins folder
   cp DomFly.jar ./plugins/
   ```

3. **Restart the Server**
   ```bash
   # Restart your server to load the plugin
   stop  # Or use another restart command
   ```

4. **Configure the Plugin**
   - This plugin currently does not have a configuration file.

5. **Set Permissions**
   - Use LuckPerms or another permission plugin to set permissions.

### 🎮 Usage

#### Basic Commands

- `/domfly` - Toggle flight mode within your own claims
- `/domfly help` - Show help information
- `/domfly reload` - Reload configuration (requires admin)
- `/domfly undomfly <player>` - Force disable flight for a player

#### Usage Flow

1. Player enters a claim they own or have access to.
2. Grant the player `domfly.use` permission.
3. Use the `/domfly` command to enable flight.
4. Freely fly within the claim.
5. Flight is automatically disabled when leaving the claim.

### 🔐 Permission Nodes

| Node            | Description                      | Default |
|-----------------|----------------------------------|---------|
| `domfly.use`    | Use basic flight functionality   | op      |
| `domfly.admin`  | Use admin commands (reload etc.) | op      |

### ⚙️ Configuration Guide

Configuration file location: `plugins/DomFly/config.yml`

#### None yet

### 🛠️ Development & Build

#### Prerequisites

- JDK 21 or above
- Maven 3.6 or above
- Git

#### Build Steps

```bash
# Clone the repo
git clone https://github.com/wunanc/DomFly.git
cd DomFly

# Build with Maven
mvn clean package

# Built artifacts will be in target/DomFly-*.jar
```

### 🤝 Contributing

Contributions are welcome! Please submit issues and pull requests.

1. Fork this repo
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a pull request

### 📝 Changelog

See [CHANGELOG.md](CHANGELOG.md) for details of version updates.

### 📄 License

This project is under the MIT License - see [LICENSE](LICENSE) for details.

### 👥 Author

- **wunanc | Hotguo** - *Main Developer*

### 🙏 Thanks

Thanks to all developers and users who contributed to this project!

### 📞 Support & Feedback

- 🐛 [Report Bugs](https://github.com/wunanc/DomFly/issues)
- 💡 [Feature Suggestions](https://github.com/wunanc/DomFly/issues)
- 📧 Contact Author: via GitHub Issues

---

<div align="center">

**Made with ❤️ by Hotguo**

⭐ If you like this project, please consider giving it a Star!

</div>
