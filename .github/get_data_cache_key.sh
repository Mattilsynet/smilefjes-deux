#!/bin/bash

get_etag() {
    curl -sI "$1" | grep -i etag | awk '{print $2}' | tr -d '"' | tr -d '[:space:]'
}

get_etag "https://matnyttig.mattilsynet.no/api/nyeste-dato"
