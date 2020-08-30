FROM confluentinc/cp-kafka-connect:5.5.1

# Grafana dashboard: # https://grafana.com/grafana/dashboards/11962
ADD jmx-exporter-kafka.yml /etc/jmx-exporter/jmx-exporter-kafka.yml
ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.13.0/jmx_prometheus_javaagent-0.13.0.jar /opt/jmx-exporter/jmx-exporter-javaagent.jar
