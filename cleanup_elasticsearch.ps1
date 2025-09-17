# Script to remove Elasticsearch related files
$filesToDelete = @(
    "src\main\java\com\sms\elasticsearch\*.java",
    "src\main\java\com\sms\service\StudentSearchService.java",
    "src\main\java\com\sms\service\TeacherSearchService.java",
    "src\main\java\com\sms\service\UnifiedSearchService.java",
    "src\main\java\com\sms\listener\ElasticsearchIndexListener.java"
)

# Remove each file
foreach ($file in $filesToDelete) {
    if (Test-Path $file) {
        Remove-Item -Path $file -Force
        Write-Host "Removed: $file"
    } else {
        Write-Host "Not found: $file"
    }
}

Write-Host "Cleanup complete!"
