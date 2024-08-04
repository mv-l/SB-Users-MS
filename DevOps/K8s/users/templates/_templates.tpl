{{- define "users.deployment" -}}
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ .appName }}-deployment
  labels:
    app: {{ .appName }}
spec:
  replicas: {{ .replicasCount }}
  selector:
    matchLabels:
      app: {{ .appName }}
  template:
    metadata:
      labels:
        app: {{ .appName }}
    spec:
      containers:
        - name: {{ .appName }}
          image: {{ .container.imageName }}:{{ .container.imageVersion }}
          imagePullPolicy: IfNotPresent
          resources:
            limits:
              memory: 512Mi
              cpu: 500m
{{- if .container.env }}
          env:
{{ toYaml .container.env | indent 10 }}
{{- end }}
{{- end }}

{{- define "users.service" }}
{{- if .service }}
apiVersion: v1
kind: Service
metadata:
  name: {{ .appName }}-service
spec:
  selector:
    app: {{ .appName }}
  ports:
    - port: {{ .service.port }}
    {{ if .service.targetPort -}}targetPort: {{ .service.targetPort }} {{ end }}
    {{ if .service.nodePort }}nodePort: {{ .service.nodePort }} {{ end }}
{{- end }}
{{- end}}
