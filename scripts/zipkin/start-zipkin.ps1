#!/usr/bin/env pwsh

# PowerShell script to download and start Zipkin
# Usage: .\start-zipkin.ps1

Write-Host "Starting Zipkin setup..." -ForegroundColor Green

if (Test-Path "zipkin.jar") {
    Write-Host "zipkin.jar found, skipping download..." -ForegroundColor Yellow
} else {
    Write-Host "Downloading Zipkin..." -ForegroundColor Blue

    try {
        $zipkinUrl = "https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec"
        Invoke-WebRequest -Uri $zipkinUrl -OutFile "zipkin.jar" -UseBasicParsing
        Write-Host "Download completed!" -ForegroundColor Green
    }
    catch {
        Write-Host "Error downloading Zipkin: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
}

try {
    $javaVersion = java -version 2>&1
    Write-Host "Java found: $($javaVersion[0])" -ForegroundColor Green
}
catch {
    Write-Host "Java not found! Please install Java 8 or higher." -ForegroundColor Red
    exit 1
}

Write-Host "Starting Zipkin server..." -ForegroundColor Blue
Write-Host "Zipkin will be available at: http://localhost:9411" -ForegroundColor Cyan
Write-Host "Press Ctrl+C to stop Zipkin" -ForegroundColor Yellow
Write-Host ""

java -jar zipkin.jar