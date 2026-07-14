<div align="center">

**🚀 Minecraft 领地飞行插件**

[![Latest Build](https://img.shields.io/github/v/release/wunanc/DomFly?label=%E6%9C%80%E6%96%B0%E6%9E%84%E5%BB%BA%E4%B8%8B%E8%BD%BD&logo=github&color=0aa344)](https://github.com/wunanc/DomFly/releases/latest)
![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Folia-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

[简体中文]  | [English](README_EN.md)

</div>

---

### 📖 项目简介

DomFly 是一个轻量级的 Minecraft 服务器插件，专为 Paper/Spigot/Folia 服务器设计。该插件允许玩家在自己的领地范围内自由飞行，为建筑和管理领地提供便利，同时保持游戏平衡性。

### ✨ 主要特性

- **🏠 领地飞行** - 玩家可以在自己拥有的领地内启用飞行模式
- **🔐 权限管理** - 灵活的权限系统，可精细控制玩家飞行权限
- **⚡ 自动检测** - 自动检测玩家是否在领地范围内，离开领地自动禁用飞行
- **🎯 轻量高效** - 优化的性能，对服务器资源占用极小
- **🌐 多语言支持** - 支持自定义消息和多语言

### 📋 系统要求

- **Minecraft 版本**: 1.20.1 或更高
- **服务端**: Paper / Spigot / Purpur / Folia
- **Java 版本**: Java 17 或更高
- **依赖插件**: 需要领地插件Dominion

### 📥 安装方法

1. **下载插件**
   - 从 [Releases](https://github.com/wunanc/DomFly/releases) 页面下载最新版本的 `DomFly.jar`

2. **安装到服务器**
   ```bash
   # 将 jar 文件放入服务器的 plugins 文件夹
   cp DomFly.jar ./plugins/
   ```

3. **重启服务器**
   ```bash
   # 重启服务器以加载插件
   stop  # 或使用其他重启命令
   ```

4. **配置插件**
   - 本插件暂无配置文件

5. **设置权限**
   - 使用LP等权限插件设置权限

### 🎮 使用方法

#### 基础命令

- `/domfly` - 在自己的领地内切换飞行模式
- `/domfly help` - 显示帮助信息
- `/domfly reload` - 重新加载配置文件（需要管理员权限）
- `/domfly undomfly <玩家>` - 强制关闭某位玩家的飞行

#### 使用流程

1. 玩家进入自己拥有或有权限的领地
2. 给予玩家 `domfly.use` 权限
3. 输入 `/domfly` 命令启用飞行
4. 在领地内自由飞行
5. 离开领地时自动禁用飞行模式

### 🔐 权限节点

| 权限节点           | 说明                 | 默认值 |
|----------------|--------------------|-----|
| `domfly.use`   | 允许使用基础飞行功能         | op  |
| `domfly.admin` | 允许使用管理命令（如 reload） | op  |

### ⚙️ 配置说明

配置文件位置：`plugins/DomFly/config.yml`

#### 暂无


### 🛠️ 开发构建

#### 前置要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- Git

#### 构建步骤

```bash
# 克隆仓库
git clone https://github.com/wunanc/DomFly.git
cd DomFly

# 使用 Maven 构建
mvn clean package

# 构建产物位于 target/DomFly-*.jar
```

### 📝 变更日志

查看 [Commits](https://github.com/wunanc/DomFly/commits/master/) 了解详细的版本更新历史。

### 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

### 👥 作者

- **wunanc | Hotguo** - *主要开发者*

### 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户！

### 📞 支持与反馈

- 🐛 [报告 Bug](https://github.com/wunanc/DomFly/issues)
- 💡 [功能建议](https://github.com/wunanc/DomFly/issues)
- 🐧 [Q群](https://qm.qq.com/q/VQrOYySVie)
- 📧 联系作者：通过 GitHub Issues

---

<div align="center">

**Made with ❤️ by Hotguo**

⭐ 如果你喜欢这个项目，请考虑给它一个 Star！

[![bStats](https://bstats.org/signatures/bukkit/DomFly.svg)](https://bstats.org/plugin/bukkit/DomFly)

</div>