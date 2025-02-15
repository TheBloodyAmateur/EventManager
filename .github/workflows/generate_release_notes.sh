#!/bin/bash

# Get the latest tag
LATEST_TAG=$(git describe --tags --abbrev=0)
# Get commit messages since the latest tag with specific prefixes
COMMITS=$(git log "$LATEST_TAG"..HEAD --pretty=format:"%s" | grep -E "^(feat|fix|docs|chore|refactor):")

# Initialize release notes
RELEASE_NOTES="## Changes since $LATEST_TAG\n"

# Append commits to release notes
while IFS= read -r COMMIT; do
  if [[ $COMMIT == feat:* ]]; then
    RELEASE_NOTES+="\n### 🚀 Features\n- ${COMMIT#feat: }"
  elif [[ $COMMIT == fix:* ]]; then
    RELEASE_NOTES+="\n### 🐛 Fixes\n- ${COMMIT#fix: }"
  elif [[ $COMMIT == docs:* ]]; then
    RELEASE_NOTES+="\n### 📖 Documentation\n- ${COMMIT#docs: }"
  elif [[ $COMMIT == chore:* ]]; then
    RELEASE_NOTES+="\n### 🛠️ Chores\n- ${COMMIT#chore: }"
  elif [[ $COMMIT == refactor:* ]]; then
    RELEASE_NOTES+="\n### 🔄 Refactors\n- ${COMMIT#refactor: }"
  fi
done <<< "$COMMITS"

# Output release notes
echo -e "$RELEASE_NOTES"
