#Requires -Version 5.1
<#
.SYNOPSIS
    Verifies Java, Maven, Chrome, and UAT portal connectivity for E2E tests.
#>

$ErrorActionPreference = "Continue"
$allPassed = $true

function Test-Item($label, $passed, $detail) {
    $status = if ($passed) { "OK" } else { "MISSING" }
    $color = if ($passed) { "Green" } else { "Red" }
    Write-Host ("[{0}] {1}" -f $status, $label) -ForegroundColor $color
    if ($detail) { Write-Host "      $detail" }
    if (-not $passed) { $script:allPassed = $false }
}

Write-Host "`nAxis Max Life Automation - Prerequisites Check`n" -ForegroundColor Cyan

if (Get-Command java -ErrorAction SilentlyContinue) {
    Test-Item "Java" $true (java -version 2>&1 | Select-Object -First 1)
} else {
    Test-Item "Java" $false "Install Java 17+ from https://adoptium.net"
}

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Test-Item "Maven" $true (mvn -version 2>&1 | Select-Object -First 1)
} else {
    Test-Item "Maven" $false "Install Maven from https://maven.apache.org/download.cgi"
}

$chromePaths = @(
    "$env:ProgramFiles\Google\Chrome\Application\chrome.exe",
    "${env:ProgramFiles(x86)}\Google\Chrome\Application\chrome.exe"
)
$chrome = $chromePaths | Where-Object { Test-Path $_ } | Select-Object -First 1
if ($chrome) {
    Test-Item "Chrome" $true (& $chrome --version 2>&1)
} else {
    Test-Item "Chrome" $false "Install from https://www.google.com/chrome/"
}

try {
    $response = Invoke-WebRequest -Uri "https://mprouat.axismaxlife.com" -UseBasicParsing -TimeoutSec 20
    Test-Item "UAT Portal" $true "HTTP $($response.StatusCode) - mprouat.axismaxlife.com"
} catch {
    Test-Item "UAT Portal" $false "Not reachable - connect to VPN ($($_.Exception.Message))"
}

Write-Host ""
if ($allPassed) {
    Write-Host "All checks passed. Ready to run: mvn clean test" -ForegroundColor Green
    exit 0
} else {
    Write-Host "Some checks failed. Fix the items above before running E2E tests." -ForegroundColor Red
    exit 1
}
