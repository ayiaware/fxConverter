# fxconverter

Demo app demonstrating how to consume and cache data from REST API's in android with Retofit, Moshi, Coroutines, LiveData, Flows and Jetpacks (Room, ViewModel).
FxConverter provides exchange rate information for various currencies around the world (over 100 currencies) it also lets you calculate the exchange value between two currencies.
### This could be implemented as a feature in a larger project.

<p align="center">
<img src="/previews/screenshot1.png"/>
</p>

<p align="center">
<img src="/previews/screenshot2.png"/>
</p>

### Functionalities
- View rates and currencies
- Search rates and currencies
- Convert currencies
- Cache rates and currencies
- Clear rates and currencies cache
- Dynamically switch between linear and grid view while still maintaining scroll position.

### Functionalities to be added
- Favorite currencies
- Pin currencies
- Get notifications for pinned currencies
- Sort currencies
- Filter currencies
- Rates history and charts
- Crypto currencies

### Other topics covered in this project
- Custom moshi Adapters
- Dependency Inject with Hilt
- Data binding
- Two-way binding
- How to implement settings 
- Form validation

## Download
Go to the [Releases](https://github.com/ayiaware/fxconverter/releases) to download the latest APK.


## Tech stack & Open-source libraries
- API source [ExchangeRate-API](https://app.exchangerate-api.com/)
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) 
- Uses both Kotlin Flows and Java Live Data
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [Moshi](https://github.com/square/moshi/): A modern JSON library for Kotlin and Java.
- [Material-Components](https://github.com/material-components/material-components-android) for ui design.
- [Glide](https://github.com/bumptech/glide) Loading images from network.
- [Timber](https://github.com/JakeWharton/timber): A logger with a small, extensible API.

[![license](https://img.shields.io/github/license/DAVFoundation/captain-n3m0.svg?style=flat-square)](https://github.com/ayiaware/fxconverter/blob/master/License)

## Find this repository useful? leave a⭐ for this repository [follow me](https://github.com/ayiaware) on GitHub 🤩

