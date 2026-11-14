$env:UNI_INPUT_DIR=$PSScriptRoot
$env:UNI_OUTPUT_DIR=Join-Path $PSScriptRoot 'unpackage/dist/dev/h5'
$env:UNI_PLATFORM='h5'
$env:NODE_ENV='development'
$env:NODE_OPTIONS='--openssl-legacy-provider'
$env:VUE_CLI_CONTEXT='D:\HBuilderX\HBuilderX\plugins\uniapp-cli'
node "$env:VUE_CLI_CONTEXT\bin\uniapp-cli.js"
