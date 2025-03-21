#!/bin/bash

# Create index.html at the root of gh-pages
echo '<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EventManager Javadoc</title>
    <style>
        /* Dark Mode Theme */
        :root {
            --background-color: #121212;
            --container-bg: #1E1E1E;
            --text-color: #FFFFFF;
            --primary-color: #5A5A5A;  /* Replaced Blue with Grey */
            --secondary-color: #333333;
            --hover-bg: #5A5A5A;
            --hover-text: white;
            --border-radius: 8px;
        }

        body {
            font-family: "Arial", sans-serif;
            background-color: var(--background-color);
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            color: var(--text-color);
        }
        header {
            width: 100%;
            background: var(--primary-color);
            color: white;
            padding: 20px;
            text-align: center;
            font-size: 26px;
            font-weight: bold;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);
        }
        .container {
            max-width: 800px;
            margin: 40px auto;
            background: var(--container-bg);
            padding: 20px;
            border-radius: var(--border-radius);
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5);
            text-align: center;
        }
        h1 {
            color: var(--text-color);
            font-size: 28px;
            margin-bottom: 10px;
        }
        p {
            font-size: 18px;
            color: #b0b0b0;
        }
        ul {
            list-style-type: none;
            padding: 0;
            margin-top: 20px;
        }
        li {
            background: var(--secondary-color);
            margin: 10px 0;
            padding: 15px;
            border-radius: var(--border-radius);
            font-size: 18px;
            transition: all 0.3s ease-in-out;
        }
        li:hover {
            background: var(--hover-bg);
            color: var(--hover-text);
            transform: scale(1.05);
        }
        a {
            text-decoration: none;
            color: inherit;
            font-weight: bold;
        }
        .footer {
            margin-top: 20px;
            font-size: 14px;
            color: #888;
        }
        #version-selector {
            margin-top: 20px;
        }
        select {
            padding: 8px;
            font-size: 16px;
            border-radius: var(--border-radius);
            border: 1px solid #555;
            background: var(--secondary-color);
            color: var(--text-color);
            cursor: pointer;
        }
        option {
            background: var(--background-color);
            color: var(--text-color);
        }
    </style>
    <script>
        function switchVersion() {
            var selectedVersion = document.getElementById("version-select").value;
            window.location.href = "/EventManager/javadoc/" + selectedVersion + "/index.html";
        }
    </script>
</head>
<body>
    <header>EventManager Javadoc</header>
    <div class="container">
        <h1>Javadoc Versions</h1>
        <p>Select a version to view:</p>

        <div id="version-selector">
            <label for="version-select"><strong>Choose Version:</strong></label>
            <select id="version-select" onchange="switchVersion()">
' > gh-pages/index.html

# Loop through all versioned Javadoc folders inside gh-pages/javadoc/
for dir in gh-pages/javadoc/*; do
  if [[ -d "$dir" ]]; then
    version=$(basename "$dir")
    echo "<option value='$version'>$version</option>" >> gh-pages/index.html
  fi
done

echo '</select>
        </div>

        <ul>' >> gh-pages/index.html

for dir in gh-pages/javadoc/*; do
  if [[ -d "$dir" ]]; then
    version=$(basename "$dir")
    echo "<li><a href='javadoc/$version/'>Version $version</a></li>" >> gh-pages/index.html
  fi
done

echo '</ul>
    <div class="footer">🚀 Created by <a href="https://github.com/TheBloodyAmateur" style="color: #5A5A5A;">TheBloodyAmateur</a></div>
    </div>
</body>
</html>' >> gh-pages/index.html
