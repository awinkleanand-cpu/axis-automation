#Requires -Version 5.1
<#
.SYNOPSIS
    Registers a Windows self-hosted GitHub Actions runner for axis-automation E2E tests.

.DESCRIPTION
    Downloads the GitHub Actions runner, configures it with your registration token,
    and optionally installs it as a Windows service.

    Prerequisites on this machine:
    - Java 17+
    - Maven 3.9+
    - Google Chrome
    - VPN/corporate network access to mprouat.axismaxlife.com

.EXAMPLE
    .\scripts\setup-self-hosted-runner.ps1 -RegistrationToken "AXXXXXXXXXX"
#>

param(
    [Parameter(Mandatory = $true)]
    [string]$RegistrationToken,

    [string]$RunnerName = "axis-maxlife-runner-$env:COMPUTERNAME",
    [string]$RunnerLabels = "windows,axis-maxlife-uat",
    [string]$InstallPath = "C:\actions-runner\axis-automation",
    [string]$RepoUrl = "https://github.com/awinkleanand-cpu/axis-automation",
    [string]$RunnerVersion = "2.321.0",
    [switch]$InstallAsService
)

$ErrorActionPreference = "Stop"

function Write-Step($message) {
    Write-Host "`n==> $message" -ForegroundColor Cyan
}

function Test-Command($name) {
    return [bool](Get-Command $name -ErrorAction SilentlyContinue)
}

Write-Step "Checking prerequisites"

$missing = @()
if (-not (Test-Command java)) { $missing += "Java 17+ (https://adoptium.net)" }
if (-not (Test-Command mvn)) { $missing += "Maven (https://maven.apache.org/download.cgi)" }

$chromePaths = @(
    "$env:ProgramFiles\Google\Chrome\Application\chrome.exe",
    "${env:ProgramFiles(x86)}\Google\Chrome\Application\chrome.exe"
)
if (-not ($chromePaths | Where-Object { Test-Path $_ })) {
    $missing += "Google Chrome (https://www.google.com/chrome/)"
}

if ($missing.Count -gt 0) {
    Write-Host "Missing prerequisites:" -ForegroundColor Red
    $missing | ForEach-Object { Write-Host "  - $_" -ForegroundColor Red }
    Write-Host "`nInstall the above, ensure they are in PATH, then re-run this script." -ForegroundColor Yellow
    exit 1
}

Write-Host "  Java:  $(java -version 2>&1 | Select-Object -First 1)"
Write-Host "  Maven: $(mvn -version 2>&1 | Select-Object -First 1)"
$chrome = $chromePaths | Where-Object { Test-Path $_ } | Select-Object -First 1
Write-Host "  Chrome: $(& $chrome --version 2>&1)"

Write-Step "Checking UAT portal connectivity"
try {
    $response = Invoke-WebRequest -Uri "https://mprouat.axismaxlife.com" -UseBasicParsing -TimeoutSec 20
    Write-Host "  UAT portal reachable - HTTP $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Warning "  UAT portal not reachable from this machine. Connect to VPN before running E2E tests."
}

Write-Step "Preparing runner directory: $InstallPath"
New-Item -ItemType Directory -Force -Path $InstallPath | Out-Null
Set-Location $InstallPath

$zipName = "actions-runner-win-x64-$RunnerVersion.zip"
$zipPath = Join-Path $InstallPath $zipName
$downloadUrl = "https://github.com/actions/runner/releases/download/v$RunnerVersion/$zipName"

if (-not (Test-Path "config.cmd")) {
    Write-Host "Downloading GitHub Actions runner v$RunnerVersion..."
    Invoke-WebRequest -Uri $downloadUrl -OutFile $zipPath
    Expand-Archive -Path $zipPath -DestinationPath $InstallPath -Force
    Remove-Item $zipPath -Force
}

Write-Step "Configuring runner"
Write-Host "  Name:   $RunnerName"
Write-Host "  Labels: $RunnerLabels"
Write-Host "  Repo:   $RepoUrl"

& .\config.cmd remove --unattended 2>$null

& .\config.cmd `
    --url $RepoUrl `
    --token $RegistrationToken `
    --name $RunnerName `
    --labels $RunnerLabels `
    --unattended `
    --replace

if ($LASTEXITCODE -ne 0) {
    Write-Error "Runner configuration failed. Token may be expired - generate a new one from GitHub."
    exit 1
}

Write-Host "`nRunner configured successfully!" -ForegroundColor Green

if ($InstallAsService) {
    Write-Step "Installing runner as Windows service"
    & .\svc.cmd install
    & .\svc.cmd start
    Write-Host "Service installed and started." -ForegroundColor Green
    Write-Host "The runner will start automatically on boot." -ForegroundColor Green
} else {
    Write-Step "Starting runner interactively"
    Write-Host "To install as a service later, run:" -ForegroundColor Yellow
    Write-Host "  cd $InstallPath" -ForegroundColor Yellow
    Write-Host "  .\svc.cmd install" -ForegroundColor Yellow
    Write-Host "  .\svc.cmd start" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Starting runner now (Ctrl+C to stop)..." -ForegroundColor Yellow
    & .\run.cmd
}
