# Jenkins Setup (Docker Compose)

This project now includes a Jenkins service in `infrastructure/dockers/docker-compose.yml` and a pipeline file at `Jenkinsfile`.

## 1) Start Jenkins

From repo root:

```powershell
Set-Location "C:\Users\NNHY\My_Data\coding\ticket-booking\infrastructure\dockers"
docker compose up -d --build jenkins
```

Open Jenkins at:

- `http://localhost:9090/jenkins`

## 2) First-Time Jenkins Admin Setup

Wizard is disabled by default. If security is enabled and an unlock token is requested:

```powershell
docker exec -it jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Then create an admin user and log in.

## 3) Create Pipeline Job

1. **New Item** -> **Pipeline**
2. Name: `ticket-booking-ci`
3. In Pipeline section: choose **Pipeline script from SCM**
4. SCM: **Git**
5. Repository URL: your repo URL
6. Branch: `*/main` (or your branch)
7. Script Path: `Jenkinsfile`
8. Save and **Build Now**

## 4) What the Pipeline Does

- Checkout source code
- Build and run tests: `mvn clean test -P dev`
- Package artifacts: `mvn package -DskipTests -P dev`
- Archive generated JAR files
- Optional deploy stage (`DEPLOY_STACK=true`) runs:
  - `docker compose -f infrastructure/dockers/docker-compose.yml --env-file .env up -d --build`

## 5) Required Credentials / Secrets

For basic CI build/test only, no credentials are required beyond repo access.

For deploy stage:

- Ensure a valid `.env` file exists in repo root (or pass a custom `ENV_FILE_PATH` parameter).
- Keep secrets out of source control.

## 6) Useful Commands

```powershell
# Jenkins logs
docker logs -f jenkins

# Restart Jenkins
docker restart jenkins

# Rebuild Jenkins image after plugin/tool changes
Set-Location "C:\Users\NNHY\My_Data\coding\ticket-booking\infrastructure\dockers"
docker compose build --no-cache jenkins
docker compose up -d jenkins
```

## Notes

- Jenkins container runs as `root` in this setup so it can use Docker socket mounts.
- This is practical for local/dev CI. For production hardening, use stricter RBAC, isolated agents, and a safer Docker access model.

