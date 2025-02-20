
# Create index.html at the root of gh-pages
echo '<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EventManager Javadoc</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            text-align: center;
        }
        header {
            background: #007BFF;
            color: white;
            padding: 20px;
            font-size: 24px;
            font-weight: bold;
        }
        .container {
            max-width: 800px;
            margin: 40px auto;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #333;
            font-size: 26px;
        }
        p {
            font-size: 18px;
            color: #666;
        }
        ul {
            list-style-type: none;
            padding: 0;
        }
        li {
            margin: 10px 0;
            font-size: 18px;
        }
        a {
            text-decoration: none;
            color: #007BFF;
            font-weight: bold;
            transition: color 0.3s ease-in-out;
        }
        a:hover {
            color: #0056b3;
        }
        .footer {
            margin-top: 20px;
            font-size: 14px;
            color: #888;
        }
    </style>
</head>
<body>
    <header>EventManager Javadoc</header>
    <div class="container">
        <h1>Javadoc Versions</h1>
        <p>Select a version to view:</p>
        <ul>' > gh-pages/index.html

# Loop through all versioned Javadoc folders inside gh-pages/javadoc/
for dir in gh-pages/javadoc/*; do
  if [[ -d "$dir" ]]; then
    version=$(basename "$dir")
    echo "<li><a href='javadoc/$version/'>Version $version</a></li>" >> gh-pages/index.html
  fi
done

echo '</ul>
    <div class="footer">Documentation hosted on GitHub Pages</div>
    </div>
</body>
</html>' >> gh-pages/index.html