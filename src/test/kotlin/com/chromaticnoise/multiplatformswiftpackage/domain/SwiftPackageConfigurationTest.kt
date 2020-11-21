package com.chromaticnoise.multiplatformswiftpackage.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldStartWith
import io.mockk.every
import io.mockk.mockk

class SwiftPackageConfigurationTest : StringSpec() {

    init {
        "tools version property should match the tools version name" {
            configuration()
                .copy(toolVersion = SwiftToolVersion.of("42")!!)
                .templateProperties["toolsVersion"].shouldBe("42")
        }

        "name property should match the project name" {
            configuration()
                .copy(project = mockk(relaxed = true) { every { name } returns "project name" })
                .templateProperties["name"].shouldBe("project name")
        }

        "platforms property should match the given platforms" {
            configuration()
                .copy(platforms = "my platforms")
                .templateProperties["platforms"].shouldBe("my platforms")
        }

        "is-local property should be true if distribution mode is local" {
            configuration()
                .copy(distributionMode = DistributionMode.Local)
                .templateProperties["isLocal"].shouldBe(true)
        }

        "is-local property should be false if distribution mode is remote" {
            configuration()
                .copy(distributionMode = DistributionMode.Remote(DistributionURL("")))
                .templateProperties["isLocal"].shouldBe(false)
        }

        "url property should be null if distribution mode is local" {
            configuration()
                .copy(distributionMode = DistributionMode.Local)
                .templateProperties["url"].shouldBeNull()
        }

        "url property should match the value of the remote distribution url" {
            (configuration()
                .copy(distributionMode = DistributionMode.Remote(DistributionURL("my url")))
                .templateProperties["url"] as String).shouldStartWith("my url")
        }

        "url property should end with the zip file name" {
            (configuration()
                .copy(distributionMode = DistributionMode.Remote(DistributionURL("my url")))
                .templateProperties["url"] as String).shouldEndWith(".zip")
        }

        "checksum property should match the value of zip checksum" {
            configuration()
                .copy(zipChecksum = "the checksum")
                .templateProperties["checksum"].shouldBe("the checksum")
        }
    }

    private fun configuration() = SwiftPackageConfiguration(
        project = mockk(relaxed = true),
        frameworkName = FrameworkName("project name"),
        toolVersion = SwiftToolVersion.of("42")!!,
        platforms = "",
        distributionMode = DistributionMode.Local,
        zipChecksum = ""
    )
}
