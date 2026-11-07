#!/bin/bash
# audit-dependencies.sh

echo "🔍 Recherche des références à des JARs locaux dans les build.gradle.kts..."

find . -name "build.gradle.kts" -exec grep -H "files(\"lib" {} \; | \
  sed 's/.*files("\([^"]*\)").*/\1/' | \
  sort -u | \
  while read jar; do
    echo "📦 $jar"
    # Essayer de trouver l'équivalent Maven Central
    basename="${jar##*/}"
    basename="${basename%.jar}"
    echo "   🔎 Recherche Maven Central pour: $basename"
    curl -s "https://search.maven.org/solrsearch/select?q=g:*+AND+a:*${basename}*&rows=3&wt=json" | \
      jq -r '.response.docs[]? | "      ✅ \(.g):\(.a):\(.latestVersion)"' 2>/dev/null || \
      echo "      ❌ Non trouvé sur Maven Central"
    echo
  done