FROM confluentinc/cp-kafka:5.5.1

# See https://github.com/prometheus/jmx_exporter and https://www.robustperception.io/monitoring-kafka-with-prometheus for the config
# Grafana dashboard: https://grafana.com/grafana/dashboards/721
ADD jmx-exporter-kafka.yml /etc/jmx-exporter/jmx-exporter-kafka.yml
ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.13.0/jmx_prometheus_javaagent-0.13.0.jar /opt/jmx-exporter/jmx-exporter-javaagent.jar