# richAF

**richAF** is a Java application that uses a custom web crawler to gather articles from user-provided URLs and then leverages the [Ollama API](https://github.com/jmorganca/ollama) to classify articles and predict potential stock movements. It identifies whether an article mentions a specific ticker symbol and, if so, runs a prediction model to estimate the stock’s trend.

---

## Table of Contents
1. [Features](#features)
2. [Technologies and Dependencies](#technologies-and-dependencies)
3. [Installation](#installation)
4. [Usage](#usage)
5. [Workflow Overview](#workflow-overview)
6. [Configuration](#configuration)
7. [Limitations & Disclaimer](#limitations--disclaimer)
8. [License](#license)
9. [Acknowledgments](#Acknowledgments)

---

## Features

- **Custom Web Crawler**  
  Extracts and processes articles from user-provided URLs.

- **Ticker Detection**  
  Searches for stock ticker symbols within the extracted text.

- **Ollama API Integration**  
  Calls the [Ollama API](https://github.com/jmorganca/ollama) to classify articles and predict stock movements.

- **JSON Output**  
  Stores classification results and predictions in a JSON format for downstream analysis or integration into other tools.

- **JavaFX GUI**  
  Comes bundled with a JavaFX-based interface (`app.fxml`) to manage the crawling and classification process interactively.

---

## Technologies and Dependencies

1. **Java 21+**  
   The core language used.
2. **JavaFX**  
   Provides the graphical user interface.
3. **Gradle**
   Handles project build and dependency management.
4. **Ollama API**  
   External inference API that classifies articles and predicts stock movements.
5. **SQLite Database**  
   Handles storage of articles and result-json
---

## Installation

1. **Clone the Repository**
2. **Configure Ollama**  
   * Install Ollama locally and pull model "llama3.2"
   ```bash 
    brew install ollama
    ollama pull llama3.2
3. **Run with Gradle**
   ```bash 
    ./gradlew build
    ./gradlew run
   
---
## Usage
1. **Start the Application**
3. **Provide Articles**
    - You can input a list of article URLs via the GUI 
    - The crawler begins fetching these URLs to extract textual content.

3. **Crawler Execution**
    - The crawler logs its progress in the console/terminal window.
    - It also reports any invalid links or parsing errors.

4. **Ticker Detection & Ollama Classification**
    - Once the crawler extracts text, the app sends the content to the Ollama API.
    - Ollama classifies articles based on the presence of ticker symbols and attempts a predictive analysis on whether the mentioned stock might move up or down.

5. **View/Export Results**
    - Results are saved in a JSON structure.
    - The JSON includes:
        - Article URL
        - Mentioned ticker symbols
        - Whether the article is a match to the classifier task
        - Prediction from Ollama
        - Article summary

6. **Stop or Cancel**
    - You can cancel the crawler or classification process at any time by closing the application or using built-in “Cancel” buttons

---

## Workflow Overview

1. **User Input**  
   Provide URLs via the GUI or config file.

2. **Crawl & Parse**  
   The crawler retrieves HTML content and parses it for text.

4. **Ollama API Call**  
   Each article is sent to Ollama for classification and prediction.

5. **Result Storage**  
   The app aggregates all results in JSON, which you can then use for analytics, logs, or further processing.

---

## Configuration
Depending on your environment, you may need to configure:

- **Ollama API Endpoint**
    - Specify in developer menu if it’s not running on the default `localhost:11411`.

---

## Limitations & Disclaimer

- **Experimental**: This application is for **experimental or academic use only**. Predictions about stock movements come from an AI model that may not be accurate.
- **Not Financial Advice**: Any output or prediction from this tool should not be taken as financial advice. Always do your own research and consider consulting a professional.
- **Unpredictable Parsing**: Some articles may contain unusual HTML structures leading to parse errors or incomplete extraction of content.

---

**Happy crawling and classifying!** If you have any questions, suggestions, or run into issues, please open an issue or reach out via pull request.

---

## Acknowledgments
all icons by: designed by UIcons from Flaticon (https://www.flaticon.com)