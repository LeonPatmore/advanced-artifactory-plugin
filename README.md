# Advanced Artifactory Plugin

A gradle Plugin to make pushing to artifactory easier! The plugin configures everything you need to push, you just need to set the following env vars:

- `ARTIFACTORY_URL`: Base url of the artifactory URL.
- `ARTIFACTORY_USERNAME`
- `ARTIFACTORY_PASSWORD`
- `ARTIFACTORY_REPO_KEY`

## Project Structure

- `advanced-artifactory-plugin-kotlin`: The plugin code.
- `example-kotlin`: An example project using this plugin.
