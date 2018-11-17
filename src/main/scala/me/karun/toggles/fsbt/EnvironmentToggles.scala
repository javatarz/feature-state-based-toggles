package me.karun.toggles.fsbt

import java.util

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsScalaMapConverter}

class EnvironmentToggles(fileName: String, baseKey: String, environmentName: String) {
  private val config: Config = ConfigFactory.load(fileName)
  private val stateDefinitions = config.getList("me.karun.fsbt.stateDefinition").asScala
    .map(cv => cv.unwrapped())
    .map(map => map.asInstanceOf[util.Map[String, Any]].asScala.toMap)
    .zipWithIndex
    .map(t => StateDefinition(t._1, t._2))

  private val toggles = {
    val toggleConfig = config.getConfig(baseKey)

    val invalidKeys = toggleConfig.entrySet().asScala
      .map(e => (e.getKey, e.getValue.unwrapped()))
      .map(t => (t._1, t._2))
      .map(t => (t._1, stateDefinitions.exists(_.canHandleState(t._2.toString))))
      .filter(!_._2)
      .map(_._1)

    if (invalidKeys.nonEmpty) throw new RuntimeException(s"Following toggles are mapped to states do not have a state definition: ${invalidKeys.toList.sorted.mkString(", ")}")

    toggleConfig
  }

  private val envState = {
    val maybeEnvState = stateDefinitions.find(_.canHandleEnvironment(environmentName))
    if (maybeEnvState.isEmpty) throw new RuntimeException(s"$environmentName is not mapped to a state")
    maybeEnvState.get
  }

  def toggleIsEnabled(toggleName: String): Boolean = {
    val toggleStateString = toggles.getString(toggleName)
    val toggleState = stateDefinitions.find(_.canHandleState(toggleStateString))

    if (toggleState.isEmpty) throw new RuntimeException(s"Invalid toggle state: $toggleStateString")

    envState.canRun(toggleState.get)
  }
}

