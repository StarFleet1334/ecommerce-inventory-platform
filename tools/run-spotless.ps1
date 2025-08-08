$ErrorActionPreference = "Stop"

$modules = @(
  "api-gateway",
  "graphs",
  "inventory-service",
  "rocketmq-service",
  "order-processing-service"
)

foreach ($module in $modules) {
  $wrapper = Join-Path $module "gradlew.bat"
  if (-not (Test-Path $wrapper)) {
    Write-Host "Skipping $module (no gradlew.bat)" -ForegroundColor Yellow
    continue
  }

  Push-Location $module
  try {
    $tasks = & ".\gradlew.bat" tasks --all --no-daemon --console=plain
    if ($LASTEXITCODE -ne 0) {
      Write-Host "Skipping $module (failed to list tasks)" -ForegroundColor Yellow
      continue
    }

    if ($tasks -match "(?m)^\s*spotlessApply\s") {
      Write-Host "Running Spotless in $module ..."
      & ".\gradlew.bat" spotlessApply -q --no-daemon
      if ($LASTEXITCODE -ne 0) {
        throw "spotlessApply failed in $module"
      }
    } else {
      Write-Host "Skipping $module (no spotlessApply task)" -ForegroundColor Yellow
    }
  } finally {
    Pop-Location
  }
}

Write-Host "Spotless run complete."
