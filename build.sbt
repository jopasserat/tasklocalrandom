import AssemblyKeys._ // put this at the top of the file

assemblySettings

organization := "fr.isima"

name := "tasklocalrandom"

version := "0.1"

scalaVersion := "2.10.0"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
 {
  case PathList( "META-INF", "MANIFEST.MF" ) => MergeStrategy.discard
  case _ => MergeStrategy.deduplicate
 }
}

mainClass in assembly := Some("fr.isima.Main4")

resolvers ++= Seq(
  "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo",
  "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/",
  "Sonatype OSS Repository" at "http://oss.sonatype.org/content/repositories/snapshots"
)
