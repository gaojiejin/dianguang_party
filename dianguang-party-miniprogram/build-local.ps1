param([ValidateSet('mp-weixin','h5')][string]$Platform='mp-weixin')
$env:UNI_INPUT_DIR=$PSScriptRoot
$env:UNI_OUTPUT_DIR=Join-Path $PSScriptRoot "unpackage/dist/build/$Platform"
$env:UNI_PLATFORM=$Platform
$env:NODE_OPTIONS='--openssl-legacy-provider'
$env:NODE_ENV='production'
$env:VUE_CLI_CONTEXT='D:\HBuilderX\HBuilderX\plugins\uniapp-cli'
node "$env:VUE_CLI_CONTEXT\bin\uniapp-cli.js"
exit $LASTEXITCODE
