#!/usr/bin/groovy
def call(Map parameters = [:], body) {

  def cloud = parameters.get('cloud', 'kubernetes')
  // ensure agents are not reused
  def defaultLabel = 'maven-' + currentBuild.projectName + "-" + currentBuild.id
  def label = parameters.get('label', defaultLabel)
  def image = parameters.get('image', 'maven:latest')

  podTemplate(cloud: cloud, label: label,
    containers: [
      containerTemplate([name: 'maven', image: "${image}", command: 'cat', ttyEnabled: true,
        envVars: [
          envVar(key: '_JAVA_OPTIONS', value:'-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1')
        ],
        resourceLimitMemory: '1024Mi'])],
    volumes: []) {

      body()
  }
}
