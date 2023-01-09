# ------------------------------------
# Environment and Variables
# ------------------------------------
ENV ?= dev
DEPLOYMENT_BUCKET ?= jobs-config-dev
VERSION = $(shell cat build.sbt | grep "version :=" | tr -d 'version :=' | tr -d '"' )

# ------------------------------------
# Tools
# ------------------------------------
.DEFAULT_GOAL := help
.PHONY: help
help: ## help
	@ printf "\n"
	@ printf "Make targets are:\n"
	@ grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

# ------------------------------------
# Targets
# ------------------------------------
.PHONY: clean
clean: ## clean sbt target
	sbt clean

build: ## creates a Fat-Jar
	sbt 'set test in assembly := {}' clean assembly

test: ## run tests
	sbt test

deploy: ## deploy FatJar
	aws s3 cp \
		target/scala-2.*/data-lake-deduplicates-assembly-$(VERSION).jar \
		s3://${DEPLOYMENT_BUCKET}/jars/admin/data-lake-deduplicates-assembly-$(VERSION).jar
