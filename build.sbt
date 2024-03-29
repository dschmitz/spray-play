organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.5"
  val sprayV = "1.3.1"
  Seq(
    "io.spray"            %   "spray-can"     % sprayV,
    "io.spray"            %   "spray-routing" % sprayV,
    "io.spray"            %   "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "com.typesafe.play"   %%  "play-json"     % "2.3.4",
    "org.specs2"          %%  "specs2-core"   % "2.3.7" % "test",
    "com.wandoulabs.akka" %% "spray-websocket" % "0.1.2" exclude("io.spray", "spray-can") exclude("io.spray", "spray-routing") 
  )
}

Revolver.settings
