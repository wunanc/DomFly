# DomFly

<div align="center">

**🚀 Dominion Territory Flight Plugin**

[![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](https://github.com/ColdeZhang/DomFly)
[![Minecraft](https://img.shields.io/badge/minecraft-1.21.8-green.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/java-21-orange.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/license-MIT-red.svg)](LICENSE)

[简体中文](README.md) | [English](README_EN.md)

</div>

---

### 📖 Introduction

DomFly is a lightweight Minecraft server plugin designed for Paper/Spigot servers. This plugin allows players to fly freely within their own claimed territories, providing convenience for building and managing domains while maintaining game balance.

### ✨ Features

- **🏠 Territory Flight** - Players can enable flight mode within their owned territories
- **🔐 Permission Management** - Flexible permission system for fine-grained control
- **⚡ Auto Detection** - Automatically detects if players are within territory boundaries and disables flight when leaving
- **🎯 Lightweight & Efficient** - Optimized performance with minimal server resource usage
- **🔧 Easy Configuration** - Simple and intuitive configuration file
- **🌐 Multi-language Support** - Supports custom messages and multiple languages

### 📋 Requirements

- **Minecraft Version**: 1.21.8 or higher
- **Server**: Paper / Spigot / Purpur
- **Java Version**: Java 21 or higher
- **Dependencies**: Territory/Land claiming plugin (e.g., Residence, GriefPrevention)

### 📥 Installation

1. **Download the Plugin**
   - Download the latest `DomFly.jar` from the [Releases](https://github.com/ColdeZhang/DomFly/releases) page

2. **Install on Server**
   ```bash
   # Place the jar file in your server's plugins folder
   cp DomFly.jar /path/to/your/server/plugins/
   ```

3. **Restart Server**
   ```bash
   # Restart the server to load the plugin
   /stop  # or use your preferred restart method
   ```

4. **Configure Plugin**
   - Configuration files will be generated in `plugins/DomFly/` after first startup
   - Modify configuration as needed

5. **Set Permissions**
   - Use a permissions plugin to assign appropriate permissions to players or groups

### 🎮 Usage

#### Basic Commands

- `/domfly` - Toggle flight mode within your territory
- `/domfly help` - Display help information
- `/domfly reload` - Reload configuration file (requires admin permission)

#### Workflow

1. Player enters their owned or permitted territory
2. Execute `/domfly` command to enable flight
3. Fly freely within the territory
4. Flight mode automatically disables when leaving the territory

### 🔐 Permissions

| Permission Node | Description | Default |
|----------------|-------------|---------|
| `domfly.use` | Allows using basic flight functionality | false |
| `domfly.admin` | Allows using admin commands (e.g., reload) | op |
| `domfly.bypass` | Bypass territory detection (fly anywhere) | op |

### ⚙️ Configuration

Configuration file location: `plugins/DomFly/config.yml`

```yaml
# DomFly Configuration File

# Flight Settings
flight:
  # Disable flight immediately when leaving territory
  disable-on-leave: true
  # Check interval (seconds)
  check-interval: 1

# Message Settings
messages:
  # Flight enabled
  flight-enabled: "&aFlight enabled!"
  # Flight disabled
  flight-disabled: "&cFlight disabled!"
  # Not in territory
  not-in-territory: "&cYou are not in your territory!"
  # No permission
  no-permission: "&cYou don't have permission to use this feature!"
```

### 🛠️ Development Build

#### Prerequisites

- JDK 21 or higher
- Maven 3.6 or higher
- Git

#### Build Steps

```bash
# Clone the repository
git clone https://github.com/ColdeZhang/DomFly.git
cd DomFly

# Build with Maven
mvn clean package

# Built artifact is located at target/DomFly-2.0.0.jar
```

### 🤝 Contributing

Issues and Pull Requests are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### 📝 Changelog

See [CHANGELOG.md](CHANGELOG.md) for detailed version history.

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 👥 Authors

- **Hotguo** - *Main Developer*

### 🙏 Acknowledgments

Thanks to all developers and users who contributed to this project!

### 📞 Support & Feedback

- 🐛 [Report Bugs](https://github.com/ColdeZhang/DomFly/issues)
- 💡 [Feature Requests](https://github.com/ColdeZhang/DomFly/issues)
- 📧 Contact: Through GitHub Issues

---

<div align="center">

**Made with ❤️ by Hotguo**

⭐ If you like this project, please consider giving it a star!

</div>
