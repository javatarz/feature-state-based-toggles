# Feature State Based Toggles

[![Build status](https://travis-ci.org/javatarz/feature-state-based-toggles.svg?branch=master)](https://travis-ci.org/javatarz/feature-state-based-toggles)

A framework to map [feature toggles](https://www.martinfowler.com/articles/feature-toggles.html) to [feature states](https://agiledemystified.com/2012/02/25/user-story-life-cycle/) to environments to simply toggles in [Continuous Delivery](https://www.thoughtworks.com/continuous-delivery) **without having an opinion** on your project's feature states, environments or how you map them.

All of the control. None of the headache.

## About the framework
### The problem with traditional feature toggles
On a typical project toggles map to boolean values that have overrides for every environment

| Environment | Feature Toggle Value |
|:-----------:|:--------------------:|
|   default   |         false        |
|     dev     |         true         |
|      qa     |         true         |
|     uat     |         true         |
|     prod    |         false        |

Accidents can happen.

It can accidentally be disabled for one of the environments (like QA)

| Environment | Actual Value | Expected Value |
|:-----------:|:------------:|:--------------:|
|   default   |     false    |      false     |
|     dev     |     true     |      true      |
|      qa     |     false    |      true      |
|     uat     |     true     |      true      |
|     prod    |     false    |      false     |

Or worse, the default value can accidentally be turned on

| Environment | Actual Value | Expected Value |
|:-----------:|:------------:|:--------------:|
|   default   |     true     |      false     |
|     dev     |     true     |      true      |
|      qa     |     true     |      true      |
|     uat     |     true     |      true      |
|     prod    |     true     |      false     |

### Solution

The framework maps a feature toggle to a feature's state (in dev, in QA etc.). Feature states are mapped to environments (dev, QA etc.)

If a toggle is enabled on an environment, it is enabled on all environments below it.

If the feature state is `in-user-acceptance-testing` which maps to the `uat` environment (similar to the first table on top), the feature toggle will be turned on in the following environments

| Environment | Toggle State |
|:-----------:|:------------:|
|    local    |    enabled   |
|     dev     |    enabled   |
|      qa     |    enabled   |
|     uat     |    enabled   |
|     prod    |   disabled   |

## How to use

The entry point to the framework is `FeatureTogglesForEnvironment`. Sample usage is available in [it's test](https://github.com/javatarz/feature-state-based-toggles/blob/master/src/test/scala/me/karun/toggles/fsbt/FeatureTogglesForEnvironmentTest.scala).

```java
// Setup
val env = new FeatureTogglesForEnvironment(fileName = "toggles.conf", baseKey = "me.karun.toggles", environmentName = "prod")

// Usage
if (env.toggleIsEnabled("feature-name")) newImplementation()
else oldImplementation()
```

Sample configuration is [available in the test as well](https://github.com/javatarz/feature-state-based-toggles/blob/master/src/test/resources/valid-toggles.conf).

Your configuration file controls the state definitions (which map states to environments).
The same configuration file also provides you the ability to map toggles to feature states (in any package structure you would like).
Configure the framework based on your project's need.

Confused about what feature states you should have? Look at your [story/feature development lifecycle](https://agiledemystified.com/2012/02/25/user-story-life-cycle/). A sample is also available in [the tests](https://github.com/javatarz/feature-state-based-toggles/blob/master/src/test/resources/valid-toggles.conf).

Confused about what environments to map to which states? Ask your self when you release software to a particular environment.

### Configuration Rules

1. State Definitions are mandatory and must always be under `me.karun.fsbt.stateDefinition`
1. The list is order specific. Toggles mapped to states lower on the list will work on environments mapped above it.
1. If the environment value provided to `FeatureTogglesForEnvironment` during instantiation is not mapped to a state definition, an exception will be thrown (as shown in [FeatureTogglesForEnvironmentTest](https://github.com/javatarz/feature-state-based-toggles/blob/master/src/test/scala/me/karun/toggles/fsbt/FeatureTogglesForEnvironmentTest.scala))
1. If the feature toggle is mapped to a state that is not defined in the state definition, the `FeatureTogglesForEnvironment` instantiation will fail (as shown in [FeatureTogglesForEnvironmentTest](https://github.com/javatarz/feature-state-based-toggles/blob/master/src/test/scala/me/karun/toggles/fsbt/FeatureTogglesForEnvironmentTest.scala))
