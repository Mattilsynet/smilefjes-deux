#!/usr/bin/env bash
set -euo pipefail

DATA_DIR="data"
STAMP_FILE="$DATA_DIR/.last_run_date"
TODAY="$(date +%F)"   # YYYY-MM-DD

already_ran_today() {
  [[ -f "$STAMP_FILE" ]] && [[ "$(cat "$STAMP_FILE")" == "$TODAY" ]]
}

mark_ran_today() {
  printf '%s\n' "$TODAY" > "$STAMP_FILE"
}

refresh_data_daily() {
  local out="$1"
  local url="$2"

  echo "Downloading $url -> $out"

  # -f: fail on HTTP errors, -sS: quiet but show errors, -L: follow redirects
  curl -fsSL "$url" | sed '1s/^\xEF\xBB\xBF//' > "$out"
}

main() {
  mkdir -p "$DATA_DIR"

  if already_ran_today; then
    echo "Already ran today ($TODAY); skipping."
    exit 0
  fi

  # If you want the stamp to only be written after *successful* downloads,
  # keep it at the end (as below). If you want "only try once per day even if it fails",
  # move mark_ran_today up before the downloads.
  refresh_data_daily "$DATA_DIR/tilsyn.csv" \
    "https://matnyttig.mattilsynet.no/smilefjes/tilsyn.csv"

  refresh_data_daily "$DATA_DIR/vurderinger.csv" \
    "https://matnyttig.mattilsynet.no/smilefjes/vurderinger.csv"

  mark_ran_today

}

main "$@"
