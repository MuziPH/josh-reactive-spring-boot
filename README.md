# Reactive Spring Boot with R2DBC
Postgres with Docker
docker run --name postgres_container -e POSTGRES_PASSWORD=admin -d -p 5432:5432 postgres
docker run --name postgres_container -p 5432:5432 -e POSTGRES_DB=orders -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d postgres
Pg4admin with Docker
docker run --name pg4admin_container -p 5050:80 -e PGADMIN_DEFAULT_EMAIL=postgres@gmail.com -e PGADMIN_DEFAULT_PASSWORD=postgres -d dpage/pgadmin4
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' postgres_container
* Use the IP from above as the hostname for pg4admin browser
* Change the id(Primary Key) to IDENTITY when creating the tables from the pg4admin browser
