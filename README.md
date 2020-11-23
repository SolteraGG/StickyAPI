# StickyAPI

![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/DumbDogDiner/StickyAPI/build/release?label=builds&logo=github)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/DumbDogDiner/StickyAPI/docs/release?label=docs&logo=github)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/DumbDogDiner/StickyAPI?label=release&logo=java)
![Code Coverage (master)](https://img.shields.io/codecov/c/gh/DumbDogDiner/StickyAPI)

Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins.

## Packages

- `common` - features common between both Bukkit and Bungee. If you are unsure which server type your plugin will be running on, use this.
- `bungee` - features specific to BungeeCord and its API - **will not work outside of BungeeCord.**
- `bukkit`- features specific to Bukkit and its API - **will not work outside of Bukkit.**

## Development

The aim of StickyAPI is to provide a base set of utilities wrapping Bukkit and its plugin API to aid the development of our plugins.

### Branching

- `master` - the current working branch, accepts merges for features and hotfixes
- `release` - release branch, accepts merges for hotfixes

### Important Links

- [GH Project](https://github.com/DumbDogDiner/StickyAPI/projects/2)
- [Javadoc Guidelines](https://google.github.io/styleguide/javaguide.html#s7-javadoc)

## Contributing

This project is the shared responsibility of all of the developers working for DDD, or anyone who choses to contribute. Before contributing, please read the [contribution guidelines](CONTRIBUTING.md), so nothing fucky wucky happens while you're working on the API.

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

# Development Quotes

- "_When Unsafe.java is the only safe option..._"

- "_New in 2.0 - `UnsafeUtils`! Because Java is great at removing features we need, so we are forced to use a internal jre class literally called `Unsafe`._"


- "_**MD_5 and his knobbery continues.** ... Because MD_5 couldn't help but program like a 12 year old we now have to use reflection to get a more reasonable way to register commands._"