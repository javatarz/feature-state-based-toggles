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
    val (validKeys, invalidKeys) = config.getConfig(baseKey).entrySet().asScala
      .map(e => (e.getKey, e.getValue.unwrapped()))
      .map(t => (t._1, t._2))
      .map(t => (t._1, stateDefinitions.find(_.canHandleState(t._2.toString))))
      .partition(t => t._2.isDefined)

    if (invalidKeys.nonEmpty) throw InvalidTogglesException(invalidKeys.map(_._1))

    validKeys
      .map(t => (t._1, t._2.get))
      .toMap
  }

  private val envState = stateDefinitions.find(_.canHandleEnvironment(environmentName)) match {
    case Some(state) => state
    case None => throw new UnmappedEnvironmentException(environmentName)
  }

  def toggleIsEnabled(toggleName: String): Boolean = envState.canRun(toggles(toggleName))
}

