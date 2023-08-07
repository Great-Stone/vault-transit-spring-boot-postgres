terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }
  }
}

provider "docker" {}

resource "docker_image" "postgres" {
  name = "postgres:latest"
}

resource "docker_container" "postgres_container" {
  image = docker_image.postgres.image_id
  name  = "postgres_container"

  ports {
    internal = 5432
    external = 5432
  }

  env = [
    "POSTGRES_USER=admin",
    "POSTGRES_PASSWORD=password",
    "POSTGRES_DB=mydb"
  ]
}
