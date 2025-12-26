# eXXcellent programming challenge

The solution of eXXcellent programming challenge - Weatherdata

# Description

This repository provides one possible solution
of  [coding challenge (Weatherdata) from eXXcellent](https://github.com/exxcellent/programming-challenge/tree/challenge-weatherdata).

# Requirements

- Java 17 or above
- Maven 3.9.x or above
- git 2.x or above

# Installation and Usage

```bash
# Clone the repository
git clone git@github.com:cegraris/programming-challenge.git
cd programming-challenge
git switch challenge-weatherdata-jwu

# Build the project
mvn clean package
```

After building, the executable JAR file will be located at `target/challenge-1.1-SNAPSHOT.jar`.

## Usage

```bash
java -jar target/challenge-1.1-SNAPSHOT.jar [OPTIONS] <inputFile>
```

Where:

- `<inputFile>` is the path to the input data file
- `[OPTIONS]` control the analysis mode and input format

### Options

| Option          | Description                                                                        |
|-----------------|------------------------------------------------------------------------------------|
| `--weather`     | Analyze weather data to find the day(s) with smallest temperature spread (default) |
| `--football`    | Analyze football data to find the team(s) with smallest absolute goal difference   |
| `-h, --help`    | Show help message                                                                  |
| `-V, --version` | Show version information                                                           |
| `-f, --format`  | Input file format. (Default: CSV)                                                  |

### Examples

Analyze weather data:

```bash
java -jar target/challenge-1.1-SNAPSHOT.jar --weather target\classes\de\exxcellent\challenge\weather.csv
or
java -jar target/challenge-1.1-SNAPSHOT.jar target\classes\de\exxcellent\challenge\weather.csv
or
java -jar target/challenge-1.1-SNAPSHOT.jar --weather target\classes\de\exxcellent\challenge\weather.csv -f CSV
```

Analyze football data:

```bash
java -jar target/challenge-1.1-SNAPSHOT.jar --football target\classes\de\exxcellent\challenge\football.csv
```

# Project Structure

# Testing
