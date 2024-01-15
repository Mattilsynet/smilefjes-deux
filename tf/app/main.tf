locals {
  service_name = "smilefjes"
  fqdn = "smilefjes.mattilsynet.no."
}

module "smilefjes-ui" {
  source = "git@github.com:Mattilsynet/map-tf-cloudrun?ref=v0.9.2"

  create_cloudrun_service_account_only = false
  service_name = local.service_name
  service_location = var.region
  service_project_id = var.project_id
  service_image = "gcr.io/cloudrun/hello"
  ignore_image = true

  replicas = {
    minScale = 0
    maxScale = 2
  }

  container_limits = {
    "cpu": "256m",
    "memory": "256Mi"
  }

  run_under_shared_lb = false
  allow_unauthenticated = true
  ingress = "internal-and-cloud-load-balancing"

  dedicated_lb = {
    managed_zone_name = "smilefjes-mattilsynet-no-dns-managed-zone"
    fqdn = local.fqdn
  }
}

module "smilefjes-lb" {
  source = "github.com/Mattilsynet/map-tf-cloudrun-shared-lb?ref=v1.5.0"
  lb_name = "smilefjes-shared-lb"
  lb_project_id = var.project_id
  managed_zone_name = "smilefjes-dns-zone"
  location = "europe-north1"
  backends = {
    smilefjes = {
      fqdn = "smilefjes.mattilsynet.no."
      separate_certificate = true
      enable_ipv6 = true
    }
  }

  ssl_policy = {
    min_tls_version = "TLS_1_2"
    profile = "CUSTOM"
    custom_features = ["TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"]
  }
}

output "fqdn" {
  value = local.fqdn
}

output "service" {
  value = local.service_name
}
