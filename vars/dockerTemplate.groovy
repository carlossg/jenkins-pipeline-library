#!/usr/bin/groovy
def call(Map parameters = [:], body) {

  def cloud = parameters.get('cloud', 'kubernetes')
  // ensure agents are not reused
  def defaultLabel = 'docker-' + currentBuild.projectName + "-" + currentBuild.id
  def label = parameters.get('label', defaultLabel)
  def image = parameters.get('image', 'docker:latest')
  def volumes = parameters.get('volumes', [])
  def memory = parameters.get('memory', '1024Mi')

  podTemplate(cloud: cloud, label: label,
    containers: [
      containerTemplate([name: 'docker', image: "${image}", command: 'cat', ttyEnabled: true,
        resourceLimitMemory: memory])],
    volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]+volumes) {

      body()
  }
}
