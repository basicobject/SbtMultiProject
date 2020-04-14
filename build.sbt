name := "SbtMultiBuildProject"

organization := "io.github.basicobject"

version := "1.0.0"

scalaVersion := "2.13.1"

lazy val common = project in file("Common")

lazy val serviceA = (project in file("ServiceA")) dependsOn common

lazy val serviceB = (project in file("ServiceB")) dependsOn common

lazy val root = (project in file(".")).aggregate(common, serviceA, serviceB)
