pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // kakao login repository
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }

        // naver maps repository
        maven("https://repository.map.naver.com/archive/maven")

    }
}

rootProject.name = "TPProject"
include(":app")
 