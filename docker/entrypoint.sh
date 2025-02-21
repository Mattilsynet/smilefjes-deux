#!/bin/sh
envsubst '${OBSCURITY_HEADER}' < /etc/nginx/nginx.template > /etc/nginx/nginx.conf
exec "$@"
