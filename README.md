# eXXcellent programming challenge

The solution of eXXcellent programming challenge - Weatherdata

# Description

This repository provides one possible solution
of  [coding challenge (Weatherdata) from eXXcellent](https://github.com/exxcellent/programming-challenge/tree/challenge-weatherdata).

# Implementation characteristics (assumptions)

1. Currently, only CSV input is supported, and the program validates whether the file is a proper CSV.

2. Some columns are considered mandatory, while others are optional. Further processing will only continue if all
   required columns are present in the header.
   - In Weather mode, the required columns are Day, MxT, and MnT.
   - In Football mode, the required columns are Team, Goals, and Goals Allowed.

3. When reading data rows, any row that cannot be parsed will be skipped. They will be reported.

4. For the current analysis logic, an empty result is considered an error.

5. The program validates several obvious constraints within the table:
   - In Weather mode, the day values must be unique, and MxT must be greater than or equal to MnT.
   - In Football mode, team names must be unique, and both Goals and Goals Allowed must be greater than or equal to
     zero.

6. When there are multiple results—for example, multiple records that share the same minimum value, all of them are
   considered correct results.

# Requirements

- Java 17 or above
- Maven 3.9.x or above
- git 2.x or above

# Installation and Usage

```bash
# Clone the repository
git clone https://github.com/cegraris/programming-challenge.git
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

(The following examples use Linux-style file path formats. If you are using Windows, please remember to convert the
slash characters accordingly.)

Analyze weather data:

```bash
java -jar target/challenge-1.1-SNAPSHOT.jar --weather target/classes/de/exxcellent/challenge/weather.csv
or
java -jar target/challenge-1.1-SNAPSHOT.jar target/classes/de/exxcellent/challenge/weather.csv
or
java -jar target/challenge-1.1-SNAPSHOT.jar --weather target/classes/de/exxcellent/challenge/weather.csv -f CSV
```

Analyze football data:

```bash
java -jar target/challenge-1.1-SNAPSHOT.jar --football target/classes/de/exxcellent/challenge/football.csv
```

# UML

The diagram below shows the UML diagram for the core classes (constructors are not listed).
![UML](/UML.jpg "UML Diagram of core classes")

