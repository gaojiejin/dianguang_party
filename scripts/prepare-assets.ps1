$ErrorActionPreference = 'Stop'
$repositoryRoot = Split-Path $PSScriptRoot -Parent
$source = Join-Path $repositoryRoot 'assets/banners'
$destination = Join-Path $repositoryRoot 'dianguang-party-backend/.local/uploads'
New-Item -ItemType Directory -Force -Path $destination | Out-Null
1..5 | ForEach-Object {
    Copy-Item -LiteralPath (Join-Path $source "banner-$_.png") -Destination (Join-Path $destination "banner-$_.png") -Force
}
Write-Output 'Prepared 5 local demo banners.'
