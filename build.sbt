organization := "me.karun.toggles"
name := "feature-state-based-toggles"

version := "1.0.0"

scalaVersion := "2.12.7"

libraryDependencies += "com.typesafe" % "config" % "1.3.3"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

resolvers += Resolver.jcenterRepo

licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0"))

pomExtra :=
    <scm>
        <connection>
            scm:git:git://github.com/javatarz/feature-state-based-toggles.git
        </connection>
        <url>
            https://github.com/javatarz/feature-state-based-toggles
        </url>
    </scm>
        <developers>
            <developer>
                <id>javatarz</id>
                <name>Karun Japhet</name>
                <email>karun@japhet.in</email>
            </developer>
        </developers>
