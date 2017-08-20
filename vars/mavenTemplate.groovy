#!/usr/bin/groovy
def call(Map parameters = [:], body) {

  def cloud = parameters.get('cloud', 'kubernetes')
  def defaultLabel = buildId('maven')
  def label = parameters.get('label', defaultLabel)
  def mavenImage = parameters.get('mavenImage', 'maven:latest')

  podTemplate(cloud: cloud, label: label,
    containers: [
      containerTemplate([name: 'maven', image: "${mavenImage}", command: 'cat', ttyEnabled: true,
        envVars: [
          envVar(key: '_JAVA_OPTIONS', value:'-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1')
        ],
        resourceLimitMemory: '1024Mi'])],
    volumes: []) {

      body()
  }
}
