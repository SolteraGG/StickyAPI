# StickyAPI

![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/DumbDogDiner/StickyAPI/build/release?label=builds&logo=github)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/DumbDogDiner/StickyAPI/docs/release?label=docs&logo=github)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/DumbDogDiner/StickyAPI?label=release&logo=java)

Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins.

## Packages

- `common` - features common between both Bukkit and Bungee. If you are unsure which server type your plugin will be running on, use this.
- `bungee` - features specific to BungeeCord and its API - **will not work outside of BungeeCord.**
- `bukkit`- features specific to Bukkit and its API - **will not work outside of Bukkit.**

## Development

The aim of StickyAPI is to provide a base set of utilities wrapping Bukkit and its plugin API to aid the development of our plugins.

### Branching

- `master` - the current working branch, accepts merges for features and hotfixes
- `stable` - release branch, accepts merges for hotfixes

### Important Links

- [Trello](https://trello.com/c/kx7Ppznd/13-progress-checklist)
- [Javadoc Guidelines](https://google.github.io/styleguide/javaguide.html#s7-javadoc)

## Contributing

This project is the shared responsibility of all of the developers working for DDD. Before contributing, please read the [contribution guidelines](CONTRIBUTING.md), so nothing fucky wucky happens while you're working on the API.

## Add StickyAPI to your project

### Gradle

```
repositories {
    maven {
        credentials {
            username "$ghUser"
            password "$ghPass"
        }
        url = "https://maven.pkg.github.com/DumbDogDiner/StickyAPI/" }
}

dependencies {
    implementation "com.dumbdogdiner:stickyapi:X.X.X"
}

```

# Documentation
All javadocs are located at https://dumbdogdiner.github.io/StickyAPI/ and have earlier versions available.