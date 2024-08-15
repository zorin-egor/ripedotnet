# ripe.net

<b>ripe.net</b> - sample app for search by ip/name organizations with ripe.net service. Use multi modals app developed with android architecture components, conventions plugins.

``` text
├── app.............. Entry point to the mobile application
│   └── NavHost.... App navigation coordination
├── core......... Independent project/component logic
│   ├── common.......... Utilities, extension functions, helpers
│   ├── network.......... Interaction with the network
│   ├── datastore.......... Logic for saving primitive data and objectsв
│   ├── datastore-proto.......... Description of interaction models
│   ├── database....... Database
│   ├── data....... Repositories
│   ├── domain....... Business logic
│   ├── model....... Business logic models
│   ├── designsystem....... Basic UI components, themes, color schemes
│   ├── ui....... Comprehensive UI components for a specific presentation
├── features....... All screens are divided into module-features
│   ├── inetnum_by_ip.......... Feature search network/organization by ip
│   ├── inetnums_by_org_id.......... Feature search networks by organzation id
│   ├── organizations_by_name.......... Feature search organizations by name
│   └── settings.......... Feature customization of the application theme
└──gradle-plugins.......... Convention gradle plugin for forwarding dependencies between modules
```

## Compose compiler metrics

Run the following command to get and analyse compose compiler metrics:

```bash
./gradlew assembleRelease -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true
```

## Screenshots
<p align="center">
  <a>
    //<img alt="ArchitectureComponentsSample" src="https://github.com/zorin-egor/ripedotnet/assets/13707343/c8727676-6315-4d06-8697-22e04cc46217testtes" />
  </a>
</p>