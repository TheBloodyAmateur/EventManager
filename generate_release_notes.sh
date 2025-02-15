#!/bin/bash

# Get the latest tag
LATEST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "")

# If no tag exists, start from the first commit
if [ -z "$LATEST_TAG" ]; then
  LATEST_TAG=$(git rev-list --max-parents=0 HEAD)
fi

# Get commit messages since the latest tag with specific prefixes
COMMITS=$(git log "$LATEST_TAG"..HEAD --pretty=format:"%s" | grep -E "^(feat|fix|docs|chore|refactor):")

# Initialize release notes
RELEASE_NOTES="## Changes since $LATEST_TAG\n"

# Group commits into categories
FEATURES=""
FIXES=""
DOCS=""
CHORES=""
REFACTORS=""

while IFS= read -r COMMIT; do
  if [[ $COMMIT == feat:* ]]; then
    FEATURES+="- ${COMMIT#feat: }\n"
  elif [[ $COMMIT == fix:* ]]; then
    FIXES+="- ${COMMIT#fix: }\n"
  elif [[ $COMMIT == docs:* ]]; then
    DOCS+="- ${COMMIT#docs: }\n"
  elif [[ $COMMIT == chore:* ]]; then
    CHORES+="- ${COMMIT#chore: }\n"
  elif [[ $COMMIT == refactor:* ]]; then
    REFACTORS+="- ${COMMIT#refactor: }\n"
  fi
done <<< "$COMMITS"

# Append grouped commits to release notes
if [[ -n "$FEATURES" ]]; then
  RELEASE_NOTES+="\n### 🚀 Features\n$FEATURES"
fi
if [[ -n "$FIXES" ]]; then
  RELEASE_NOTES+="\n### 🐛 Fixes\n$FIXES"
fi
if [[ -n "$DOCS" ]]; then
  RELEASE_NOTES+="\n### 📖 Documentation\n$DOCS"
fi
if [[ -n "$CHORES" ]]; then
  RELEASE_NOTES+="\n### 🛠️ Chores\n$CHORES"
fi
if [[ -n "$REFACTORS" ]]; then
  RELEASE_NOTES+="\n### 🔄 Refactors\n$REFACTORS"
fi

# Output release notes
echo -e "$RELEASE_NOTES"
