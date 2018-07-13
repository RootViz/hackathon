# Core

This AWS Lambda Core library containing core framework.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

### Installing

Things you will need to be installed:

1. [Gradle](https://gradle.org/)
```
brew install gradle
```

2. [IntelliJ](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/ide/)

3. [Java8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
```
brew install java8
```

4. [Python3](https://www.python.org/downloads/)
```
brew install python3
```

5. [boto](https://github.com/boto/boto)
```
brew install pip
pip install boto
```


## Running the tests

```
gradle clean test
```

## Deployment

```
gradle clean build
```

After above command upload the zip files of sub modules to [AWS-Lambda](https://aws.amazon.com/lambda/)
configure input and output sources and lambda function and run.

## Built With

* [AWS-Lambda](https://aws.amazon.com/lambda/) - The framework used
* [Gradle](https://gradle.org/) - Dependency Management

## Contributing

Please read [CONTRIB.md]() for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

## Authors

* **Rhutwij Tulankar** - *Initial work*



## Acknowledgments

* Lambda base structure project.
* Constructed for hackathon.
